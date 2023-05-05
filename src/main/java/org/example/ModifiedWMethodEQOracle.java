package org.example;

import de.learnlib.api.EquivalenceOracle;
import de.learnlib.api.MembershipOracle;
import de.learnlib.oracles.DefaultQuery;
import net.automatalib.automata.UniversalDeterministicAutomaton;
import net.automatalib.automata.concepts.Output;
import net.automatalib.automata.transout.MealyMachine;
import net.automatalib.commons.util.collections.CollectionsUtil;
import net.automatalib.util.automata.Automata;
import net.automatalib.words.Word;
import net.automatalib.words.WordBuilder;

import java.util.*;

public class ModifiedWMethodEQOracle<A extends UniversalDeterministicAutomaton<?, I, ?, ?, ?> & Output<I, D>, I, D>
        implements EquivalenceOracle<A, I, D> {
    public static class MealyModifiedWMethodEQOracle<I, O> extends
            ModifiedWMethodEQOracle<MealyMachine<?, I, ?, O>, I, Word<O>> implements MealyEquivalenceOracle<I, O> {
        public MealyModifiedWMethodEQOracle(int maxDepth, MembershipOracle<I, Word<O>> sulOracle) {
            super(maxDepth, sulOracle);
        }
    }
    private int maxDepth;
    private final MembershipOracle<I, D> sulOracle;
    public ModifiedWMethodEQOracle(int maxDepth, MembershipOracle<I, D> sulOracle) {
        this.maxDepth = maxDepth;
        this.sulOracle = sulOracle;
    }
    @Override
    public DefaultQuery<I, D> findCounterExample(A hypothesis, Collection<? extends I> inputs) {
        List<Word<I>> transCover = Automata.transitionCover(hypothesis, inputs);
        List<Word<I>> charSuffixes = Automata.characterizingSet(hypothesis, inputs);

        // Special case: List of characterizing suffixes may be empty,
        // but in this case we still need to test!
        if (charSuffixes.isEmpty())
            charSuffixes = Collections.singletonList(Word.<I> epsilon());

        WordBuilder<I> wb = new WordBuilder<>();

        DefaultQuery<I, D> query;
        D hypOutput;
        String output;
        Word<I> queryWord;
        boolean blacklisted;

        HashSet<Word<I>> blacklist = new HashSet<Word<I>>();

        for (Word<I> trans : transCover) {
            query = new DefaultQuery<>(trans);
            sulOracle.processQueries(Collections.singleton(query));

            hypOutput = hypothesis.computeOutput(trans);
            if (!Objects.equals(hypOutput, query.getOutput()))
                return query;

            output = query.getOutput().toString();

            // Detect closed connection to continue with queries with different prefixes
            if (output.endsWith("ConnectionClosed") || output.endsWith("ConnectionClosedEOF") || output.endsWith("ConnectionClosedException")) {
                blacklist.add(trans);
                continue;
            }

            //for(int start = 1; start < maxDepth; start++) {
            for (List<? extends I> middle : CollectionsUtil.allTuples(inputs, 1, maxDepth)) {
                wb.append(trans).append(middle);
                queryWord = wb.toWord();
                wb.clear();

                // Check if trans | middle has a prefix on the blacklist
                blacklisted = false;
                for(Word<I> w: blacklist) {
                    if(w.isPrefixOf(queryWord)) {
                        blacklisted = true;
                        break;
                    }
                }
                if(blacklisted) continue;

                query = new DefaultQuery<>(queryWord);
                sulOracle.processQueries(Collections.singleton(query));

                hypOutput = hypothesis.computeOutput(queryWord);

                if (!Objects.equals(hypOutput, query.getOutput()))
                    return query;

                output = query.getOutput().toString();

                if (output.endsWith("ConnectionClosed") || output.endsWith("ConnectionClosedEOF") || output.endsWith("ConnectionClosedException")) {
                    // Remember this prefix and ignore queries starting with this after this
                    blacklist.add(queryWord);
                    continue;
                }

                for (Word<I> suffix : charSuffixes) {
                    wb.append(trans).append(middle).append(suffix);
                    queryWord = wb.toWord();
                    wb.clear();

                    query = new DefaultQuery<>(queryWord);
                    hypOutput = hypothesis.computeOutput(queryWord);
                    sulOracle.processQueries(Collections.singleton(query));

                    if (!Objects.equals(hypOutput, query.getOutput()))
                        return query;

                    output = query.getOutput().toString();
                    if (output.endsWith("ConnectionClosed") || output.endsWith("ConnectionClosedEOF") || output.endsWith("ConnectionClosedException")) {
                        // Remember this prefix and ignore queries starting with this after this
                        blacklist.add(queryWord);
                    }
                }
            }
            //}
        }

        return null;
    }
}
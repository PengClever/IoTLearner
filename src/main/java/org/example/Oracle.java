package org.example;

import de.learnlib.api.MembershipOracle.MealyMembershipOracle;
import de.learnlib.api.Query;
import de.learnlib.logging.LearnLogger;
import net.automatalib.words.Word;
import net.automatalib.words.WordBuilder;

import java.util.Collection;

public class Oracle<I, D> implements MealyMembershipOracle<I, D> {
    LearnLogger logger;
    IoTLearnerSUL sul;
    public Oracle(IoTLearnerSUL sul, LearnLogger logger) {
        this.sul = sul;
        this.logger = logger;
    }
    @Override
    public Word<D> answerQuery(Word<I> prefix, Word<I> suffix) {
        WordBuilder<D> wbPrefix = new WordBuilder<>(prefix.length());
        WordBuilder<D> wbSuffix = new WordBuilder<>(suffix.length());

        this.sul.pre();
        try {
            // Prefix: Execute symbols, only log output
            for(I sym : prefix) {
                wbPrefix.add((D) this.sul.step((String) sym));
            }

            // Suffix: Execute symbols, outputs constitute output word
            for(I sym : suffix) {
                wbSuffix.add((D) this.sul.step((String) sym));
            }

            logger.logQuery("[" + prefix.toString() + " | " + suffix.toString() +  " / " + wbPrefix.toWord().toString() + " | " + wbSuffix.toWord().toString() + "]");
        }
        finally {
            sul.post();
        }

        return wbSuffix.toWord();
    }
    public void processQueries(Collection<? extends Query<I, Word<D>>> queries) {
        for (Query<I,Word<D>> q : queries) {
            Word<D> output = answerQuery(q.getPrefix(), q.getSuffix());
            q.answer(output);
        }
    }
}

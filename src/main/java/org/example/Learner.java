package org.example;

import de.learnlib.acex.analyzers.AcexAnalyzers;
import de.learnlib.algorithms.dhc.mealy.MealyDHC;
import de.learnlib.algorithms.kv.mealy.KearnsVaziraniMealy;
import de.learnlib.algorithms.lstargeneric.mealy.ExtensibleLStarMealyBuilder;
import de.learnlib.algorithms.malerpnueli.MalerPnueliMealy;
import de.learnlib.algorithms.rivestschapire.RivestSchapireMealy;
import de.learnlib.algorithms.ttt.mealy.TTTLearnerMealy;
import de.learnlib.api.EquivalenceOracle;
import de.learnlib.api.LearningAlgorithm;
import de.learnlib.counterexamples.AcexLocalSuffixFinder;
import de.learnlib.eqtests.basic.RandomWordsEQOracle;
import de.learnlib.eqtests.basic.WMethodEQOracle;
import de.learnlib.eqtests.basic.WpMethodEQOracle;
import de.learnlib.logging.LearnLogger;
import de.learnlib.oracles.CounterOracle;
import de.learnlib.oracles.DefaultQuery;
import de.learnlib.statistics.Counter;
import de.learnlib.statistics.SimpleProfiler;
import net.automatalib.automata.transout.MealyMachine;
import net.automatalib.util.graphs.dot.GraphDOT;
import net.automatalib.words.Word;
import net.automatalib.words.impl.SimpleAlphabet;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;


public class Learner {
    LearnerConfig config;
    IoTLearnerSUL sul;
    SimpleAlphabet<String> alphabet;

    Oracle<String, String> vpnMemOracle;
    CounterOracle.MealyCounterOracle<String, String> statsMemOracle;
    CounterOracle.MealyCounterOracle<String, String> statsCachedMemOracle;
    LearningAlgorithm<MealyMachine<?, String, ?, String>, String, Word<String>> learningAlgorithm;

    Oracle<String, String> vpnEqOracle;
    CounterOracle.MealyCounterOracle<String, String> statsEqOracle;
    CounterOracle.MealyCounterOracle<String, String> statsCachedEqOracle;
    EquivalenceOracle<MealyMachine<?, String, ?, String>, String, Word<String>> equivalenceAlgorithm;
    public Learner(LearnerConfig config) throws Exception {
//        System.out.println("Learner(LearnerConfig config)");
        this.config = config;

        // 输出文件夹
        Path path = Paths.get(config.outputDir);
        if(Files.notExists(path)) {
            Files.createDirectories(path);
        }
        // 配置日志信息
        configureLogging(config.outputDir);
        // 声明 LearnLogger 对象
        LearnLogger log = LearnLogger.getLogger(Learner.class.getSimpleName());
        log.log(Level.INFO, "日志初始化");
        // 创建VPN目标系统
        sul = new IoTLearnerSUL(config);
        alphabet = sul.getAlphabet();
        loadLearningAlgorithm(config.learningAlgorithm, alphabet, sul);
        loadEquivalenceAlgorithm(config.equivalenceAlgorithm, sul);
    }

    private void loadLearningAlgorithm(String algorithm, SimpleAlphabet<String> alphabet, IoTLearnerSUL sul) throws Exception {
        System.out.println("loadLearningAlgorithm(String algorithm, SimpleAlphabet<String> alphabet, VPNLearnerSUL sul)");

        vpnMemOracle = new Oracle<>(sul, LearnLogger.getLogger("learningQueries"));
        statsMemOracle = new CounterOracle.MealyCounterOracle<>(vpnMemOracle, "membership queries to SUL");
        statsCachedMemOracle = new CounterOracle.MealyCounterOracle<>(statsMemOracle, "membership queries to cache");

        switch(algorithm.toLowerCase()) {
            case "lstar":
                learningAlgorithm = new ExtensibleLStarMealyBuilder<String, String>().withAlphabet(alphabet).withOracle(statsCachedMemOracle).create();
                break;

            case "dhc":
                learningAlgorithm = new MealyDHC<>(alphabet, statsCachedMemOracle);
                break;

            case "kv":
                learningAlgorithm = new KearnsVaziraniMealy<>(alphabet, statsCachedMemOracle, true, AcexAnalyzers.BINARY_SEARCH);
                break;

            case "ttt":
                AcexLocalSuffixFinder suffixFinder = new AcexLocalSuffixFinder(AcexAnalyzers.BINARY_SEARCH, true, "Analyzer");
                learningAlgorithm = new TTTLearnerMealy<>(alphabet, statsCachedMemOracle, suffixFinder);
                break;

            case "mp":
                learningAlgorithm = new MalerPnueliMealy<>(alphabet, statsCachedMemOracle);
                break;

            case "rs":
                learningAlgorithm = new RivestSchapireMealy<>(alphabet, statsCachedMemOracle);
                break;

            default:
                throw new Exception("Unknown learning algorithm " + config.learningAlgorithm);
        }
    }

    private void loadEquivalenceAlgorithm(String algorithm, IoTLearnerSUL sul) throws Exception {
        System.out.println("loadEquivalenceAlgorithm(String algorithm, SimpleAlphabet<String> alphabet, VPNLearnerSUL sul)");

        vpnEqOracle = new Oracle<>(sul, LearnLogger.getLogger("equivalenceQueries"));
        statsEqOracle = new CounterOracle.MealyCounterOracle<>(vpnEqOracle, "equivalence queries to SUL");
        statsCachedEqOracle = new CounterOracle.MealyCounterOracle<>(statsEqOracle, "equivalence queries to cache");

        switch(algorithm.toLowerCase()) {
            case "wmethod":
                equivalenceAlgorithm = new WMethodEQOracle.MealyWMethodEQOracle<>(config.maxDepth, statsCachedEqOracle);
                break;

            case "modifiedwmethod":
                equivalenceAlgorithm = new ModifiedWMethodEQOracle.MealyModifiedWMethodEQOracle<>(config.maxDepth, statsCachedEqOracle);
                break;

            case "wpmethod":
                equivalenceAlgorithm = new WpMethodEQOracle.MealyWpMethodEQOracle<>(config.maxDepth, statsCachedEqOracle);
                break;

            case "randomwords":
                equivalenceAlgorithm = new RandomWordsEQOracle.MealyRandomWordsEQOracle<>(statsCachedEqOracle, config.minLength, config.maxLength, config.nrQueries, new Random(config.seed));
                break;

            default:
                throw new Exception("Unknown equivalence algorithm " + config.equivalenceAlgorithm);
        }
    }

    private void configureLogging(String outputDir) throws SecurityException, IOException {
//        System.out.println("configureLogging(String outputDir)");

        // 创建 LearnLogger 实例化对象获取 LearnLib 日志
        LearnLogger loggerLearnlib = LearnLogger.getLogger("de.learnlib");
        loggerLearnlib.setLevel(Level.ALL);
        FileHandler fhLearnlibLog = new FileHandler(outputDir + "/learnlib.log");
        loggerLearnlib.addHandler(fhLearnlibLog);
        fhLearnlibLog.setFormatter(new SimpleFormatter());

        // Learner 日志
        LearnLogger loggerLearner = LearnLogger.getLogger(Learner.class.getSimpleName());
        loggerLearner.setLevel(Level.ALL);
        FileHandler fhLearnerLog = new FileHandler(outputDir + "/learner.log");
        loggerLearner.addHandler(fhLearnerLog);
        fhLearnerLog.setFormatter(new SimpleFormatter());

        // 成员查询日志
        LearnLogger loggerLearningQueries = LearnLogger.getLogger("learningQueries");
        loggerLearningQueries.setLevel(Level.ALL);
        FileHandler fhLearningQueriesLog = new FileHandler(outputDir + "/learningQueries.log");
        loggerLearningQueries.addHandler(fhLearningQueriesLog);
        fhLearningQueriesLog.setFormatter(new SimpleFormatter());

        // 等价性查询日志
        LearnLogger loggerEquivalenceQueries = LearnLogger.getLogger("equivalenceQueries");
        loggerEquivalenceQueries.setLevel(Level.ALL);
        FileHandler fhEquivalenceQueriesLog = new FileHandler(outputDir + "/equivalenceQueries.log");
        loggerEquivalenceQueries.addHandler(fhEquivalenceQueriesLog);
        fhEquivalenceQueriesLog.setFormatter(new SimpleFormatter());
    }

    public static void writeDotModel(MealyMachine<?, String, ?, String> model, SimpleAlphabet<String> alphabet, String filename) throws IOException {
        // Write output to dot-file
        File dotFile = new File(filename);
        PrintStream psDotFile = new PrintStream(dotFile);
        GraphDOT.write(model, alphabet, psDotFile);
        psDotFile.close();
        Runtime.getRuntime().exec("dot -Tpdf -O " + filename);
    }

    public void learn() throws IOException {
        System.out.println("learn()");

        LearnLogger log = LearnLogger.getLogger(Learner.class.getSimpleName());

        log.log(Level.INFO, "Using learning algorithm " + learningAlgorithm.getClass().getSimpleName());
        log.log(Level.INFO, "Using equivalence algorithm " + equivalenceAlgorithm.getClass().getSimpleName());
        log.log(Level.INFO, "Starting learning");

        SimpleProfiler.start("Total time");
        boolean learning = true;
        Counter round = new Counter("Rounds", "");
        round.increment();
        log.logPhase("Starting round " + round.getCount());
        SimpleProfiler.start("Learning");
        learningAlgorithm.startLearning();
        SimpleProfiler.stop("Learning");
        // 获得假设模型
        MealyMachine<?, String, ?, String> hypothesis = learningAlgorithm.getHypothesisModel();

        while(learning) {
            // 输出当前结果
            writeDotModel(hypothesis, alphabet, config.outputDir + "/hypothesis_" + round.getCount() + ".dot");

            // 利用等价查询寻找反例
            SimpleProfiler.start("Searching for counter-example");
            DefaultQuery<String, Word<String>> counterExample = equivalenceAlgorithm.findCounterExample(hypothesis, alphabet);
            SimpleProfiler.stop("Searching for counter-example");

            if(counterExample == null) {
                // 未找到反例，说明模型等价结束学习接受模型
                learning = false;

                // 输出最终结果
                writeDotModel(hypothesis, alphabet, config.outputDir + "/learnedModel.dot");
            }
            else {
                // 存在反例，进行下一轮 Membership 查询
                log.logCounterexample("Counter-example found: " + counterExample);
                round.increment();
                log.logPhase("Starting round " + round.getCount());

                SimpleProfiler.start("Learning");
                learningAlgorithm.refineHypothesis(counterExample);
                SimpleProfiler.stop("Learning");

                hypothesis = learningAlgorithm.getHypothesisModel();
            }
        }

        SimpleProfiler.stop("Total time");

        // 输出最终结果
        log.log(Level.INFO, "-------------------------------------------------------");
        log.log(Level.INFO, SimpleProfiler.getResults());
        log.log(Level.INFO, round.getSummary());
        log.log(Level.INFO, statsMemOracle.getStatisticalData().getSummary());
        log.log(Level.INFO, statsCachedMemOracle.getStatisticalData().getSummary());
        log.log(Level.INFO, statsEqOracle.getStatisticalData().getSummary());
        log.log(Level.INFO, statsCachedEqOracle.getStatisticalData().getSummary());
        log.log(Level.INFO, "States in final hypothesis: " + hypothesis.size());
    }
}

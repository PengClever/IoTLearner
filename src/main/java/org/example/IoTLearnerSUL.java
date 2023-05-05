package org.example;

import de.learnlib.api.SUL;
import net.automatalib.words.impl.SimpleAlphabet;

import java.util.Arrays;

public class IoTLearnerSUL implements SUL<String, String> {
    SimpleAlphabet<String> alphabet;
    Communication server;
    public IoTLearnerSUL(LearnerConfig config) throws Exception {
//        System.out.println("IoTLearnerSUL(LearnerConfig config)");
        alphabet = new SimpleAlphabet<>(Arrays.asList(config.alphabet.split(" ")));
        server = new Communication(config);
//        System.out.println("decryptSymbol: " + server.decryptSymbol("4Deny"));
    }
    public SimpleAlphabet<String> getAlphabet() {
//        System.out.println("getAlphabet()");

        return alphabet;
    }
    // 重写SUL接口函数，推动系统向下运行
    @Override
    public String step(String symbol) {
        String result = null;
        try {
            result = server.processSymbol(symbol);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    // 重写SUL接口函数，初始化目标系统
    @Override
    public void pre() {
        try {
            server.reset();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    // 重写SUL接口函数，结束目标系统
    @Override
    public void post() {
    }
}

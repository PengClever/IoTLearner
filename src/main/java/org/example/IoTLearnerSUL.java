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
    // ��дSUL�ӿں������ƶ�ϵͳ��������
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
    // ��дSUL�ӿں�������ʼ��Ŀ��ϵͳ
    @Override
    public void pre() {
        try {
            server.reset();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    // ��дSUL�ӿں���������Ŀ��ϵͳ
    @Override
    public void post() {
    }
}

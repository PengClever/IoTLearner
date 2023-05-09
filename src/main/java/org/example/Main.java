package org.example;

public class Main {
    static String filename = "src/main/resources/config/server.properties";

    public static void main(String[] args) throws Exception {
        System.out.println("Hello IoTLearner!");
//        System.out.println(filename.charAt(0));
//        System.out.println((int)filename.charAt(0));
        // 配置参数
        LearnerConfig config = new LearnerConfig(filename);
        // 创建学习者
        Learner learner = new Learner(config);
        // 学习状态机
        learner.learn();
    }
}
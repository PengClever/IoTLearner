package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class LearnerConfig {
    protected Properties properties;
    String outputDir;
    String alphabet;
    public String learningAlgorithm;
    public String equivalenceAlgorithm;
    int maxDepth = 10;
    int minLength = 5;
    int maxLength = 10;
    int nrQueries = 100;
    int seed = 1;
    String host;
    int port;
    public LearnerConfig(String filename) throws IOException {
//        System.out.println("LearnerConfig(String filename)");
//        System.out.println("filename: " + filename);
        // 声明 Properties 实例化对象
        properties = new Properties();
        // 利用传递的文件名参数创建输入流
        InputStream input = Files.newInputStream(Paths.get(filename));
        // 加载流对应的文件
        properties.load(input);
        // 加载配置文件
        loadProperties();
    }
    private void loadProperties() {
//        System.out.println("loadProperties()");

        if(properties.getProperty("outputDir") != null)
            outputDir = properties.getProperty("outputDir");

        if(properties.getProperty("alphabet") != null)
            alphabet = properties.getProperty("alphabet");

        if(properties.getProperty("learningAlgorithm").equalsIgnoreCase("lstar") || properties.getProperty("learningAlgorithm").equalsIgnoreCase("dhc") || properties.getProperty("learningAlgorithm").equalsIgnoreCase("kv") || properties.getProperty("learningAlgorithm").equalsIgnoreCase("ttt") || properties.getProperty("learningAlgorithm").equalsIgnoreCase("mp") || properties.getProperty("learningAlgorithm").equalsIgnoreCase("rs"))
            learningAlgorithm = properties.getProperty("learningAlgorithm").toLowerCase();

        if(properties.getProperty("equivalenceAlgorithm").equalsIgnoreCase("wmethod") || properties.getProperty("equivalenceAlgorithm").equalsIgnoreCase("modifiedwmethod") || properties.getProperty("equivalenceAlgorithm").equalsIgnoreCase("wpmethod") || properties.getProperty("equivalenceAlgorithm").equalsIgnoreCase("randomwords"))
            equivalenceAlgorithm = properties.getProperty("equivalenceAlgorithm").toLowerCase();

        if(properties.getProperty("maxDepth") != null)
            maxDepth = Integer.parseInt(properties.getProperty("maxDepth"));

        if(properties.getProperty("minLength") != null)
            minLength = Integer.parseInt(properties.getProperty("minLength"));

        if(properties.getProperty("maxLength") != null)
            maxLength = Integer.parseInt(properties.getProperty("maxLength"));

        if(properties.getProperty("nrQueries") != null)
            nrQueries = Integer.parseInt(properties.getProperty("nrQueries"));

        if(properties.getProperty("seed") != null)
            seed = Integer.parseInt(properties.getProperty("seed"));

        host = properties.getProperty("host");
        port = Integer.parseInt(properties.getProperty("port"));
        System.out.println("host: " + host);
        System.out.println("port: " + port);
    }
}

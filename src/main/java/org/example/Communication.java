package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.Vector;

public class Communication {
    Socket socket;
    OutputStream output;
    InputStream input;
    byte[] bytes;
    Vector<String> queries;
    Vector<String> queriesReply;
    Vector<String> lastQueries;
    Vector<String> lastQueriesReply;
    DebugLog log;
    boolean useCache;
    boolean useRule;
    Vector<String> deviceStack;
    Vector<String> userStack;
    public Communication(LearnerConfig config) throws Exception {
        // Test CS communication
//        System.out.println("symbol: " + "ADU1CWRD88:97:46:2C:9A:CE");
//        System.out.println("encryptSymbol: " + encryptSymbol("ADU1CWRD88:97:46:2C:9A:CE"));
//        System.out.println("decryptSymbol: " + decryptSymbol("5ERROR0", false));
//        queries = new Vector<>(100);
//        queries.add("ADU1CWRD88:97:46:2C:9A:CE");
//        queries.add("DCU1D88:97:46:2C:9A:CEWLON");
//        System.out.println("Queries: " + queries);
//        System.out.println("Queries: " + queries.get(0));
//        System.out.println("Queries: " + queries.size());
//        analysisSymbol(queries.get(0));
        try {
            socket = new Socket(config.host, config.port);
        } catch (Exception e) {
            System.out.println("请先启动服务器");
        }
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(0);
        output = socket.getOutputStream();
        input = socket.getInputStream();
        log = new DebugLog();
        useCache = config.useCache;
        if (useCache) {
            lastQueries = new Vector<>(100);
            lastQueriesReply = new Vector<>(100);
        }
        useRule = config.useRule;
        if (useRule) {
            deviceStack = new Vector<>(5);
            userStack = new Vector<>(10);
        }
        // Test CS communication
//        decryptSymbol(receiveSymbol(encryptSymbol("ADU1CWR")));
//        receiveSymbol("111");
//        Scanner scan = new Scanner(System.in);
//        String symbol = "";
//        System.out.println("Test：" + symbol);
//        reset();
//        while (true){
//            System.out.println("请输入数据：");
//            if (scan.hasNext()) {
//                symbol = scan.next();
//                System.out.println("输入的数据为：" + symbol);
//            }
//            processSymbol(symbol);
//        }
    }

    public String receiveSymbol(String symbol) throws Exception {
        output.write(symbol.getBytes());
        bytes = new byte[1024];
        int len = input.read(bytes);
        System.out.println("len：" + len);
        symbol = new String(bytes, 0, len);
        System.out.println("Frida server(encrypted): " + symbol);
        return symbol;
    }

    public void analysisSymbol(String symbol) {
        Alphabet alphabet = new Alphabet(symbol);
        System.out.println(symbol);
        System.out.println("{OpName: " + alphabet.OpName +
                ", Source: " + alphabet.Source +
                ", Dest: " + alphabet.Dest +
                ", Channel: " + alphabet.Channel +
                ", Message: " + alphabet.Message + "}");
    }

    public String encryptSymbol(String symbol) {
        switch (symbol) {
            case "a":
                return "a";
            case "b":
                return "b";
            case "c":
                return "c";
            case "d":
                return "d";
            case "e":
                return "e";
            case "f":
                return "f";
            case "g":
                return "g";
            case "h":
                return "h";
            default:
                Alphabet alphabet = new Alphabet(symbol);
                return alphabet.encryptSymbol;
        }
    }

    public String decryptSymbol(String symbol, boolean isReset) throws IOException {
        if (symbol == null)
            return null;
        if (symbol.length() == 1) {
            return symbol;
        }
        int count = 0, num, total = 0;
        num = (int)symbol.charAt(count) - 48;
        while (num >= 0 && num <= 9) {
            total *= 10;
            total += num;
            count++;
            num = (int)symbol.charAt(count) - 48;
        }
        String decryptSymbol = symbol.substring(count, count + total);
        count += total; total = 0;
        num = (int)symbol.charAt(count) - 48;
        if (num != 0) {
            while (num >= 0 && num <= 9) {
                total *= 10;
                total += num;
                count++;
                num = (int) symbol.charAt(count) - 48;
            }
            if (isReset) {
                if (!symbol.substring(count, count + total).equals("SUC"))
                    output.write(encryptSymbol("FINISH").getBytes());
            }
            decryptSymbol += symbol.substring(count, count + total);
        }
        System.out.println("Frida server(decrypted): " + decryptSymbol);
        return decryptSymbol;
    }

    public void processStack(String outSymbol, String inSymbol) {
        if (Objects.equals(inSymbol, "Forbidden"))
            return;
        Alphabet alphabet = new Alphabet(outSymbol);
        switch (alphabet.OpName) {
            case "AD":
                deviceStack.add(alphabet.Message);
                break;
            case "SA":
                userStack.add(alphabet.Message);
                break;
            case "DD":
                deviceStack.remove(alphabet.Message);
                break;
            case "IR":
                userStack.removeElementAt(0);
                break;
            default:
                break;
        }
    }

    public String processSymbol(String symbol) throws Exception {
        System.out.println("processSymbol");
        if(symbol != null){
            // 本地简单配置测试
            if (symbol.equals("a"))
                return "z";
            if (symbol.equals("b"))
                return "x";
            if (symbol.equals("c"))
                return "c";
            if (symbol.equals("d"))
                return "v";
            if (symbol.equals("e"))
                return "b";
            if (symbol.equals("f"))
                return "n";
            if (symbol.equals("g"))
                return "m";
            if (symbol.equals("h"))
                return "a";
            // 输出此前已回复的响应
            System.out.println("Prefix list: ");
            if (queries.size() == 0)
                System.out.println("empty");
            for (String query : queries)
                analysisSymbol(query);
            queries.add(symbol);
            // 判断是否使用缓存
            int i = 0;
            if (useCache) {
                for (; i < lastQueries.size() && i < queries.size(); i++) {
                    if (!Objects.equals(lastQueries.get(i), queries.get(i))) {
                        break;
                    }
                }
                if (i == queries.size()) {
                    System.out.println("Cache Reply: " + lastQueriesReply.get(i - 1));
                    queriesReply.add(lastQueriesReply.get(i - 1));
                    log.addLog(lastQueries.get(i - 1), lastQueriesReply.get(i - 1), 1);
                    if (useRule)
                        processStack(lastQueries.get(i - 1), lastQueriesReply.get(i - 1));
                    return lastQueriesReply.get(i - 1);
                }
            }
            // 判断是否使用简单规则
            String outSymol = symbol;
            String inSymbol = null;
            if (useRule) {
                Alphabet alphabet = new Alphabet(outSymol);
                if ((Objects.equals(alphabet.OpName, "DC") || Objects.equals(alphabet.OpName, "DD"))
                        && deviceStack.isEmpty())
                    inSymbol = "Forbidden";
                if (Objects.equals(alphabet.OpName, "IR") && userStack.isEmpty())
                    inSymbol = "Forbidden";
            }
            // 不使用缓存和简单规则处理字符串，将字母表中的表达式转换为CS通信中要求的格式
            if (inSymbol == null) {
                symbol = encryptSymbol(symbol);
                // 发送消息，并等待回复
                symbol = receiveSymbol(symbol);
                // 处理CS消息，转换为字母表形式
                inSymbol = decryptSymbol(symbol, false);
                log.addLog(outSymol, inSymbol, 0);
            } else
                log.addLog(outSymol, inSymbol, 2);
            queriesReply.add(inSymbol);
            if (useCache) {
                if (lastQueries.size() < queries.size() || i < lastQueries.size() - 1) {
                    lastQueries = new Vector<>(100);
                    lastQueriesReply = new Vector<>(100);
                    for (int j = 0; j < queries.size(); j++) {
                        lastQueries.add(queries.get(j));
                        lastQueriesReply.add(queriesReply.get(j));
                    }
                }
            }
            if (useRule)
                processStack(outSymol, inSymbol);
            return inSymbol;
        }
        else {
            return "null";
        }
    }

    public void reset() throws Exception {
        decryptSymbol(receiveSymbol(encryptSymbol("RESET")), true);
        log.addResetCount();
        queries = new Vector<>(100);
        queriesReply = new Vector<>(100);
        if (useRule) {
            deviceStack = new Vector<>(5);
            userStack = new Vector<>(10);
        }
    }
}

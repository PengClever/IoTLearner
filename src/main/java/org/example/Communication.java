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
        lastQueries = new Vector<>(100);
        lastQueriesReply = new Vector<>(100);
        // Test CS communication
//        decryptSymbol(receiveSymbol(encryptSymbol("ADU1CWR")));
//        receiveSymbol("111");
//        while (true){}
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
        String OpName, Source, Dest, Channel, Message;
        switch (symbol) {
            case "ADU1CWRD88:97:46:2C:9A:CE":
                OpName = "AD";
                Source = "U1";
                Dest = "C";
                Channel = "WR";
                Message = "D88:97:46:2C:9A:CE";
                break;
            case "SAU1CWRU2":
                OpName = "SA";
                Source = "U1";
                Dest = "C";
                Channel = "WR";
                Message = "U2";
                break;
            case "USU1CWRU2":
                OpName = "US";
                Source = "U1";
                Dest = "C";
                Channel = "WR";
                Message = "U2";
                break;
            case "DCU1CWRON":
                OpName = "DC";
                Source = "U1";
                Dest = "C";
                Channel = "WR";
                Message = "ON";
                break;
            case "DCU1CWROF":
                OpName = "DC";
                Source = "U1";
                Dest = "C";
                Channel = "WR";
                Message = "OF";
                break;
            case "DCU2CWRON":
                OpName = "DC";
                Source = "U2";
                Dest = "C";
                Channel = "WR";
                Message = "ON";
                break;
            case "DCU2CWROF":
                OpName = "DC";
                Source = "U2";
                Dest = "C";
                Channel = "WR";
                Message = "OF";
                break;
            case "DCU1D88:97:46:2C:9A:CEWLON":
                OpName = "DC";
                Source = "U1";
                Dest = "D88:97:46:2C:9A:CE";
                Channel = "WL";
                Message = "ON";
                break;
            case "DCU1D88:97:46:2C:9A:CEWLOF":
                OpName = "DC";
                Source = "U1";
                Dest = "D88:97:46:2C:9A:CE";
                Channel = "WL";
                Message = "OF";
                break;
            case "DCU2D88:97:46:2C:9A:CEWLON":
                OpName = "DC";
                Source = "U2";
                Dest = "D88:97:46:2C:9A:CE";
                Channel = "WL";
                Message = "ON";
                break;
            case "DCU2D88:97:46:2C:9A:CEWLOF":
                OpName = "DC";
                Source = "U2";
                Dest = "D88:97:46:2C:9A:CE";
                Channel = "WL";
                Message = "OF";
                break;
            case "DDU1CWRD88:97:46:2C:9A:CE":
                OpName = "DD";
                Source = "U1";
                Dest = "C";
                Channel = "WR";
                Message = "D88:97:46:2C:9A:CE";
                break;
            case "IRU2CWRAC":
                OpName = "IR";
                Source = "U2";
                Dest = "C";
                Channel = "WR";
                Message = "AC";
                break;
            case "IRU2CWRDE":
                OpName = "IR";
                Source = "U2";
                Dest = "C";
                Channel = "WR";
                Message = "DE";
                break;
            default:
                OpName = "";
                Source = "";
                Dest = "";
                Channel = "";
                Message = "";
        }
        System.out.println(symbol);
        System.out.println("{OpName: " + OpName +
                ", Source: " + Source +
                ", Dest: " + Dest +
                ", Channel: " + Channel +
                ", Message: " + Message + "}");
    }

    public String encryptSymbol(String symbol) {
        switch (symbol) {
            case "a":
                return "a";
            case "b":
                return "b";
            case "c":
                return "c";
            case "ADU1CWR":
                return "2AD2U11C2WR0";
            case "ADU1CWRD88:97:46:2C:9A:CE":
                return "2AD2U11C2WR18D88:97:46:2C:9A:CE";
            case "DDU1CWR":
                return "2DD2U11C2WR0";
            case "DDU1CWRD88:97:46:2C:9A:CE":
                return "2DD2U11C2WR18D88:97:46:2C:9A:CE";
            case "DDU2CWRD88:97:46:2C:9A:CE":
                return "2DD2U21C2WR18D88:97:46:2C:9A:CE";
            case "SQU1CWRU2":
                return "2SQ2U11C2WR2U2";
            case "SAU1CWRU2":
                return "2SA2U11C2WR2U2";
            case "USU1CWRU2":
                return "2US2U11C2WR2U2";
            case "QHU2CWR":
                return "2QH2U21C2WR0";
            case "DCU1CWRON":
                return "2DC2U11C2WR2ON";
            case "DCU1CWROF":
                return "2DC2U11C2WR2OF";
            case "DCU1DWRON":
                return "2DC2U11D2WR2ON";
            case "DCU1DWROF":
                return "2DC2U11D2WR2OF";
            case "DCU1DWLON":
                return "2DC2U11D2WL2ON";
            case "DCU1DWLOF":
                return "2DC2U11D2WL2OF";
            case "DCU1D88:97:46:2C:9A:CEWLON":
                return "2DC2U118D88:97:46:2C:9A:CE2WL2ON";
            case "DCU1D88:97:46:2C:9A:CEWLOF":
                return "2DC2U118D88:97:46:2C:9A:CE2WL2OF";
            case "DCU1DZBON":
                return "2DC2U11D2ZB2ON";
            case "DCU1DZBOF":
                return "2DC2U11D2ZB2OF";
            case "DCU2CWRON":
                return "2DC2U21C2WR2ON";
            case "DCU2CWROF":
                return "2DC2U21C2WR2OF";
            case "DCU2DWRON":
                return "2DC2U21D2WR2ON";
            case "DCU2DWROF":
                return "2DC2U21D2WR2OF";
            case "DCU2DWLON":
                return "2DC2U21D2WL2ON";
            case "DCU2DWLOF":
                return "2DC2U21D2WL2OF";
            case "DCU2D88:97:46:2C:9A:CEWLON":
                return "2DC2U218D88:97:46:2C:9A:CE2WL2ON";
            case "DCU2D88:97:46:2C:9A:CEWLOF":
                return "2DC2U218D88:97:46:2C:9A:CE2WL2OF";
            case "DCU2DZBON":
                return "2DC2U21D2ZB2ON";
            case "DCU2DZBOF":
                return "2DC2U21D2ZB2OF";
            case "ACU2CWR":
                return "2AC2U21C2WR0";
            case "DEU2CWR":
                return "2DE2U21C2WR0";
            case "IRU2CWRAC":
                return "2IR2U21C2WR2AC";
            case "IRU2CWRDE":
                return "2IR2U21C2WR2DE";
            case "RESET":
                return "5RESET0000";
            case "FINISH":
                return "6FINISH0000";
            default:
                return "Wrong_symbol";
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

    public String processSymbol(String symbol) throws Exception {
        if(symbol != null){
            if (symbol.equals("a"))
                return "z";
            if (symbol.equals("b"))
                return "x";
            if (symbol.equals("c"))
                return "c";
            // 输出此前已回复的响应
            System.out.println("Prefix list: ");
            if (queries.size() == 0)
                System.out.println("empty");
            for (String query : queries)
                analysisSymbol(query);
            queries.add(symbol);
            // 判断是否使用缓存
            int i;
            for (i = 0; i < lastQueries.size() && i < queries.size(); i++) {
                if (!Objects.equals(lastQueries.get(i), queries.get(i))) {
                    break;
                }
            }
            if (i == queries.size()) {
                System.out.println("Cache Reply: " + lastQueriesReply.get(i - 1));
                queriesReply.add(lastQueriesReply.get(i - 1));
                return lastQueriesReply.get(i - 1);
            }
            // 不使用缓存时处理字符串，将字母表中的表达式转换为CS通信中要求的格式
            symbol = encryptSymbol(symbol);
            // 发送消息，并等待回复
            symbol = receiveSymbol(symbol);
            // 处理CS消息，转换为字母表形式
            symbol = decryptSymbol(symbol, false);
            queriesReply.add(symbol);
            if (lastQueries.size() < queries.size() || i < lastQueries.size() - 1) {
                lastQueries = new Vector<>(100);
                lastQueriesReply = new Vector<>(100);
                for (int j = 0; j < queries.size(); j++) {
                    lastQueries.add(queries.get(j));
                    lastQueriesReply.add(queriesReply.get(j));
                }
            }
            return symbol;
        }
        else {
            return "null";
        }
    }

    public void reset() throws Exception {
        decryptSymbol(receiveSymbol(encryptSymbol("RESET")), true);
        queries = new Vector<>(100);
        queriesReply = new Vector<>(100);
    }
}

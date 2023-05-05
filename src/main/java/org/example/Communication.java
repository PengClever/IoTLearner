package org.example;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Communication {
    Socket socket;
    OutputStream output;
    InputStream input;
    byte[] bytes;
    public Communication(LearnerConfig config) throws Exception {
        try {
            socket = new Socket(config.host, config.port);
        } catch (Exception e) {
            System.out.println("请先启动服务器");
        }
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(0);
        output = socket.getOutputStream();
        input = socket.getInputStream();
        // Test CS communication
        decryptSymbol(receiveSymbol("2AD2U11C0"));
        while (true){}
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

    public String encryptSymbol(String symbol) {
        switch (symbol) {
            case "ADU1C":
                return "2AD2U11C0";
            case "DDU1C":
                return "2DD2U11C0";
            case "DDU2C":
                return "2DD2U21C0";
            case "SQU1CU2":
                return "2SQ2U11C2U2";
            case "SAU1CU2":
                return "2SA2U11C2U2";
            case "USU1CU2":
                return "2US2U11C2U2";
            case "QHU2C":
                return "2QH2U21C0";
            case "CLONU1D":
                return "4CLON2U11D0";
            case "CLOFU1D":
                return "4CLOF2U11D0";
            case "CLONU2D":
                return "4CLON2U21D0";
            case "CLOFU2D":
                return "4CLOF2U21D0";
            case "CRU1D":
                return "2CR2U11D0";
            case "CRU2D":
                return "2CR2U21D0";
            case "ACU2C":
                return "2AC2U21C0";
            case "DEU2C":
                return "2DE2U21C0";
            default:
                return "Wrong_symbol";
        }
    }

    public String decryptSymbol(String symbol) {
        if (symbol == null)
            return null;
        int count = 0, num, total = 0;
        num = (int)symbol.charAt(count) - 48;
        while (num >= 0 && num <= 9) {
            total *= 10;
            total += num;
            count++;
            num = (int)symbol.charAt(count) - 48;
        }
        String decryptSymbol = symbol.substring(count, count + total);
        System.out.println("Frida server(decrypted): " + decryptSymbol);
        return decryptSymbol;
    }

    public String processSymbol(String symbol) throws Exception {
        if(symbol != null){
            // 处理字符串，将字母表中的表达式转换为CS通信中要求的格式
            symbol = encryptSymbol(symbol);
            // 发送消息，并等待回复
            symbol = receiveSymbol(symbol);
            // 处理CS消息，转换为字母表形式
            return decryptSymbol(symbol);
        }
        else {
            return "null";
        }
    }

    public void reset() throws Exception {
        output.write("5RESET000".getBytes());
    }
}

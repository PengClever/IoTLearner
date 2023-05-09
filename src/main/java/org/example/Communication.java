package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Communication {
    Socket socket;
    OutputStream output;
    InputStream input;
    byte[] bytes;
    public Communication(LearnerConfig config) throws Exception {
        // Test CS communication
//        System.out.println("symbol: " + "ADU1CWRD88:97:46:2C:9A:CE");
//        System.out.println("encryptSymbol: " + encryptSymbol("ADU1CWRD88:97:46:2C:9A:CE"));
//        System.out.println("decryptSymbol: " + decryptSymbol("5ERROR0"));
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
//        decryptSymbol(receiveSymbol(encryptSymbol("ADU1CWR")));
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

    public String encryptSymbol(String symbol) {
        switch (symbol) {
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
            // 处理字符串，将字母表中的表达式转换为CS通信中要求的格式
            symbol = encryptSymbol(symbol);
            // 发送消息，并等待回复
            symbol = receiveSymbol(symbol);
            // 处理CS消息，转换为字母表形式
            return decryptSymbol(symbol, false);
        }
        else {
            return "null";
        }
    }

    public void reset() throws Exception {
        decryptSymbol(receiveSymbol(encryptSymbol("RESET")), true);
    }
}

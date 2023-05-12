package org.example;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

public class DebugLog {
    static String filename = "result/output/DebugLog.txt";
    File file;
    FileOutputStream fos;
    public DebugLog() throws Exception {
        file = new File(filename);
        if(file.exists()) {
            try {
                file.delete();
            } catch (Exception e) {
                System.out.println("创建调试日志文件失败");
            }
        }
        file.createNewFile();
        fos = new FileOutputStream(file, true);
    }

    public void addLog(String symbol, long count) throws Exception {
        if (Objects.equals(symbol, "Reset")){
            fos.write(("[" + count + "] " + "\n").getBytes());
        }
    }

    public void addLog(String outSymbol, String inSymbol, int mode) throws Exception {
        switch (mode) {
            case 1:
                fos.write(("[CS] " + outSymbol + " -> " + inSymbol + "\n").getBytes());
                break;
            case 2:
                fos.write(("[Cache] " + outSymbol + " -> " + inSymbol + "\n").getBytes());
        }
    }
}

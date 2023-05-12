package org.example;

import java.io.File;
import java.io.FileOutputStream;

public class DebugLog {
    static String filename = "result/output/DebugLog.txt";
    File file;
    FileOutputStream fos;
    long resetCount;

    public DebugLog() throws Exception {
        resetCount = -1;
        file = new File(filename);
        if(file.exists())
            if (!file.delete())
                System.out.println("日志文件初始化失败：删除失败");
        if (file.createNewFile())
            fos = new FileOutputStream(file, true);
        else
            System.out.println("日志文件初始化失败：创建失败");
    }

    public void addResetCount() {
        resetCount++;
    }

    public void addLog(String outSymbol, String inSymbol, int mode) throws Exception {
        switch (mode) {
            case 0:
                fos.write(("[" + resetCount + "][CS] " + outSymbol + " -> " + inSymbol + "\n").getBytes());
                break;
            case 1:
                fos.write(("[" + resetCount + "][Cache] " + outSymbol + " -> " + inSymbol + "\n").getBytes());
                break;
            default:
                System.out.println("日志添加失败");
        }
    }
}

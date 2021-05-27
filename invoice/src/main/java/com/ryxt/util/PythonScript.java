package com.ryxt.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;

@Slf4j
public class PythonScript {

    /**
     * 执行sh脚本
     */
    public static void executeRestart(String command){
        Process process = null;
        BufferedReader bufrIn = null;
//        StringBuilder result = new StringBuilder();
        BufferedReader bufrError = null;
//        String[] cmd = new String[] { "/bin/sh", "-c", command };
//        String[] cmd = new String[] { "cmd", "/C", command };
        String[] cmd = new String[] { "cmd", "/K start ", command };
        try {
            process = Runtime.getRuntime().exec(cmd); // 执行命令 返回一个子进程对象
//            process.waitFor(); // 方法阻塞 等待命令执行完成

            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
//            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
//            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
//
//            // 读取输出
//            String line = null;
//            while ((line = bufrIn.readLine()) != null) {
//                result.append(line).append('\n');
//            }
//            while ((line = bufrError.readLine()) != null) {
//                result.append(line).append('\n');
//            }
//            System.out.println(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        finally {
//            closeStream(bufrIn);
//            closeStream(bufrError);
//
//            // 销毁子进程
//            if (process != null) {
//                process.destroy();
//            }
//        }
    }
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String command = "python C:\\workspace\\fpyzm\\nbhh.py --fplx 04 --key d585a6b8b1464af6ab750fa7226bcdc0 --fpdm 03\n" +
                "1001900104 --fphm 72332307 --kprq 20200518 --kjje 18811.88 --jym 586261";
        System.out.println(command);
        executeRestart(command);
    }

}

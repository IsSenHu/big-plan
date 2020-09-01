package com.gapache.ngrok.server.core;

import java.io.*;

/**
 * @author HuSen
 * @since 2020/8/14 10:24 上午
 */
public class SSHExecUtil {

    public static String getErrorMsg(String filename) {
        StringBuilder sb = new StringBuilder("");
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filename));
            String line;
            // Process the data, here we just print it out
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return sb.toString();
    }
}

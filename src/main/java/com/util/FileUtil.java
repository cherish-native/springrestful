package com.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 文件工具类
 */
public class FileUtil {

    /**
     * 读取文件
     * @param path
     * @return
     */
    public static byte[] readFile(String path) throws Exception{
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(path);
            out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}


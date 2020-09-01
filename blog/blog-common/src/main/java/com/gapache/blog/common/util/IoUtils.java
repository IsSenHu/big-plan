package com.gapache.blog.common.util;

import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author HuSen
 * create on 2020/4/5 20:44
 */
public class IoUtils {

    public static byte[] getContent(InputStream inputStream) {
        try {
            return FileCopyUtils.copyToByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

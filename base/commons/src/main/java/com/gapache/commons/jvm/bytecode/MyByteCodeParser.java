package com.gapache.commons.jvm.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.function.Consumer;

/**
 * 我的字节码解析工具
 *
 * @author HuSen
 * create on 2020/3/24 6:06 下午
 */
public class MyByteCodeParser {
    private String filePath;
    private int point;

    public MyByteCodeParser(String filePath) {
        this.filePath = filePath;
    }

    public void parse() {
        byte[] data;
        try (InputStream is = new FileInputStream(new File(this.filePath)); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            int ch;
            while (-1 != (ch = is.read())) {
                byteArrayOutputStream.write(ch);
            }
            data = byteArrayOutputStream.toByteArray();
            String content = new BigInteger(1, data).toString(16);
            System.out.println(content);

            String magicNumber = content.substring(point, point + 8);
            System.out.println("魔数: " + magicNumber);
            point += 8;

            String minorVersion = content.substring(point, point + 4);
            System.out.println("次版本号: " + hexToInt(minorVersion));
            point += 4;

            String majorVersion = content.substring(point, point + 4);
            System.out.println("主版本号: " + hexToInt(majorVersion));
            point += 4;

            String constantPoolCountHex = content.substring(point, point + 4);
            int constantPoolCount = hexToInt(constantPoolCountHex);
            System.out.println("常量池数量: " + constantPoolCount);
            point += 4;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int printPoint(int count, int point, Consumer<String> hexConsumer) {
        hexConsumer.accept();
    }

    private int hexToInt(String hex) {
        return new BigInteger(hex, 16).intValue();
    }

    public static void main(String[] args) {
        new MyByteCodeParser("/Users/macos/Documents/codes/mine/big-plan/base/commons/target/classes/com/gapache/commons/jvm/bytecode/MyTest1.class").parse();
    }
}

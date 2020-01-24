package com.gapache.commons.jvm.classloader;

/**
 * @author HuSen
 * create on 2020/1/24 16:51
 */
public class MyTest13 {

    public static void main(String[] args) {
        System.out.println("123".getClass().getClassLoader());
        System.out.println(MyTest13.class.getClassLoader());
        System.out.println(Thread.currentThread().getContextClassLoader());
    }
}

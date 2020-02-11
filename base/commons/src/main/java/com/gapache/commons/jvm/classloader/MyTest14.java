package com.gapache.commons.jvm.classloader;

import java.io.*;

/**
 * @author HuSen
 * create on 2020/1/25 13:15
 */
public class MyTest14 extends ClassLoader {

    private String classDir;
    private String classLoaderName;
    private static final String fileExtension = ".class";

    public MyTest14(ClassLoader parent, String classDir, String classLoaderName) {
        super(parent);
        this.classDir = classDir;
        this.classLoaderName = classLoaderName;
    }

    public MyTest14(String classDir, String classLoaderName) {
        this.classDir = classDir;
        this.classLoaderName = classLoaderName;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = this.loadClassData(name);
        if (data == null) {
            throw new ClassNotFoundException(name + " is not found");
        }
        return defineClass(name, data, 0, data.length);
    }

    private byte[] loadClassData(String name) {
        name = name.replace(".", File.separator);
        byte[] data = null;
        try (InputStream is = new FileInputStream(new File(this.classDir.concat(name).concat(fileExtension))); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int ch;
            while (-1 != (ch = is.read())) {
                baos.write(ch);
            }
            data = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public String toString() {
        return "MyTest14{" +
                "classLoaderName='" + classLoaderName + '\'' +
                '}';
    }

    public static void main(String[] args) throws Exception {
        MyTest14 loader1 = new MyTest14("F:\\", "loader1");
        // 加载ClassPath下的类
        test(loader1, "com.gapache.commons.jvm.classloader.MyTest1");
        // class: 325040804
        // classLoader: sun.misc.Launcher$AppClassLoader@18b4aac2
        // newInstance: com.gapache.commons.jvm.classloader.MyTest1@45ee12a7
        System.out.println("------------------");
        // 加载指定classDir目录下的类
        test(loader1, "com.example.clear.TestLoaderIt");
        // class: 1265094477
        // classLoader: MyTest14{classLoaderName='loader1'}
        // newInstance: com.example.clear.TestLoaderIt@7ea987ac
        System.out.println("------------------");
        // 再次使用不同的加载器加载同一个类
        MyTest14 loader2 = new MyTest14("F:\\", "loader2");
        test(loader2, "com.gapache.commons.jvm.classloader.MyTest1");
        // class: 1265094477
        // classLoader: MyTest14{classLoaderName='loader1'}
        // newInstance: com.example.clear.TestLoaderIt@7ea987ac
        System.out.println("------------------");
        // 再次使用不同的加载器加载classDir目录下的类
        test(loader2, "com.example.clear.TestLoaderIt");
        // class: 1846274136
        // classLoader: MyTest14{classLoaderName='loader2'}
        // newInstance: com.example.clear.TestLoaderIt@61bbe9ba
        System.out.println("-------------------");
        // 使用loader1作为loader3的父加载器，加载claasDir目录下的类
        MyTest14 loader3 = new MyTest14(loader1, "F:\\", "loader3");
        test(loader3, "com.example.clear.TestLoaderIt");
        // class: 1265094477
        // classLoader: MyTest14{classLoaderName='loader1'}
        // newInstance: com.example.clear.TestLoaderIt@610455d6
    }

    public static void test(ClassLoader classLoader, String className) throws Exception {
        Class<?> aClass = classLoader.loadClass(className);
        System.out.println("class: " + aClass.hashCode());
        System.out.println("classLoader: " + aClass.getClassLoader());
        System.out.println("newInstance: " + aClass.newInstance());
    }
}

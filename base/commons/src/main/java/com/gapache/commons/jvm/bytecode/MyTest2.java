package com.gapache.commons.jvm.bytecode;

/**
 * java verbose -p(打印私有信息)
 *
 * moniterenter
 * moniterexit
 *
 * @author HuSen
 * create on 2020/3/23 3:45 下午
 */
public class MyTest2 {

    String str = "Welcome";

    private int x = 5;

    public static Integer in = 10;

    private final Object object = new Object();

    public static void main(String[] args) {
        MyTest2 myTest2 = new MyTest2();

        myTest2.setX(8);

        in = 20;
    }

    private synchronized void setX(int x) {
        this.x = x;
    }

    private void test(String str) {
        synchronized (this.object) {
            System.out.println("hello world");
        }
    }

    private synchronized static void test2() {

    }
}

package com.gapache.commons.jvm.bytecode;

/**
 * @author HuSen
 * create on 2020/3/23 3:45 下午
 */
public class MyTest2 {

    String str = "Welcome";

    private int x = 5;

    public static Integer in = 10;

    public static void main(String[] args) {
        MyTest2 myTest2 = new MyTest2();

        myTest2.setX(8);

        in = 20;
    }

    public void setX(int x) {
        this.x = x;
    }
}

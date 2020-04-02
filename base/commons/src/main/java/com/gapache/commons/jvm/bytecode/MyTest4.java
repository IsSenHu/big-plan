package com.gapache.commons.jvm.bytecode;

/**
 * 栈帧（stack frame）
 * 用于帮助虚拟机执行方法调用与方法执行的数据结构。
 * 封装了方法的局部变量表、动态链接信息、方法的返回地址以及操作数栈等信息。
 * 符号引用，直接引用
 * 有些符号引用是在类加载阶段或是第一次使用时就会转换为直接引用，这种转换叫做静态解析；另外一些符号引用则是在每次运行期
 * 转换为直接引用，这种转换叫做动态链接，这体现为Java的多态性。
 *
 * invokevirtual
 *      Animal a = new Cat();
 *      a.sleep();
 *      a = new Dog();
 *      a.sleep();
 *      a = new Tiger();
 *      a.sleep();
 *
 * @author HuSen
 * create on 2020/4/2 11:35 上午
 */
public class MyTest4 {

}

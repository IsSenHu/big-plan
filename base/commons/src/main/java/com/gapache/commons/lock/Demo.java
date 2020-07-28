package com.gapache.commons.lock;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程有哪些状态？
 * synchronized 是可重入锁吗？是公平锁吗？实现原理？
 * Monitor对象和对象会关联起来。
 * 依赖于操作系统mutex 性能太差 1.6之前（需要调用操作系统）导致中断（状态切换 用户态切换为用户态）
 *
 * 1.6之后性能升级 偏向锁 ->（轻量级锁 底层也是基于cas做的）-> mutex 这里涉及到锁的升级膨胀过程
 * 对象头，实例数据，对齐填充。
 *
 * Mark Word + 元数据指针 数组长度（数组对象才有）
 *
 * jvm启动5s后才会开启偏向锁
 * 调用对象的hashcode方法会造成锁升级
 *
 * 偏向状态 锁状态标志
 *
 * aqs
 *
 * monitorenter
 * monitorexit
 *
 *  cas Unsafe类 compareAndSwap 硬件原语 cmpxchg 硬件原语支持的原子指令（安全的）
 *
 *  jmm
 *  synchronized和lock的区别，说下AQS
 *  Volatile关键字的底层原理
 *  说一下单例场景为什么要使用DCL
 *  CAS的底层原语原理
 *  unsafe有什么弊端
 *  说一下线程池的参数以及销毁过程
 *  线程池的线程数该如何设置
 *
 * @author HuSen
 * @since 2020/7/23 1:46 下午
 */
public class Demo {

    public static void main(String[] args) {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 10, 1000L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000), r -> new Thread(r, "demo"), new ThreadPoolExecutor.CallerRunsPolicy());

        poolExecutor.execute(() -> System.out.println(1));

        poolExecutor.shutdown();
    }
}

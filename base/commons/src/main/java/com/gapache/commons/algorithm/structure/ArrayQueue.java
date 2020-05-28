package com.gapache.commons.algorithm.structure;

/**
 * 数组实现队列
 *
 * @author HuSen
 * @since 2020/5/28 2:36 下午
 */
public class ArrayQueue {
    /** 该数组用于存放数据，模拟队列 */
    private int[] arr;
    /** 表示数组的最大容量 */
    private int maxSize;
    /** 队列头的前一个位置 */
    private int front;
    /** 队列尾 */
    private int rear;

    public ArrayQueue(int maxSize) {
        this.maxSize = maxSize;
        arr = new int[this.maxSize];
        front = -1;
        rear = -1;
    }

    /**
     * 判断队列是否满
     *
     * @return 队列是否满
     */
    private boolean isFull() {
        return rear == maxSize - 1;
    }

    /**
     * 判断队列是否为空
     *
     * @return 队列是否为空
     */
    private boolean isEmpty() {
        return rear == front;
    }

    /**
     * 添加数据到队列
     *
     * @param n 数据
     */
    private void push(int n) {
        if (isFull()) {
            return;
        }
        // Coverage of Spring’s integration with AspectJ is also provided.
        rear++;
        arr[rear] = n;
    }

    /**
     * 获取队列的数据
     *
     * @return 数据
     */
    private int pop() {
        if (isEmpty()) {
            throw new IllegalStateException("arr is empty!!!");
        }
        front++;
        return arr[front];
    }

    /**
     * 显示队列的所有数据
     */
    private void show() {
        if (isEmpty()) {
            System.out.println("arr is empty!!!");
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.printf("arr[%d]=%d\t", i, arr[i]);
        }
        System.out.println();
    }

    /**
     * 显示队列的头数据，注意不是取出数据
     *
     * @return 头数据
     */
    private int peek() {
        if (isEmpty()) {
            throw new IllegalStateException("arr is empty!!!");
        }
        return arr[front + 1];
    }

    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue(10);
        queue.show();

        queue.push(1);
        queue.show();
        queue.push(2);
        queue.show();
        queue.push(3);
        queue.show();

        System.out.println(queue.peek());
        queue.show();

        System.out.println(queue.pop());
        System.out.println(queue.pop());
        System.out.println(queue.pop());
    }
}

package com.gapache.commons.algorithm.structure;

/**
 * 数组实现环形队列
 * 最好理解的一种方式
 * 还有一种取模的方式
 *
 * @author HuSen
 * @since 2020/5/28 2:36 下午
 */
public class ArrayQueue {
    /** 该数组用于存放数据，模拟队列 */
    private int[] arr;
    /** 表示数组的最大容量 */
    private int maxSize;
    /** 下一个取的位置 */
    private int front;
    /** 下一个放的位置 */
    private int rear;
    /** 总大小 */
    private int size;

    public ArrayQueue(int maxSize) {
        this.maxSize = maxSize;
        arr = new int[this.maxSize];
        front = 0;
        rear = 0;
        size = 0;
    }

    /**
     * 判断队列是否满
     *
     * @return 队列是否满
     */
    private boolean isFull() {
        return this.size == maxSize;
    }

    /**
     * 判断队列是否为空
     *
     * @return 队列是否为空
     */
    private boolean isEmpty() {
        return this.size() == 0;
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
        arr[rear] = n;
        rear = (rear + 1) % maxSize;
        size++;
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
        int i = arr[front];
        arr[front] = 0;
        front = (front + 1) % maxSize;
        size--;
        return i;
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
        return arr[front];
    }

    /**
     * 队列中的数据个数
     *
     * @return 个数
     */
    private int size() {
        return this.size;
    }

    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue(3);
        queue.show();
        System.out.println(queue.size());

        queue.push(1);
        queue.show();
        queue.push(2);
        queue.show();
        queue.push(3);
        queue.show();
        System.out.println("size: " + queue.size());

        System.out.println(queue.peek());
        queue.show();
        System.out.println(queue.pop());
        queue.show();
        queue.push(4);
        queue.show();
        System.out.println(queue.pop());
        queue.show();
        System.out.println(queue.pop());
        queue.show();
        System.out.println(queue.pop());
        queue.show();
    }
}

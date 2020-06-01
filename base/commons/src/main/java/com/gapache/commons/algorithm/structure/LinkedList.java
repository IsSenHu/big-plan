package com.gapache.commons.algorithm.structure;

import lombok.Getter;
import lombok.Setter;

/**
 * 单链表，有顺序的
 *
 * @author HuSen
 * @since 2020/5/29 5:13 下午
 */
public class LinkedList {
    /** 标识节点，下一个节点是链表头 */
    private Node sign = new Node(0, null, null);

    public void addByNo(Node node) {
        // 空，直接设置为头节点
        if (isEmpty()) {
            sign.setNext(node);
            return;
        }
        // 获取到头节点
        Node temp = sign.getNext();
        // 比头结点还要小，直接设置为头节点
        if (node.getNo() < temp.getNo()) {
            node.setNext(temp);
            sign.setNext(node);
            return;
        }
        while (temp != null) {
            Node tempNext = temp.getNext();
            // 没有尾节点了，说明找到最后了
            if (tempNext == null) {
                temp.setNext(node);
                break;
            }
            // 找到了
            if (node.getNo() > temp.getNo() && node.getNo() < tempNext.getNo()) {
                // 找到了
                temp.setNext(node);
                node.setNext(tempNext);
                break;
            }
            // 找到了相等的说明已存在，不进行任何操作
            if (node.getNo() == temp.getNo()) {
                break;
            }
            temp = temp.getNext();
        }
    }

    public void update(Node node) {
        if (isEmpty()) {
            System.out.println("节点为空!!!");
            return;
        }
        // 获取到头节点
        Node temp = sign.getNext();
        while (temp != null) {
            if (temp.getNo() == node.getNo()) {
                temp.setName(node.getName());
                temp.setNickName(node.getNickName());
                break;
            }
            temp = temp.getNext();
        }
    }

    public Node remove(int no) {
        if (isEmpty()) {
            System.out.println("节点为空!!!");
            return null;
        }
        // 获取到头节点
        Node temp = sign.getNext();
        // 是头节点
        if (temp.getNo() == no) {
            sign.setNext(temp.getNext());
            return temp;
        }
        while (temp != null) {
            Node next = temp.getNext();
            if (next == null) {
                return null;
            }
            if (next.getNo() == no) {
                temp.setNext(next.getNext());
                next.setNext(null);
                return next;
            }
            temp = temp.getNext();
        }
        return null;
    }

    public void list() {
        Node temp = sign.getNext();
        while (temp != null) {
            System.out.println(temp);
            temp = temp.getNext();
        }
    }

    public boolean isEmpty() {
        return sign.getNext() == null;
    }

    public static void main(String[] args) {
        LinkedList linkedList = new LinkedList();
        linkedList.addByNo(new Node(2, "2", "2"));
        linkedList.list();
        linkedList.addByNo(new Node(1, "1", "1"));
        linkedList.list();
        linkedList.addByNo(new Node(4, "4", "4"));
        linkedList.list();
        linkedList.addByNo(new Node(3, "3", "4"));
        linkedList.list();

        linkedList.update(new Node(2, "2", "two"));
        linkedList.list();

        System.out.println("======================");
        System.out.println(linkedList.remove(4));
        System.out.println("======================");
        linkedList.list();
    }
}

@Getter
@Setter
class Node {
    private int no;
    private String name;
    private String nickName;
    private Node next;

    public Node(int no, String name, String nickName) {
        this.no = no;
        this.name = name;
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "Node{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}

























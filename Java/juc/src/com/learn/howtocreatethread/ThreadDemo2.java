package com.learn.howtocreatethread;

import java.util.LinkedList;
import java.util.List;

/**
 * 写一个固定容器同步容器，拥有put，get，getCount方法
 * 能够支持2个生产者线程以及10个消费者线程的阻塞调用
 *
 * @author Edie
 * @since
 **/
public class ThreadDemo2 {

    static class MyContainer<T> {
        final private List<T> list = new LinkedList<>();
        final private int MAX = 10;
        private int count = 0;

        public synchronized void put(T t) {
            while (list.size() == MAX) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.add(t);
            count++;
            System.out.println(Thread.currentThread().getName() + "插入一个元素，当前元素个数：" + getCount());
            this.notifyAll();

        }

        public synchronized T get() {
            while (list.size() == 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count--;

            T t = list.remove(0);
            System.out.println(Thread.currentThread().getName() + "获取一个元素，当前元素个数：" + getCount());
            this.notifyAll();
            return t;

        }


        public synchronized Integer getCount() {
            return count;
        }


    }


    public static void main(String[] args) {
        MyContainer myContainer = new MyContainer();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 25; j++) {
                    myContainer.put(new Object());
                }
            }).start();
        }

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    myContainer.get();
                }
            }).start();
        }

    }


}

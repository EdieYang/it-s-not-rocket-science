package com.learn.howtocreatethread;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 写一个固定容器同步容器，拥有put，get，getCount方法
 * 能够支持2个生产者线程以及10个消费者线程的阻塞调用
 * ReentrantLock 写法
 *
 * @author Edie
 * @since
 **/
public class ThreadDemo2ReentrantLock {

    static class MyContainer<T> {
        private final List<T> list = new LinkedList<>();
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition consumerCondition = lock.newCondition();
        private final Condition producerCondition = lock.newCondition();
        private final int MAX = 10;
        private int count = 0;

        public void put(T t) {
            lock.lock();
            try {
                while (list.size() == MAX) {
                    producerCondition.await();
                }
                list.add(t);
                count++;
                System.out.println(Thread.currentThread().getName() + "插入一个元素，当前元素个数：" + getCount());
                consumerCondition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }

        public synchronized T get() {
            T t = null;
            lock.lock();
            try {
                while (list.size() == 0) {
                    producerCondition.await();
                }
                count--;
                t = list.remove(0);
                System.out.println(Thread.currentThread().getName() + "获取一个元素，当前元素个数：" + getCount());
                producerCondition.signalAll();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                return t;
            }

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

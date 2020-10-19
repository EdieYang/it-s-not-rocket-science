package com.learn.howtocreatethread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数
 * 当个数到5时，线程2给出提示并结束
 *
 * @author Edie
 * @since
 **/
public class ThreadDemo {


    //1.加入volatile ，但是需要加时间睡1秒，等cpu把新加元素的列表刷到线程缓存中
    //2.加synchronized，不用加volatile
//    private static volatile List<Integer> list = new ArrayList<>();
    private static List<Integer> list = new ArrayList<>();


    private static CountDownLatch countDownLatch = new CountDownLatch(5);
    private static CountDownLatch countDownLatch2 = new CountDownLatch(1);

    private static ThreadDemo threadDemo = new ThreadDemo();
    private static Thread thread1 = null;
    private static Thread thread2 = null;


    public static void main(String[] args) throws InterruptedException {
//        waitNotify();
//        CountDownLatchMethod();
//        LockSupportMethod();
    }

    /**
     * LockSupport park unpark实现
     */
    private static void LockSupportMethod() {
        thread2 = new Thread(() -> {
            System.out.println("t2 start");
            LockSupport.park();
            System.out.println("t2结束");
            LockSupport.unpark(thread1);
        });

        thread1 = new Thread(() -> {
            System.out.println("t1 start");
            synchronized (threadDemo) {
                for (int i = 0; i < 10; i++) {
                    list.add(i);
                    System.out.println("t1加入对象:" + i);
                    if (list.size() == 5) {

                        LockSupport.unpark(thread2);
                        LockSupport.park();
                    }


                }
            }
        });

        thread2.start();
        thread1.start();
    }

    /**
     * CountDownLatch实现
     */
    private static void CountDownLatchMethod() {
        Thread thread1 = new Thread(() -> {
            System.out.println("t1 start");
            synchronized (threadDemo) {
                for (int i = 0; i < 10; i++) {
                    list.add(i);
                    System.out.println("t1加入对象:" + i);
                    countDownLatch.countDown();
                    if (list.size() == 5) {
                        try {
                            countDownLatch2.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("t2 start");
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t2结束");
            countDownLatch2.countDown();

        });

        thread2.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (
                InterruptedException e) {
            e.printStackTrace();
        }
        thread1.start();
    }


    /**
     * Wait、Notify实现
     */
    private static void waitNotify() {
        Thread thread1 = new Thread(() -> {
            System.out.println("t1 start");

            synchronized (threadDemo) {
                for (int i = 0; i < 10; i++) {
                    list.add(i);
                    System.out.println("t1加入对象:" + i);
                    if (list.size() == 5) {
                        threadDemo.notify();
                        try {
                            threadDemo.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("t2 start");
            synchronized (threadDemo) {
                if (list.size() != 5) {
                    try {
                        threadDemo.wait();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (list.size() == 5) {
                    System.out.println("t2结束");
                    threadDemo.notify();
                }

            }

        });

        thread2.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread1.start();
    }


}

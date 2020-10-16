package com.learn.howtocreatethread;

/**
 * TODO
 *
 * @author Edie
 * @since
 **/
public class ThreadDemo {

    static class MyThread extends Thread {

    }

    static class MyRunnable implements Runnable {

        @Override
        public void run() {
            System.out.println("running");
        }
    }

    static class ResourceClass {
        public void hello() {

            System.out.println("hello");
        }
    }

    public static void main(String[] args) {
//        new MyThread().start();
//        new Thread(new MyRunnable()).start();
//
//        ResourceClass resourceClass = new ResourceClass();
//        new Thread(() -> resourceClass.hello()).start();

    }


}

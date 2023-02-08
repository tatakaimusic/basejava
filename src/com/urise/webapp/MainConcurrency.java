package com.urise.webapp;


public class MainConcurrency {
    private static int counter;
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                increment();
            }
        });

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                increment();
            }
        });
        thread.start();
        thread1.start();

        thread.join();
        thread1.join();


        System.out.println(counter);

    }

    public static void increment() {
        synchronized (LOCK) {
            counter++;
        }
    }
}

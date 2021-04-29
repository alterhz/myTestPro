package org.zielger.threads;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class volatileTest implements Runnable {

    private int x;
    private int y;

    CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {

        final volatileTest volatileTest = new volatileTest();

        final ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(volatileTest);
    }

    public void go() {
        x = 2;
        y = 3;
    }

    public void run() {
        System.out.println("x = " + x + ", y = " + y);
    }
}

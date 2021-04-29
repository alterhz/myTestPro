package org.ziegler.threds;


import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    CountDownLatch countDownLatch = new CountDownLatch(2);
    private int n;

    private static final sun.misc.Unsafe U = sun.misc.Unsafe.getUnsafe();
    private static long VALUE;

    static {
        try {
            Field field = Test.class.getDeclaredField("n");

// 无视权限

            field.setAccessible(true);
            VALUE = U.objectFieldOffset(field);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final Test test = new Test();
        test.run();
    }

    private void run() {

        final ExecutorService executorService = Executors.newCachedThreadPool();
        final Runnable command = new Runnable() {

            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 6000; ++i) {
                    int oldN;
                    int newN;
                    do {
                        oldN = n;
                        newN = ++oldN;
                    }
                    while (!U.compareAndSwapInt(this, VALUE, oldN, newN));
                }
                System.out.println("n = " + n);
            }
        };


        executorService.execute(command);
        executorService.execute(command);

        countDownLatch.countDown();
        countDownLatch.countDown();
    }

}

package org.zielger.threads;

import sun.misc.Unsafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CASTest {
    private static final Unsafe unsafe = com.lmax.disruptor.util.Util.getUnsafe();
    private static final long valueOffset;
    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                    (CASTest.class.getDeclaredField("value"));
        } catch (Exception ex) {
            System.out.println("CASTest.static initializer"); throw new Error(ex); }
    }
    private long value;

    public static void main(String[] args) {
        CASTest casTest = new CASTest();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService executorService = (ExecutorService) Executors.newCachedThreadPool();
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i =0;i < 6000000;i++){
                    long oldValue;
                    long newValue;
                    do{
                        oldValue = casTest.value;
                        newValue = oldValue + 1;
                    }while(!unsafe.compareAndSwapLong(casTest,valueOffset,oldValue,newValue));
                }

                System.out.println("CASTest.run "+ casTest.value);
            }
        };

        for (int i = 0; i < 4; i++) {
            new Thread(runnable).start();
        }

        countDownLatch.countDown();;
    }
}

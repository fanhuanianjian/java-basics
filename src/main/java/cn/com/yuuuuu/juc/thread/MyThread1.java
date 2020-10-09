package cn.com.yuuuuu.juc.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bhl
 */
@Slf4j
public class MyThread1 extends Thread {
    private static final int MAX = 100;

    @Override
    public void run() {
        int sum = 0;
        log.info("MyThread1:{}", Thread.currentThread().getName());
        for (int i = 0; i <= MAX; i++) {
            sum += i;
        }
        log.info("sum01:{}", sum);
    }
}
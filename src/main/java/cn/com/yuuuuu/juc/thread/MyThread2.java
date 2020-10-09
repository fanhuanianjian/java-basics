package cn.com.yuuuuu.juc.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bhl
 */
@Slf4j
public class MyThread2 implements Runnable {

    private static final int MAX = 100;

    @Override
    public void run() {

        int sum = 0;
        log.info("MyThread2:{}", Thread.currentThread().getName());
        for (int i = 0; i <= MAX; i++) {
            sum += i;
        }
        log.info("sum02:{}", sum);

    }
}
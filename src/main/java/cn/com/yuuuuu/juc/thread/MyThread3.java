package cn.com.yuuuuu.juc.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 *
 * @author bhl
 */
@Slf4j
public class MyThread3 implements Callable<Integer> {

    /**
     * 细节：
     * 1、有缓存
     * 2、结果可能需要等待，会阻塞！
     */
    private static final int MAX = 100;

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        log.info("MyThread3:{}", Thread.currentThread().getName());
        for (int i = 0; i <= MAX; i++) {
            sum += i;
        }

        return sum;
    }
}
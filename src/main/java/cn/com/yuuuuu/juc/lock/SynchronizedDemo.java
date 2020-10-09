package cn.com.yuuuuu.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: bhl
 * @Date: 2020/10/7
 * @Description: cn.com.yuuuuu.juc.lock
 * @version: 1.0
 */
@Slf4j
public class SynchronizedDemo {

    /**
     * 参考狂神说
     * https://www.bilibili.com/video/BV1B7411L7tE?p=1
     */


    private static final int MAX_THREAD = 120;

    public static void main(String[] args) {


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(MAX_THREAD,
                MAX_THREAD,
                3L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );


        SynchronizedDemo synchronizedDemo = new SynchronizedDemo();

        try {

            for (int i = 0; i < 20; i++) {
                // Synchronized
                threadPoolExecutor.execute(synchronizedDemo::sale);
                threadPoolExecutor.execute(synchronizedDemo::add);
                threadPoolExecutor.execute(SynchronizedDemo::getName);
            }

        } finally {
            threadPoolExecutor.shutdown();
        }

    }


    private int num = 10;

    private static final String TICKET_OFFICE = "华山售票厅";

    /**
     * 同一个对象情况下sale()和add()是一把锁
     */

    /**
     * 这个时候的synchronized是对象锁
     */
    public synchronized void sale() {
        if (num > 0) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("卖出了第" + (num--) + "张票,还剩:" + num);
        }

    }

    /**
     * 这个时候的synchronized是对象锁
     */
    public synchronized void add() {
        if (num == 0) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            num += 20;
            log.info("增加了20张票:{}", Thread.currentThread().getName());
        }

    }

    /**
     * 这个时候的synchronized是Class类模板的锁
     */
    public static synchronized void getName() {
        try {
            log.info("执行了add方法:{},{}", Thread.currentThread().getName(), TICKET_OFFICE);

            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

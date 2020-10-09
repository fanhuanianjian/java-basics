package cn.com.yuuuuu.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: bhl
 * @Date: 2020/10/7
 * @Description: cn.com.yuuuuu.juc.lock
 * @version: 1.0
 */
@Slf4j
public class ReentrantLockDemo {

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


        ReentrantLockDemo ticket = new ReentrantLockDemo();


        try {
            for (int i = 0; i < 20; i++) {

                // ReentrantLock
                threadPoolExecutor.execute(ticket::sale);
                threadPoolExecutor.execute(ticket::add);
                threadPoolExecutor.execute(ReentrantLockDemo::getName);

            }



        } finally {
            threadPoolExecutor.shutdown();
        }

    }

    private int num = 10;

    private final Lock lock = new ReentrantLock();
    private static Lock staticLock = new ReentrantLock();

    private static final String TICKET_OFFICE = "华山售票厅";



    public void sale() {

        lock.lock();
        try {

            if (num > 0) {
                TimeUnit.SECONDS.sleep(1);
                log.info("卖出了第" + (num--) + "张票,还剩:" + num);
            }
        } catch (Exception e) {
            log.error("sale:{}", e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public void add() {

        lock.lock();
        try {
            if (num == 0) {

                TimeUnit.SECONDS.sleep(1);

                num += 20;
                log.info("增加了20张票:{}", Thread.currentThread().getName());
            }
        } catch (Exception e) {
            log.error("add:{}", e.getMessage());
        } finally {
            lock.unlock();
        }

    }

    public static synchronized void getName() {
        staticLock.lock();
        try {
            log.info("执行了add方法:{},{}", Thread.currentThread().getName(), TICKET_OFFICE);

            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            staticLock.unlock();
        }
    }

}

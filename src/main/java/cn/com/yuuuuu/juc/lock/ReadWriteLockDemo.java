package cn.com.yuuuuu.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 *
 * @Author: bhl
 * @Date: 2020/10/7
 * @Description: cn.com.yuuuuu.juc.lock
 * @version: 1.0
 */
@Slf4j
public class ReadWriteLockDemo {

    /**
     * 参考狂神说
     * https://www.bilibili.com/video/BV1B7411L7tE?p=1
     */

    /**
     * 独占锁（写锁） 一次只能被一个线程占有
     * 共享锁（读锁） 多个线程可以同时占有
     * ReadWriteLock
     * 读-读 可以共存！
     * 读-写 不能共存！
     * 写-写 不能共存！
     */

    private static final int MAX_THREAD = 100;

    public static void main(String[] args) {

        ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(MAX_THREAD,
                MAX_THREAD,
                3L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );

        try {
            for (int i = 0; i < 50; i++) {

                int finalI = i;
                threadPoolExecutor.execute(() -> {
                    readWriteLockDemo.put(finalI + "", UUID.randomUUID().toString());
                });

            }
            TimeUnit.SECONDS.sleep(1);

            for (int i = 0; i < 50; i++) {

                int finalI = i;
                threadPoolExecutor.execute(() -> {
                    readWriteLockDemo.get(finalI + "");
                });

            }
        } catch (Exception e) {
            log.error("main:{}", e.getMessage());
        } finally {
            threadPoolExecutor.shutdown();
        }

    }


    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Map<String, Object> map = new HashMap<>();

    public void put(String key, Object value) {
        readWriteLock.writeLock().lock();
        try {
            TimeUnit.SECONDS.sleep(1);
            map.put(key, value);
            log.info("添加了Key:{}", key);
        } catch (Exception e) {
            log.error("put:{}", e.getMessage());
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock();
        }

    }

    public void get(String key) {
        readWriteLock.readLock().lock();
        try {
            log.info("读取了Key:{}", map.get(key));
        } catch (Exception e) {
            log.error("put:{}", e.getMessage());
            e.printStackTrace();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }


}

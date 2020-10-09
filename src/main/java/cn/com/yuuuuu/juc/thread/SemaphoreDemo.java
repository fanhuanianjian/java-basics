package cn.com.yuuuuu.juc.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 信号量
 * @Author: bhl
 * @Date: 2020/10/9
 * @Description: cn.com.yuuuuu.juc.thread
 * @version: 1.0
 */
@Slf4j
public class SemaphoreDemo {
    //[ˈseməfɔːr] 三木far儿

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                6,
                6,
                3L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 6; i++) {
            threadPoolExecutor.execute(() -> {

                    try {
                        semaphore.acquire();
                        log.info("{}:抢到车位",Thread.currentThread().getName());
                        TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                        log.info("{}:离开了车位",Thread.currentThread().getName());
                        semaphore.release();
                    } catch (Exception e) {
                        log.error("main:{}",e.getMessage());
                    }

                }
            );
        }
        threadPoolExecutor.shutdown();
    }
}

package cn.com.yuuuuu.juc.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @Author: bhl
 * @Date: 2020/10/8
 * @Description: cn.com.yuuuuu.juc.thread
 * @version: 1.0
 */
@Slf4j
public class CountDownLatchDemo {
    /**
     * 参考狂神说
     * https://www.bilibili.com/video/BV1B7411L7tE?p=1
     */
    public static void main(String[] args) throws Exception {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(6,
                Runtime.getRuntime().availableProcessors(),
                3L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        // 设置计数器数
        CountDownLatch countDownLatch = new CountDownLatch(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i <Runtime.getRuntime().availableProcessors() ; i++) {

            threadPoolExecutor.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                } catch (Exception e) {
                    log.error("error:{}",e.getMessage());
                }
                log.info("{}:Go out",Thread.currentThread().getName());

                // 每次次执行计数器减一
                countDownLatch.countDown();
            });

        }

        // 等待计数器归零，然后再向下执行
        countDownLatch.await();

        log.info("Close Door");
        threadPoolExecutor.shutdown();

    }

}

package cn.com.yuuuuu.juc.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: bhl
 * @Date: 2020/10/8
 * @Description: cn.com.yuuuuu.juc.thread
 * @version: 1.0
 */
@Slf4j
public class CyclicBarrierDemo {
    //[ˈsaɪklɪk][ˈbæriər] 塞克类克拜瑞尔
    /**
     * 参考狂神说
     * https://www.bilibili.com/video/BV1B7411L7tE?p=1
     */
    public static void main(String[] args) {

        AtomicBoolean flag = new AtomicBoolean(true);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(12,
                Runtime.getRuntime().availableProcessors(),
                3L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,()->{

            if (flag.get()) {
                log.info("神龙出现了");
                flag.set(false);
            } else {
                log.info("龙珠飞走了");
            }

        });

        for (int i = 0; i < 7; i++) {
            int finalI = i;
            threadPoolExecutor.execute(()->{

                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                    log.info("{}:收集了第{}颗龙珠",Thread.currentThread().getName(),finalI+1);
                    cyclicBarrier.await();

                    TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                    log.info("{}:许下了愿望",Thread.currentThread().getName());
                    cyclicBarrier.await();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        threadPoolExecutor.shutdown();
    }

}

package cn.com.yuuuuu.juc.lock;

import ch.qos.logback.classic.pattern.ClassOfCallerConverter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * @Author: bhl
 * @Date: 2020/10/7
 * @Description: cn.com.yuuuuu.juc.lock
 * @version: 1.0
 */
@Slf4j
public class VolatileDemo {


    public static void main(String[] args) {
        VolatileDemo volatileDemo = new VolatileDemo();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                3L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
        threadPoolExecutor.execute(volatileDemo::run);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadPoolExecutor.execute(volatileDemo::change);

        threadPoolExecutor.shutdown();

    }


    // private  boolean flag = true;
    private volatile boolean flag = true;

    public void run(){
        //while 有内容时flag是可见的 待分析
        while (flag){
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
         //  log.info("run:{}:{}",LocalDateTime.now(),Thread.currentThread().getName());
        }
    }

    public void change(){
        flag = !flag;
        log.info("change:{}",Thread.currentThread().getName());
    }


}

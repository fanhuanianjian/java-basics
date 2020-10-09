package cn.com.yuuuuu.juc.lock;

import ch.qos.logback.classic.pattern.ClassOfCallerConverter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 *
 * volatile 保证可见性
 * 验证这个问题的关键,不从主内存读，读取线程本身的工作内存
 *
 * @Author: bhl
 * @Date: 2020/10/7
 * @Description: cn.com.yuuuuu.juc.lock
 * @version: 1.0
 */
@Slf4j
public class VolatileDemo {

    /**
     * 参考狂神说
     * https://www.bilibili.com/video/BV1B7411L7tE?p=1
     */

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

    /**
     * while 有以下内容时flag是可见的
     * Thread.sleep,触发了线程的重新调度,保存当前线程上下文，即刷到主内存。
     * 存在synchronized,当获取锁以后,清空本地内存中共享变量，从主内存进行加载，在释放锁时将本地内存中共享变量刷新到主内存中。
     * System.out.println(); 中存在synchronized 同第二种;
     */

    public void run(){

        while (flag){
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//          log.info("run:{}:{}",LocalDateTime.now(),Thread.currentThread().getName());
        }
    }

    public void change(){
        flag = !flag;
        log.info("change:{}",Thread.currentThread().getName());
    }


}

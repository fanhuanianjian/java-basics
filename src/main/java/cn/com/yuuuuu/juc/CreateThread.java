package cn.com.yuuuuu.juc;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.*;

/**
 * 创建线程的方式
 *
 * @author: bhl
 * @Date: 2020/10/7
 * @Description: cn.com.yuuuuu.juc
 * @version: 1.0
 */
@Slf4j
public class CreateThread {

    /**
     * 参考狂神说
     * https://www.bilibili.com/video/BV1B7411L7tE?p=1
     */
    private static final int MAX = 100;

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /**
         * 创建线程的方式(不推荐使用)
         */
        //01 继承Thread
        new MyThread1().start();

        //02 实现Runnable
        new Thread(new MyThread2()).start();

        //03 内部类(lambda方式)
        new Thread(() -> {
            int sum = 0;
            log.info("main:{}", Thread.currentThread().getName());
            for (int i = 0; i <= MAX; i++) {
                sum += i;
            }
            log.info("sum03:{}", sum);

        }).start();

        //04 Callable
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread3());
        new Thread(futureTask).start();
        log.info("sum04:{}", futureTask.get());

        //05 线程池：

        /**
         * 创建线程池-三大方法
         */
        //单个线程
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

        //创建一个固定的线程池的大小
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

        //可伸缩的线程池
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        try {

            for (int i = 0; i < 10; i++) {
                singleThreadPool.execute(() -> {
                    log.info("Thread-singleThreadPool:{}", Thread.currentThread().getName());

                });

                fixedThreadPool.execute(() -> {
                    log.info("Thread-fixedThreadPool:{}", Thread.currentThread().getName());
                });

                cachedThreadPool.execute(() -> {
                    log.info("Thread-cachedThreadPool:{}", Thread.currentThread().getName());
                });
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            singleThreadPool.shutdown();
            fixedThreadPool.shutdown();
            cachedThreadPool.shutdown();

        }

        /**
         * 创建线程的方式(推荐使用)
         */

        /** 创建线程池的-七大参数
         * public ThreadPoolExecutor(int corePoolSize, // 核心线程池大小
         *                           int maximumPoolSize, // 最大核心线程池大小
         *                           long keepAliveTime, // 超时了没有人调用就会释放
         *                           TimeUnit unit, // 超时单位
         *                           BlockingQueue<Runnable> workQueue, // 阻塞队列
         *                           ThreadFactory threadFactory,  // 线程工厂：创建线程的，一般 不用动
         *                           RejectedExecutionHandler handler // 拒绝策略
         *                           ){}
         */

        /** -四种拒绝策略
         *  new ThreadPoolExecutor.AbortPolicy() // 线程池满了，还有线程进来，不处理这个线程的，抛出异常
         *  new ThreadPoolExecutor.CallerRunsPolicy() // 哪来的去哪里
         *  new ThreadPoolExecutor.DiscardPolicy() //队列满了，丢掉任务，不会抛出异常
         *  new ThreadPoolExecutor.DiscardOldestPolicy() //队列满了，尝试去和最早的竞争，也不会抛出异常
         */

        // 获取CPU的核数 Runtime.getRuntime().availableProcessors()


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(6,
                Runtime.getRuntime().availableProcessors(),
                3L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        try {

            for (int i = 0; i < 10; i++) {
                threadPoolExecutor.execute(() -> {
                    log.info("Thread-threadPoolExecutor:{}", Thread.currentThread().getName());

                });
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            threadPoolExecutor.shutdown();
        }

    }


}

@Slf4j
class MyThread1 extends Thread {
    private static final int MAX = 100;

    @Override
    public void run() {
        int sum = 0;
        log.info("MyThread1:{}", Thread.currentThread().getName());
        for (int i = 0; i <= MAX; i++) {
            sum += i;
        }
        log.info("sum01:{}", sum);
    }
}

@Slf4j
class MyThread2 implements Runnable {

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

@Slf4j
class MyThread3 implements Callable<Integer> {

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

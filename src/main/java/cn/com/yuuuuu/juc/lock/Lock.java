package cn.com.yuuuuu.juc.lock;


import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: bhl
 * @Date: 2020/10/7
 * @Description: cn.com.yuuuuu.juc
 * @version: 1.0
 */
public class Lock {
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


        TicketByReentrantLock ticket = new TicketByReentrantLock();
        TicketBySynchronized ticketBySynchronized = new TicketBySynchronized();

        try {
            for (int i = 0; i < 20; i++) {

                // ReentrantLock
                threadPoolExecutor.execute(ticket::sale);
                threadPoolExecutor.execute(ticket::add);
                threadPoolExecutor.execute(TicketByReentrantLock::getName);

                // Synchronized
                threadPoolExecutor.execute(ticketBySynchronized::sale);
                threadPoolExecutor.execute(ticketBySynchronized::add);
                threadPoolExecutor.execute(TicketBySynchronized::getName);
            }
            


        } finally {
            threadPoolExecutor.shutdown();
        }

    }

}



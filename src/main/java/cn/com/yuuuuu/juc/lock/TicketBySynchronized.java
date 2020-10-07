package cn.com.yuuuuu.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: bhl
 * @Date: 2020/10/7
 * @Description: cn.com.yuuuuu.juc.lock
 * @version: 1.0
 */
@Slf4j
public class TicketBySynchronized {

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

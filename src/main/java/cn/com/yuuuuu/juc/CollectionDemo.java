package cn.com.yuuuuu.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: bhl
 * @Date: 2020/10/6
 * @Description: cn.com.yuuuuu
 * @version: 1.0
 */
@Slf4j
public class CollectionDemo {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,5));
                log.info("list.add:{}",list);
            }).start();
        }
    }

}

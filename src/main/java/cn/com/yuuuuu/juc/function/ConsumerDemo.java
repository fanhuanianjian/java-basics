package cn.com.yuuuuu.juc.function;

import java.util.function.Consumer;

/**
 * Consumer 消费型接口
 * @Author: bhl
 * @Date: 2020/10/9
 * @Description: cn.com.yuuuuu.juc.function
 * @version: 1.0
 */
public class ConsumerDemo {

    public static void main(String[] args) {
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };

        consumer.accept("1024");
        Consumer<String> consumerLambda = str -> System.out.println(str);
        consumerLambda.accept("233");

    }

}

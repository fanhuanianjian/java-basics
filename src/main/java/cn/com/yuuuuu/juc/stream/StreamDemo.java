package cn.com.yuuuuu.juc.stream;

import cn.com.yuuuuu.juc.pojo.User;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

/**
 * Stream流式计算
 *
 * @Author: bhl
 * @Date: 2020/10/9
 * @Description: cn.com.yuuuuu.juc.stream
 * @version: 1.0
 */
public class StreamDemo {
    // [striːm]

    public static void main(String[] args) {
        User u1 = new User(1, "a", 21);
        User u2 = new User(2, "b", 22);
        User u3 = new User(3, "c", 23);
        User u4 = new User(4, "d", 24);
        User u5 = new User(6, "e", 25);
        // 集合就是存储
        List<User> list = Arrays.asList(u1, u2, u3, u4, u5);


        list.stream().
                filter(user -> user.getId() % 2 == 0)
                .filter(user -> user.getAge() > 23)
                .map(user -> {
                    user.setName(user.getName().toUpperCase());
                    return user;
                }).sorted(Comparator.comparingInt(User::getId).reversed())
                .limit(1)
                .forEach(System.out::println);

    }

}

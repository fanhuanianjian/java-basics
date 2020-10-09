package cn.com.yuuuuu.juc.function;

import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * 断定型接口：有一个输入参数，返回值只能是 布尔值
 * @Author: bhl
 * @Date: 2020/10/9
 * @Description: cn.com.yuuuuu.juc.function
 * @version: 1.0
 */
public class PredicateDemo {
    public static void main(String[] args) {
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return !StringUtils.isEmpty(s);
            }
        };

        System.out.println(predicate.test(""));

        Predicate<String> predicateLambda = str-> !StringUtils.isEmpty(str);

        System.out.println(predicateLambda.test("1"));
    }
}

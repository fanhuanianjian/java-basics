package cn.com.yuuuuu.juc.function;

import java.util.function.Function;

/**
 *
 * @Author: bhl
 * @Date: 2020/10/9
 * @Description: cn.com.yuuuuu.juc.function
 * @version: 1.0
 */
public class FunctionDemo {

    /**
     * Function 函数型接口, 有一个输入参数，有一个输出
     * 只要是 函数型接口 可以 用 lambda表达式简化
     */

    public static void main(String[] args) {

        Function<Integer, String> function = new Function<>() {
            @Override
            public String apply(Integer s) {
                return String.valueOf(s + 100);
            }
        };
        System.out.println(function.apply(15));

        Function<Integer, String> functionLambda = num ->String.valueOf(num + 100);
        System.out.println(functionLambda.apply(15));

    }
}

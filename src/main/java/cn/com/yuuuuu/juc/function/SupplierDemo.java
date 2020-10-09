package cn.com.yuuuuu.juc.function;


import java.util.function.Supplier;

/**
 * @Author: bhl
 * @Date: 2020/10/9
 * @Description: cn.com.yuuuuu.juc.function
 * @version: 1.0
 */
public class SupplierDemo {
    //[səˈplaɪər] 色派尔 供应商

    public static void main(String[] args) {

        Supplier<String> supplier = new Supplier<>() {
            @Override
            public String get() {
                return "233";
            }
        };
        System.out.println(supplier.get());

        Supplier<String> supplierLambda = ()-> "2332";

        System.out.println(supplierLambda.get());

    }
}

package cn.com.yuuuuu.stream;

import cn.com.yuuuuu.juc.pojo.User;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    /**
     * 集合是存储,流是计算
     *  创建 Stream
     * 一个数据源（如：集合、数组），获取一个流
     *  中间操作
     * 一个中间操作链，对数据源的数据进行处理
     *  终止操作(终端操作)
     * 一个终止操作，执行中间操作链，并产生结果
     */


    User u1 = new User(1, "aa", 21);
    User u2 = new User(2, "ba", 22);
    User u3 = new User(3, "ca", 23);
    User u4 = new User(6, "da", 24);
    User u5 = new User(6, "ea", 25);
    User u6 = new User(6, "ea", 27);

    List<User> list = Arrays.asList(u1, u2, u3, u4, u5,u6);


    /**
     * 一、创建Stream流
     */
    @Test
    public void test01(){
        //1. Collection 提供了两个方法stream()与parallelStream()
        //串行流
        Stream<User> stream = list.stream();
        //并行流
        Stream<User> parallelStream = list.parallelStream();

        //2.通过Arrays中的stream()获取一个数组流
        Integer[] numArray = new Integer[]{2, 2, 3};
        Stream<Integer> streamByArrays = Arrays.stream(numArray);

        //3.通过Stream类中静态方法of()
        Stream<String> stringStream = Stream.of("2", "2", "2", "2", "2", "2");

        //4.创建无限流
        //迭代
        Stream.iterate(0,x->x+=2).limit(10).forEach(System.out::println);

        //生成
        Stream.generate(UUID::randomUUID).limit(10).forEach(System.out::println);

    }
    /**
     * 二、中间操作
     * 所有的中间操作不会做任何的处理
     * 只有当做终止操作时，所有的中间操作会一次性的全部执行，称为“惰性求值”
     */

    /**
     * 1.筛选与切片
     * filter 过滤
     * skip(n) 调过
     * limit(n) 限制
     * distinct 去重
     */
    @Test
    public void test2(){
        list.stream()
                .filter(user -> user.getAge()>22)
                .skip(1L)
                .limit(2L)
                .distinct()
                .forEach(System.out::println);
    }

    /**
     *	映射
     *	map——接收 Lambda,将元素转换成其他形式或提取信息。接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
     *	flatMap——接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流
	 */
    @Test
    public void test3(){
        list.stream()
                .map(user -> user.getName().toUpperCase())
                .forEach(System.out::println);
        list.stream()
                .map(User::getName)
                .flatMap(StreamDemo::getCharStream)
                .forEach(System.out::println);
    }

    public static Stream<Character> getCharStream(String str){
        return str.chars().mapToObj(x -> (char)x);
    }

    /**
     *sorted()——自然排序
     *sorted(Comparator com)——定制排序
     */
    @Test
    public void test4(){
        list.stream()
                .map(User::getAge)
                .sorted()
                .forEach(System.out::println);

        list.stream().sorted((x,y)->{
            if(Objects.equals(x.getName(),y.getName())){
                return x.getAge().compareTo(y.getAge());
            }else {
                return x.getName().compareTo(y.getName());
            }
        }).forEach(System.out::println);

        //Comparator.comparingInt(User::getId).reversed()

        list.stream()
                .sorted(Comparator.comparingInt(User::getId).reversed())
                .forEach(System.out::println);
    }

    /**
     * 三、终止操作
     * allMatch——检查是否匹配所有元素
     * anyMatch——检查是否至少匹配一个元素
     * noneMatch——检查是否没有匹配的元素
     * findFirst——返回第一个元素
     * findAny——返回当前流中的任意元素
     * count——返回流中元素的总个数
     * max——返回流中最大值
     * min——返回流中最小值
     */
    @Test
    public void test5(){
        // 检查是否匹配所有元素
        boolean allMatch = list.stream().allMatch(user -> user.getName().contains("a"));
        System.out.println(allMatch?"全部匹配":"不全部匹配");

        // 检查是否至少匹配一个元素
        boolean anyMatch = list.stream().anyMatch(user -> Objects.equals(user.getAge(), 33));
        System.out.println(anyMatch?"有33岁的用户":"没有33岁的用户");

        // 检查是否至少匹配一个元素
        boolean noneMatch = list.stream().noneMatch(user -> Objects.equals(user.getAge(), 455));
        System.out.println(noneMatch?"没有455岁的用户":"有455岁的用户");

        // 返回第一个元素
        Optional<User> user = list.stream().findFirst();
        System.out.println(user.orElse(null));

        // 返回当前流中的任意元素
        Optional<User> any = list.stream().parallel().filter(u -> u.getAge() > 21).findAny();
        System.out.println(any.orElse(null));

        // 返回流中元素的总个数
        long count = list.stream().filter(u->u.getAge()>22).count();
        System.out.println(count);

        // 返回流中最大值
        Optional<User> max = list.stream().max(Comparator.comparingInt(User::getAge));
        System.out.println(max.orElse(null));

        // 返回流中最大值
        Optional<User> min = list.stream().min(Comparator.comparingInt(User::getAge));
        System.out.println(min.orElse(null));
    }

    /**
     * 归约
     * reduce(T identity, BinaryOperator) / reduce(BinaryOperator) ——可以将流中元素反复结合起来，得到一个值。
     */
    @Test
    public void test6(){

        List<Integer> listNum = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        Integer sum = listNum.stream().reduce(0, Integer::sum);
        System.out.println(sum);

        Optional<Integer> sumOptional = list.stream().map(User::getAge).reduce((x,y)->x*y);
        System.out.println(sumOptional.orElse(null));

        //需求：搜索名字中 “a” 出现的次数
        Optional<Integer> countReduce = list.stream()
                .map(User::getName)
                .flatMap(StreamDemo::getCharStream)
                .map(character -> {
                    if (Objects.equals(character, 'a')) {
                        return 1;
                    } else {
                        return 0;
                    }
                }).reduce(Integer::sum);

        System.out.println(countReduce.orElse(null));

       Long s = list.stream()
                .map(User::getName)
                .flatMap(StreamDemo::getCharStream)
                .filter(c->Objects.equals(c,'a')).count();
       System.out.println(s);

    }
    /**
     * collect
     * 将流转换为其他形式。接收一个 Collector接口的实现，用于给Stream中元素做汇总的方法
     */
    @Test
    public void test7(){
        List<User> userList = list.stream().filter(user -> user.getAge() > 22).collect(Collectors.toList());
        userList.forEach(System.out::println);

        Set<String> strings = list.stream().map(User::getName).collect(Collectors.toSet());
        strings.forEach(System.out::println);

        LinkedList<Integer> linkedList = list.stream().map(User::getAge).collect(Collectors.toCollection(LinkedList::new));
        linkedList.forEach(System.out::println);

        System.out.println("--------------------------------------------");
        Optional<Integer> collectMax = list.stream().map(User::getAge).collect(Collectors.maxBy(Integer::compare));
        Optional<User> collect = list.stream().collect(Collectors.minBy((uu1, uu2) -> Integer.compare(uu1.getId(), uu2.getId())));
        Integer sum = list.stream().collect(Collectors.summingInt(User::getAge));
        Double avg = list.stream().collect(Collectors.averagingDouble(User::getAge));
        Long count = list.stream().collect(Collectors.counting());
        System.out.println("--------------------------------------------");
        IntSummaryStatistics is = list.stream().collect(Collectors.summarizingInt(User::getAge));
        System.out.println(is);

    }

    /**
     * 分组
     */
    @Test
    public void test8(){

        //分组
        Map<Integer, List<User>> collect = list.stream().collect(Collectors.groupingBy(User::getId));
        System.out.println(collect);

        //多级分组
        Map<Integer, Map<String, List<User>>> map = list.stream().collect(Collectors.groupingBy(User::getId, Collectors.groupingBy(User::getName)));
        System.out.println(map);

    }

    /**
     * 分区
     */
    @Test
    public void test9(){
        Map<Boolean, List<User>> collect = list.stream().collect(Collectors.partitioningBy(user -> user.getAge() > 23));
        System.out.println(collect);
    }

    @Test
    public void test10(){
        String collect = list.stream().map(User::getName).collect(Collectors.joining(",","+++","---"));
        System.out.println(collect);
    }


}
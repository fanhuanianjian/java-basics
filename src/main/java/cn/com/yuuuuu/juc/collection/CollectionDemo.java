package cn.com.yuuuuu.juc.collection;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @Author: bhl
 * @Date: 2020/10/6
 * @Description: cn.com.yuuuuu
 * @version: 1.0
 */
@Slf4j
public class CollectionDemo {

    public static void main(String[] args) {

        //线程不安全的Collection系集合
        //多线程并发操作时会出现ConcurrentModificationException(并发修改异常)
        List<String> arrayList = new ArrayList<>();
        List<String> linkList = new LinkedList<>();

        Set<String> hashSet = new HashSet<>();
        Set<String> linkedHashSet = new LinkedHashSet<>();
        Set<String> treeSet = new TreeSet<>();


        //线程安全的Collection系集合
        List<String> vector = new Vector<>();
        // Collections.synchronizedCollection(new ArrayList<>()); 待研究
        Collection<Object> syncArray  = Collections.synchronizedList(new ArrayList<>());
        Collection<Object> syncLinked  = Collections.synchronizedList(new LinkedList<>());
        Set<String> syncHashSet  = Collections.synchronizedSet(new HashSet<>());
        Set<String> syncLinkedHashSet =Collections.synchronizedSet(new LinkedHashSet<>());
        Set<String> syncTreeSet =Collections.synchronizedSet(new TreeSet<>());




        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                syncArray.add(UUID.randomUUID().toString().substring(0,5));
                log.info("list.add:{}",syncArray);
            }).start();
        }
    }

}

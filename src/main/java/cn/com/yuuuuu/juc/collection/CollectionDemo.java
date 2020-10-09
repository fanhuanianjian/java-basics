package cn.com.yuuuuu.juc.collection;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.*;

/**
 * @Author: bhl
 * @Date: 2020/10/6
 * @Description: cn.com.yuuuuu
 * @version: 1.0
 */
@Slf4j
public class CollectionDemo {

    /**
     * 参考狂神说
     * https://www.bilibili.com/video/BV1B7411L7tE?p=1
     */

    public static void main(String[] args) {

        // 线程不安全的Collection系集合
        // 多线程并发操作时会出现ConcurrentModificationException(并发修改异常)
        List<String> arrayList = new ArrayList<>();
        List<String> linkList = new LinkedList<>();

        Set<String> hashSet = new HashSet<>();
        Set<String> linkedHashSet = new LinkedHashSet<>();
        Set<String> treeSet = new TreeSet<>();




        // 线程安全的Collection系集合
        // 已放弃
        List<String> vector = new Vector<>();

        // 读写都是synchronized
        List<String> syncArray = Collections.synchronizedList(new ArrayList<>());
        List<String> syncLinked = Collections.synchronizedList(new LinkedList<>());
        Set<String> syncHashSet = Collections.synchronizedSet(new HashSet<>());
        Set<String> syncLinkedHashSet = Collections.synchronizedSet(new LinkedHashSet<>());
        Set<String> syncTreeSet = Collections.synchronizedSet(new TreeSet<>());

        // jdk 8 写入时ReentrantLock,jdk11是synchronized
        List<String> copyOnWriteArray = new CopyOnWriteArrayList<>();
        Set<String> copyOnWriteSet = new CopyOnWriteArraySet<>();


        /**
         * 方式       抛出异常   有返回值,不抛出异常   阻塞       等待超时等待
         * 添加       add       offer()           put()     offer(,,)
         * 移除       remove    poll()            take()    poll(,)
         * 检测队首元素 element   peek
         */
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(3);

        /** 同步队列 * 和其他的BlockingQueue 不一样，
         *  SynchronousQueue 不存储元素
         *  put了一个元素，必须从里面先take取出来，否则不能在put进去值！
         */
        SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();

        // 线程不安全的Map系集合
        Map<String,String> map = new HashMap<>();
        Map<String,String> linkedHashMap  = new LinkedHashMap<>();

        // 线程安全的Map系集合
        Map<String,String> concurrentHashMap = new ConcurrentHashMap<>();
    }

}

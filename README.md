# 并发与多线程

## 一、并发与并行

### 1、并发

某个时间段内，多任务交替处理的能力

### 2、并行

同时处理多任务的能力

## 二、线程和进程

### 1、进程

操作系统进行资源调度很分配的基本单位

### 2、线程

进程中最小的执行单位，是cpu资源的调度和分派的的基本单位

多线程可以共享所属进程的所有资源

### 3、线程的创建方式

#### 3.1、继承Thread类(不推荐)

```java
/**
 * @author bhl
 */
@Slf4j
public class MyThread1 extends Thread {
    private static final int MAX = 100;

    @Override
    public void run() {
        int sum = 0;
        log.info("MyThread1:{}", Thread.currentThread().getName());
        for (int i = 0; i <= MAX; i++) {
            sum += i;
        }
        log.info("sum01:{}", sum);
    }
}
```

#### 3.2、实现Runnable接口(不推荐)

```java
/**
 * @author bhl
 */
@Slf4j
public class MyThread2 implements Runnable {

    private static final int MAX = 100;

    @Override
    public void run() {

        int sum = 0;
        log.info("MyThread2:{}", Thread.currentThread().getName());
        for (int i = 0; i <= MAX; i++) {
            sum += i;
        }
        log.info("sum02:{}", sum);

    }
}
```

#### 3.3、内部类(lambda方式)(不推荐)

```java
new Thread(() -> {
    int sum = 0;
    log.info("main:{}", Thread.currentThread().getName());
    for (int i = 0; i <= MAX; i++) {
        sum += i;
    }
    log.info("sum03:{}", sum);

}).start();
```

#### 3.4、实现Callable接口(不推荐)

```java
/**
 *
 * @author bhl
 */
@Slf4j
public class MyThread3 implements Callable<Integer> {

    /**
     * 细节：
     * 1、有缓存
     * 2、结果可能需要等待，会阻塞！
     */
    private static final int MAX = 100;

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        log.info("MyThread3:{}", Thread.currentThread().getName());
        for (int i = 0; i <= MAX; i++) {
            sum += i;
        }

        return sum;
    }
}
```

#### 3.5、线程池中获取(推荐)

##### 3.5.1创建线程池-三大方法(不推荐)

```java
        /**
         * 创建线程池-三大方法(不推荐)
         * 1)FixedThreadPool和SingleThreadPool∶允许的请求队列长度为 Integer.MAXVALUE，可能会堆积大量的请求，从而导致OOM.
         * 2)CachedThreadPool:允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM.
         */

        
        //单个线程
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

        //创建一个固定的线程池的大小
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

        //可伸缩的线程池
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        try {

            for (int i = 0; i < 10; i++) {
                singleThreadPool.execute(() -> {
                    log.info("Thread-singleThreadPool:{}", Thread.currentThread().getName());

                });

                fixedThreadPool.execute(() -> {
                    log.info("Thread-fixedThreadPool:{}", Thread.currentThread().getName());
                });

                cachedThreadPool.execute(() -> {
                    log.info("Thread-cachedThreadPool:{}", Thread.currentThread().getName());
                });
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            singleThreadPool.shutdown();
            fixedThreadPool.shutdown();
            cachedThreadPool.shutdown();

        }

```

##### 3.5.2手动创建线程池(推荐使用)

```java

/** 创建线程池的-七大参数
 * public ThreadPoolExecutor(int corePoolSize, // 核心线程池大小
 *                           int maximumPoolSize, // 最大核心线程池大小
 *                           long keepAliveTime, // 超时了没有人调用就会释放
 *                           TimeUnit unit, // 超时单位
 *                           BlockingQueue<Runnable> workQueue, // 阻塞队列
 *                           ThreadFactory threadFactory,  // 线程工厂：创建线程的，一般 不用动
 *                           RejectedExecutionHandler handler // 拒绝策略
 *                           ){}
 */

/** -四种拒绝策略
 *  new ThreadPoolExecutor.AbortPolicy() // 线程池满了，还有线程进来，不处理这个线程的，抛出异常
 *  new ThreadPoolExecutor.CallerRunsPolicy() // 哪来的去哪里
 *  new ThreadPoolExecutor.DiscardPolicy() //队列满了，丢掉任务，不会抛出异常
 *  new ThreadPoolExecutor.DiscardOldestPolicy() //队列满了，尝试去和最早的竞争，也不会抛出异常
 */

// 获取CPU的核数 Runtime.getRuntime().availableProcessors()


ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(6,
        Runtime.getRuntime().availableProcessors(),
        3L,
        TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(3),
        Executors.defaultThreadFactory(),
        new ThreadPoolExecutor.AbortPolicy()
);

try {

    for (int i = 0; i < 10; i++) {
        threadPoolExecutor.execute(() -> {
            log.info("Thread-threadPoolExecutor:{}", Thread.currentThread().getName());

        });
    }

} catch (Exception e) {
    log.error(e.getMessage());
} finally {
    threadPoolExecutor.shutdown();
}
```

### 4、线程的生命周期(jdk11)

```java
public static enum State {
    //新生
    NEW,
    //就绪
    RUNNABLE,
    //阻塞
    BLOCKED,
    //等待
    WAITING,
    //超时等待
    TIMED_WAITING,
    //终止
    TERMINATED;

    private State() {
    }
}
```

### 5、线程安全

线程安全的核心理念:要么只读，要么加锁

#### 5.1、定义

多个线程并发执行时，仍旧能够保证共享数据的正确性，这种现象称之为线程安全。反之称为线程不安全。

#### 5.2、导致线程不安全的因素

- 多个线程并发执行
- 多个线程并发执行时存在共享数据集(临界资源)。
- 多个线程在共享数据集上的操作不是原子操作(不可拆分的一个操作)

#### 5.3、如何保证并发线程的安全性

Java 中的线程安全问题的主要关注点有 3 个：可见性，有序性，原子性；Java 内存模型（JMM）解决了可见性和有序性问题，而锁解决了原子性问题。

- 对共享方法,数据进行限制(阻塞)访问（例如加锁：syncronizd，Lock）:多线程在同步方法或同步代码块上排队执行。
- 基于 CAS(比较和交换)实现非阻塞同步（基于 CPU 硬件技术支持）
- 取消共享，每个线程一个对象实例（例如 threadlocal）

## 三、JUC(Java并发编程包)

### 1、Lock锁相关

#### 1.1、Synchrnoized

##### 1.1.1、定义

synchronized 是排它锁的一种实现，支持可重入性

##### 1.1.2、用法

1) 修饰方法：同步方法（锁为当前实例或 Class 对象）

2) 修饰代码块：同步代码块（代码块括号内配置的对象）

```java
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
```

##### 1.1.3、优化

为了减少获得锁和释放锁带来的性能消耗，JDK1.6 以后的锁一共有 4 种状态，级别从低到高依次是：无锁状态、偏向锁状态、轻量级锁状态和重量级锁状态，
这几个状态会随着竞争情况逐渐升级。

#### 1.2、Lock接口

##### 1.2.1、ReentrantLock可重入锁

```java
private int num = 10;

private final Lock lock = new ReentrantLock();
private static Lock staticLock = new ReentrantLock();

private static final String TICKET_OFFICE = "华山售票厅";

public void sale() {

    lock.lock();
    try {

        if (num > 0) {
            TimeUnit.SECONDS.sleep(1);
            log.info("卖出了第" + (num--) + "张票,还剩:" + num);
        }
    } catch (Exception e) {
        log.error("sale:{}", e.getMessage());
    } finally {
        lock.unlock();
    }
}

public void add() {

    lock.lock();
    try {
        if (num == 0) {

            TimeUnit.SECONDS.sleep(1);

            num += 20;
            log.info("增加了20张票:{}", Thread.currentThread().getName());
        }
    } catch (Exception e) {
        log.error("add:{}", e.getMessage());
    } finally {
        lock.unlock();
    }

}

public static synchronized void getName() {
    staticLock.lock();
    try {
        log.info("执行了add方法:{},{}", Thread.currentThread().getName(), TICKET_OFFICE);

        TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }finally {
        staticLock.unlock();
    }
}
```

##### 1.2.2、ReadWriteLock 读写锁

###### 1.2.2.1、特性

```java
/**
 * 独占锁（写锁） 一次只能被一个线程占有
 * 共享锁（读锁） 多个线程可以同时占有
 * ReadWriteLock
 * 读-读 可以共存！
 * 读-写 不能共存！
 * 写-写 不能共存！
 */
```

```java
ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
private final Map<String, Object> map = new HashMap<>();

public void put(String key, Object value) {
    readWriteLock.writeLock().lock();
    try {
        TimeUnit.SECONDS.sleep(1);
        map.put(key, value);
        log.info("添加了Key:{}", key);
    } catch (Exception e) {
        log.error("put:{}", e.getMessage());
        e.printStackTrace();
    } finally {
        readWriteLock.writeLock().unlock();
    }

}

public void get(String key) {
    readWriteLock.readLock().lock();
    try {
        log.info("读取了Key:{}", map.get(key));
    } catch (Exception e) {
        log.error("put:{}", e.getMessage());
        e.printStackTrace();
    } finally {
        readWriteLock.readLock().unlock();
    }
}
```

#### 1.3、Synchronized 和 Lock 区别

1、Synchronized 内置的Java关键字， Lock 是一个Java类 

2、Synchronized 无法判断获取锁的状态，Lock 可以判断是否获取到了锁

3、Synchronized 会自动释放锁，lock 必须要手动释放锁！如果不释放锁，**死锁**

4、Synchronized 线程 1（获得锁，阻塞）、线程2（等待，傻傻的等）；Lock锁就不一定会等待下

去；

5、Synchronized 可重入锁，不可以中断的，非公平；Lock ，可重入锁，可以 判断锁，非公平（可以

自己设置）；

6、Synchronized 适合锁少量的代码同步问题，Lock 适合锁大量的同步代码！

#### 1.4、Volatile

##### 1.4.1、定义

volatile 一般用于修饰属性变量

1) 保证共享变量的可见性.(尤其是多核或多 cpu 场景下)

2) 禁止指令的重排序操作（例如：count++底层会有三个步骤）

3) 不保证原子性（例如不能保证一个线程执行完 count++所有指令其它线程才能执行。）

##### 1.4.2、应用

1) 状态标记（boolean 类型属性）

2) 安全发布 (线程安全单例中的对象安全发布-双重检测机制)

3) 读写锁策略（一个写，并发读，类似读写锁）

```java
    // private  boolean flag = true;
    private volatile boolean flag = true;

    /**
     * while 有以下内容时flag是可见的
     * Thread.sleep,触发了线程的重新调度,保存当前线程上下文，即刷到主内存。
     * 存在synchronized,当获取锁以后,清空本地内存中共享变量，从主内存进行加载，在释放锁时将本地内存中共享变量刷新到主内存中。
     * System.out.println(); 中存在synchronized 同第二种;
     */

    public void run(){

        while (flag){
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//          log.info("run:{}:{}",LocalDateTime.now(),Thread.currentThread().getName());
        }
    }

    public void change(){
        flag = !flag;
        log.info("change:{}",Thread.currentThread().getName());
    }
```

### 2、并发集合类

```
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


// 线程安全的Map系集合
Map<String,String> concurrentHashMap = new ConcurrentHashMap<>();
```

### 3、线程同步类

#### 3.1、CountDownLatch

```java
public static void main(String[] args) throws Exception {

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(6,
            Runtime.getRuntime().availableProcessors(),
            3L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(3),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );
    // 设置计数器数
    CountDownLatch countDownLatch = new CountDownLatch(Runtime.getRuntime().availableProcessors());

    for (int i = 0; i <Runtime.getRuntime().availableProcessors() ; i++) {

        threadPoolExecutor.execute(()->{
            try {
                TimeUnit.SECONDS.sleep(new Random().nextInt(10));
            } catch (Exception e) {
                log.error("error:{}",e.getMessage());
            }
            log.info("{}:Go out",Thread.currentThread().getName());

            // 每次次执行计数器减一
            countDownLatch.countDown();
        });

    }

    // 等待计数器归零，然后再向下执行
    countDownLatch.await();

    log.info("Close Door");
    threadPoolExecutor.shutdown();

}
```

#### 3.2、CyclicBarrier 栅栏

```java
public static void main(String[] args) {

    AtomicBoolean flag = new AtomicBoolean(true);

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(12,
            Runtime.getRuntime().availableProcessors(),
            3L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(3),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );
    CyclicBarrier cyclicBarrier = new CyclicBarrier(7,()->{

        if (flag.get()) {
            log.info("神龙出现了");
            flag.set(false);
        } else {
            log.info("龙珠飞走了");
        }

    });

    for (int i = 0; i < 7; i++) {
        int finalI = i;
        threadPoolExecutor.execute(()->{

            try {
                TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                log.info("{}:收集了第{}颗龙珠",Thread.currentThread().getName(),finalI+1);
                cyclicBarrier.await();

                TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                log.info("{}:许下了愿望",Thread.currentThread().getName());
                cyclicBarrier.await();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    threadPoolExecutor.shutdown();
}
```

#### 3.3、Semaphore信号量

```java
public static void main(String[] args) {

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            6,
            6,
            3L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );
    Semaphore semaphore = new Semaphore(3);
    for (int i = 0; i < 6; i++) {
        threadPoolExecutor.execute(() -> {

                try {
                    semaphore.acquire();
                    log.info("{}:抢到车位",Thread.currentThread().getName());
                    TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                    log.info("{}:离开了车位",Thread.currentThread().getName());
                    semaphore.release();
                } catch (Exception e) {
                    log.error("main:{}",e.getMessage());
                }

            }
        );
    }
    threadPoolExecutor.shutdown();
```

#### 3.4、
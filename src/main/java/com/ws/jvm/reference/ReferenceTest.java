package com.ws.jvm.reference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author JunWu
 * 引用测试
 * <p>
 * 强引用 : 有引用就无法回收
 * 软引用:  当内存不足时GC回收
 * 弱引用:  只要触发了GC,就回收
 * 虚引用:  必须配合引用队列使用
 */
@Slf4j
public class ReferenceTest {

    public static List list = new ArrayList<>();

    @AllArgsConstructor
    @Data
    public static class Ws {
        private String name;

        public Ws(){}
        /**
         * 允许自救,只会生效一次
         *
         * @throws Throwable
         */
        @Override
        protected void finalize() throws Throwable {
            list.add(this);
        }
    }

    /**
     * 针对字符串常量池以及堆中的字符串 回收问题
     * <p>
     * String.intern() :
     * 当字符串常量池中存在该字符串时,返回常量池中的引用;
     * 否则将当前对象加入常量池(指向的内存地址也是这个对象的地址),返回该对象的引用;
     */
    @Test
    public void testStringPool() throws Exception {
        /**
         *   常量池中的字符串是不回收的;
         *   堆中的字符串回收;
         *   String str = new String("大宝");         // 被回收
         *   String str = "大" + new String("宝");    // 被回收
         *   String a   = "大宝", str = a + "宝";     // 被回收
         *   String str = "大" + "宝";                // 不回收,编译期被编译器优化成"大宝"
         */
        String a = "大";
        // 编译期间会变成 StringBuilder进行累加,最终toString;
        String str = a + "宝";
        String intern = str.intern();
        log.info("str == intern -> {} | a == a.intern() -> {}", str == intern, a == a.intern());
        WeakReference reference = new WeakReference(str);
        str = null;
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        Object o = reference.get();
        log.info("{}", o);

        String b = "Hello";
        String c = "World";
        // 在堆中创建了字符串对象,注意:常量池 并没有该字符串
        String d = b + c;
        // 将该字符串添加到常量池中
        String s = d.intern();
        String e = "HelloWorld";
        log.info(" d == s -> {} | e == s -> {}", d == s, e == s);

        // 常量池中 + 堆中 含有该字符串, 堆中的实际上指向的也是常量池中的地址;
        String f = new String("Shit");
        // false
        String intern1 = f.intern();
        log.info("{}", f == intern1);
    }

    /**
     * 测试软引用
     */
    @Test
    public void testSoft() {
        Ws ws = new Ws("bean");
        SoftReference<Ws> softReference = new SoftReference(ws);
        ws = null;
        System.gc();
        ws = softReference.get();
        log.info("{}", ws.name);
    }

    /**
     * 测试弱引用
     */
    @Test
    public void testWeak() throws Exception {
        Ws ws = new Ws("大宝");
        WeakReference<Ws> weakReference = new WeakReference<>(ws);
        ws = null;
        System.gc();
        ws = weakReference.get();
        log.info("{}", ws == null ? "已回收" : ws.name);

        Ws fromList = (Ws) list.remove(0);
        log.info("{}", fromList);
        fromList = null;
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        list.forEach(System.out::println);
    }

    /**
     * 使用引用队列 + 弱引用
     */
    @Test
    public void testReferenceQueueAndWeak() throws Exception {
        Ws ws = new Ws("大宝");
        // 创建引用队列
        ReferenceQueue<Ws> referenceQueue = new ReferenceQueue<>();
        // 创建虚引用
        WeakReference<Ws> reference = new WeakReference(ws, referenceQueue);
        ws = null;
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        // 是否回收
        boolean enqueued = reference.isEnqueued();
        Ws fromWeak = reference.get();
        Reference<? extends Ws> poll = referenceQueue.poll();
        log.info("是否回收[{}] |  fromWeak is null [{}] | [{}]", new Object[]{enqueued, fromWeak == null, reference == poll});
    }

    /**
     * 虚引用
     * <p>
     * 必须配合引用队列使用,因为只有一个构造器
     */
    @Test
    public void testPhantomReference() throws Exception {
        Ws ws = new Ws("大宝");
        ReferenceQueue<Ws> referenceQueue = new ReferenceQueue<>();
        PhantomReference<Ws> phantomReference = new PhantomReference(ws, referenceQueue);
        ws = null;
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        //都是null
        Ws allNull = phantomReference.get();
        boolean enqueued = phantomReference.isEnqueued();
        log.info("是否回收[{}]", enqueued);
    }

    /**
     * WeakHashMap
     */
    @Test
    public void testWeakHashMap() throws Exception {
        // WeakHashMap<String, Ws> weakHashMap = new WeakHashMap<>(); 经测试这样泛型回收失败!
        WeakHashMap<Ws, String> weakHashMap = new WeakHashMap<>();
        weakHashMap.put(new Ws("1"), "1");
        Ws ws = new Ws("2");
        weakHashMap.put(ws, "2");
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        weakHashMap.forEach((x, y) -> {
            log.info("K[{}] V[{}]", x, y);
        });
    }
}

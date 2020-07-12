package com.ws.jvm;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.management.*;

/**
 * @Description: 测试JDK自带 获取各种JVM数据的工厂类
 * @Author: JulyJunWu
 * @Date: 2020/7/11 20:44
 */
@Slf4j
public class TestManagementFactory {

    /**
     * 查询JDK 堆内存相关数据
     * 初始/最大/已使用/committed
     */
    @Test
    public void getMemoryInfo() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage usage = memoryMXBean.getHeapMemoryUsage();
        log.info("init heap:{}/M", usage.getInit() / 1024 / 1024);
        log.info("max heap:{}/M", usage.getMax() / 1024 / 1024);
        log.info("used heap:{}/M", usage.getUsed() / 1024 / 1024);
        log.info("committed:{}/M", usage.getCommitted() / 1024 / 1024);
    }

    /**
     * 查询堆外内存数据
     */
    @Test
    public void getNonHeapMemory() {
        MemoryUsage memoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        log.info("init NonHeap:{}/M", memoryUsage.getInit() / 1024 / 1024);
        log.info("max NonHeap:{}/M", memoryUsage.getMax() / 1024 / 1024);
        log.info("used NonHeap:{}/M", memoryUsage.getUsed() / 1024 / 1024);
        log.info("committed NonHeap:{}/M", memoryUsage.getCommitted() / 1024 / 1024);
    }

    /**
     * 获取JDK线程相关数据
     */
    @Test
    public void getThreadInfo() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] allThreadIds = threadMXBean.getAllThreadIds();
        for (long thread : allThreadIds) {
            log.info("threadId:{}", thread);
        }
        ThreadInfo[] threadInfo = threadMXBean.getThreadInfo(allThreadIds);
        for (ThreadInfo info : threadInfo) {
            StackTraceElement[] stackTrace = info.getStackTrace();
            //  打印线程的调用栈
            for (StackTraceElement stackTraceElement : stackTrace) {
                log.info("at {}", stackTraceElement.toString());
            }
        }

        //Map<String, Thread> threads = ThreadUtil.getThreads();

/*        for (Map.Entry<String,Thread> entry : threads.entrySet()){
            Thread thread = entry.getValue();
            long id = thread.getId();
            String name = thread.getName();
            long threadCpuTime = threadMXBean.getThreadCpuTime(id);
            ThreadInfo info = threadMXBean.getThreadInfo(id);
            StackTraceElement[] stackTrace = info.getStackTrace();
            //  打印线程的调用栈
            for (StackTraceElement stackTraceElement : stackTrace){
                log.info("at {}",stackTraceElement.toString());
            }
        }*/
    }
}

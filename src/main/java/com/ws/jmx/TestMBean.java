package com.ws.jmx;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * 测试JMX
 */
public class TestMBean {

    public static void main(String[] args) throws Exception {
        testWithAnnocation();
        testWithMxBean();
        TimeUnit.SECONDS.sleep(66688L);
    }

    /**
     * 测试以MXBean结尾
     *
     * @throws Exception
     */
    public static void testWithMxBean() throws Exception {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        // 注册到mbserver,就可以在jconsole上查看
        beanServer.registerMBean(new StudentImpl(), new ObjectName("com.ws.jmx:type=StudentImpl"));
    }

    /**
     * 测试以注解为主
     *
     * @throws Exception
     */
    public static void testWithAnnocation() throws Exception {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        // 注册到mbserver,就可以在jconsole上查看
        beanServer.registerMBean(new ShopImpl(), new ObjectName("com.ws.jmx:type=ShopImpl"));

    }

}

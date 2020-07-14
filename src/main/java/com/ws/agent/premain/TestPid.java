package com.ws.agent.premain;

import java.lang.management.ManagementFactory;
import java.util.concurrent.locks.LockSupport;

/**
 * @Description: 测试从Java代码中获取自身程序的PID
 * @Author: JulyJunWu
 * @Date: 2020/7/13 23:26
 */
public class TestPid {

    public static void main(String[] args) {

        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);

        long longPid = PidUtils.currentLongPid();
        String currentPid = PidUtils.currentPid();
        System.out.println(longPid);
        System.out.println(currentPid);

        LockSupport.park();
    }

}

package com.ws.agent;

import java.lang.instrument.Instrumentation;

/**
 * @Description: 测试PreMain
 * @Author: JulyJunWu
 * @Date: 2020/7/12 15:47
 */
public class PreMainAgent {
    /**
     * 在执行目标main方法之前执行(类似AOP)
     *
     * @param args
     * @param ins
     */
    public static void premain(String args, Instrumentation ins) {
        System.out.println("==========PreMainAgent start============");
        System.out.println(args);
        System.out.println("已加载class数量=" + ins.getAllLoadedClasses().length);
        System.out.println("==========PreMainAgent end============");
    }

    public static void premain(String args) {
        System.out.println("无Instrumentation调用这个, " + args);
    }

}

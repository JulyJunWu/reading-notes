package com.ws.agent;

/**
 * @Description: 测试agent类
 * @Author: JulyJunWu
 * @Date: 2020/7/12 15:50
 */
public class AgentTest {
    public static void main(String[] args) {
        System.out.println("===========AgentTest start===========");
        System.out.println("测试AgentTest");
        if (args != null) {
            for (String a : args) {
                System.out.println(a);
            }
        }
        System.out.println("===========AgentTest end===========");
    }
}

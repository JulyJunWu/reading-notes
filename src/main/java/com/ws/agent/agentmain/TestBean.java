package com.ws.agent.agentmain;

/**
 * 热更新几个注意点 :
 * 只能修改方法内部逻辑,其他不能动
 */
public class TestBean {

    public void sleep() {
        System.out.println(TestAgent.REDEFINE_CLASS_MAP.size());
    }

}

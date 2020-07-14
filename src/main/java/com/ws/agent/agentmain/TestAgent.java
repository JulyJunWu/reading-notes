package com.ws.agent.agentmain;

import com.sun.tools.attach.VirtualMachine;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestAgent {

    public static Map<String, Integer> REDEFINE_CLASS_MAP = new ConcurrentHashMap();

    public static void main(String[] args) throws Exception {
        System.out.println("TestAgent");
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new AgentTask(), 5L, 20L, TimeUnit.SECONDS);
        TestBean testBean = new TestBean();
        while (true) {
            TimeUnit.SECONDS.sleep(5L);
            testBean.sleep();
        }
    }


    public static class AgentTask implements Runnable {

        public void run() {
            VirtualMachine attach = null;
            try {
                String name = ManagementFactory.getRuntimeMXBean().getName();
                System.out.println(name);
                int index = name.indexOf("@");
                String pid = name.substring(0, index);
                attach = VirtualMachine.attach(pid);
                assert attach != null;
                attach.loadAgent("C:\\Users\\DELL\\Desktop\\agentmain.jar");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (attach != null) {
                    try {
                        attach.detach();
                        System.out.println("detach end");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}

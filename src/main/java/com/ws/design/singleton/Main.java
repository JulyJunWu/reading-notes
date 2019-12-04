package com.ws.design.singleton;

import org.junit.Test;

/**
 * @author Jun
 * data  2019-09-17 0:00
 */
public class Main {


    @Test
    public void one() {

        Singleton instance = Singleton.getInstance();

        Singleton instance2 = Singleton.getInstance();

        System.out.println(instance == instance2);
    }

}

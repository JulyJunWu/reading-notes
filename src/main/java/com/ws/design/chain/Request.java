package com.ws.design.chain;

/**
 * @author Jun
 * data  2019-09-26 20:19
 */
public class Request {

    private String name = "zws";

    private volatile int count = 0;


    public int getAndIncr() {
        return ++count;
    }

}

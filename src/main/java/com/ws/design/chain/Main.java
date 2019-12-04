package com.ws.design.chain;

/**
 * @author Jun
 * data  2019-09-26 20:28
 */
public class Main {

    public static void main(String[] args) {
        Request request = new Request();
        ApplicationChain applicationChain = new ApplicationChain();

        applicationChain.addHandler(new ConcreteInterceptorA());
        applicationChain.addHandler(new ConcreteInterceptorB());
        applicationChain.addHandler(new ConcreteInterceptorC());


        applicationChain.handleRequest(request);


    }

}

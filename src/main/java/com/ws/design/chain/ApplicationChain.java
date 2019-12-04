package com.ws.design.chain;

/**
 * @author Jun
 * data  2019-09-26 20:29
 */
public class ApplicationChain {

    private AbstracterHandleInterceptor tail;

    private AbstracterHandleInterceptor head;

    public void addHandler(AbstracterHandleInterceptor abstracterInterceptor) {

        if (tail == null) {
            head = tail = abstracterInterceptor;
            return;
        }

        tail.next = abstracterInterceptor;
        tail = abstracterInterceptor;
    }


    public void addFirst(AbstracterHandleInterceptor abstracterInterceptor) {

        if (head == null) {
            addHandler(abstracterInterceptor);
            return;
        }

        abstracterInterceptor.next = head;
        head = abstracterInterceptor;
    }

    public void addLast(AbstracterHandleInterceptor abstracterInterceptor) {
        addHandler(abstracterInterceptor);
    }

    public void handleRequest(Request request) {
        head.handle(request);
    }
}

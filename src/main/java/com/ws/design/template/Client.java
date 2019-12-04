package com.ws.design.template;

/**
 * @author Jun
 * data  2019-09-23 23:38
 */
public class Client {

    private AbstractProcess abstractProcess;

    public Client(AbstractProcess abstractProcess) {
        this.abstractProcess = abstractProcess;
    }

    ;

    public void run() {
        abstractProcess.process();
    }

    public void setAbstractProcess(AbstractProcess abstractProcess) {
        this.abstractProcess = abstractProcess;
    }
}

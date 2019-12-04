package com.ws.jvm.classloader;

/**
 * @author Jun
 * data  2019-08-09 22:25
 */
public class Test7 {

    public static void main(String[] args) {

        Test7Plus test7Plus = new Test7Plus();
        
        System.out.println(System.getProperty("sun.boot.class.path"));

        System.out.println(System.getProperty("java.ext.dirs"));

        System.out.println(System.getProperty("java.class.path"));
    }

}

class Test7Plus {

    int a = 10;

    {
        System.out.println(a);
    }

    public Test7Plus() {
        System.out.println(" hello" + a);
    }


}

package com.ws.book.深入理解java虚拟机;

/**
 * @author Jun
 * data  2019-10-04 20:36
 */
public class Test {

    public static void main(String[] args) {
        try {
            String s = "a";
            s = s + "b" + "c";
            s = s + "b" + "c" + "d";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(66);
        }

        Man man = new Man();

        Human.RUNNABLE.run();

    }

    public int inc() {
        int x;
        try {
            x = 1;
            return x;
        } catch (Exception e) {
            x = 2;
            return x;
        } finally {
            x = 3;
        }
    }


}

class Man implements Human{

    static {
        System.out.println("man");
    }


}

interface Human{

    public static Runnable RUNNABLE = new Runnable() {
        {
            System.out.println("Human");
        }

        @Override
        public void run() {

        }
    };


}

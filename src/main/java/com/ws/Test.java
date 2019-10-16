package com.ws;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: QiuJunWu
 * @Date: 2019/10/11 0011 13:02
 * @Copyright: Fujian Linewell Software Co., Ltd. All rights reserved.
 */
public class Test {

    public static void main(String[] args) throws Exception{

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("完事了");
                }catch (Exception e){

                }

            }
        });

        thread.start();
        System.out.println(6666);
        thread.join();
        System.out.println(88888);


    }

}

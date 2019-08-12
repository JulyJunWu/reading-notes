package com.ws.framework.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: QiuJunWu
 * @Date: 2019/8/12 0012 11:18
 * @Copyright: Fujian Linewell Software Co., Ltd. All rights reserved.
 */
public class ZkDemo {

    public static void main(String[] args) throws Exception {

        ZkClient client = new ZkClient("server-1:2181,server-2:2181,server-3:2181");

        //client.createPersistent("/ZWS");

        client.createEphemeralSequential("/ZWS/server-1", "HELLO WORLD");
        client.createEphemeralSequential("/ZWS/server-2", "HELLO WORLD");
        client.createEphemeralSequential("/ZWS/server-3", "HELLO WORLD");

        //这边有个坑,如果是在zkCli.sh命令行改动数据则watch不会生效!
        client.subscribeDataChanges("/ZWS", new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("目录 -> " + dataPath + "  数据发生改变了 -> " + data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("数据删除了");
            }
        });

        TimeUnit.SECONDS.sleep(1);
        client.writeData("/ZWS","Hello world");
        TimeUnit.SECONDS.sleep(2);
        client.writeData("/ZWS","what`s your problem ?");

        TimeUnit.SECONDS.sleep(100);

        client.close();


    }


}

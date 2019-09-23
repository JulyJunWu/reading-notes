package com.ws.framework.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Date: 2019/8/12 0012 11:18
 * <p>
 * zk cli 命令 :
 * ls get create delete
 */
public class ZkDemo {

    public static ZooKeeper zooKeeper;
    public static final int EXISTS_TYPE = 1 << 0;
    public static final int DATA_CHANGE_TYPE = 1 << 1;
    public static final int CHILDREN_CHANGE = 1 << 2;

    @Before
    public void init() throws Exception {
        zooKeeper = new ZooKeeper("server-1:2181,server-2:2181,server-3:2181", 30000, p -> {
        });
    }

    public static Watcher getWatcher(int listenerType) {
        return watchedEvent -> {
            Watcher.Event.EventType type = watchedEvent.getType();
            switch (type) {
                case NodeChildrenChanged:
                    System.out.println("子节点发生变动");
                    break;
                case NodeDataChanged:
                    System.out.println("数据发生改变");
                    break;
                case NodeCreated:
                    System.out.println("节点增加");
                    break;
                case NodeDeleted:
                    System.out.println("节点删除");
                    break;
                case None:
                    System.out.println("无");
                    break;
            }

            if (listenerType == EXISTS_TYPE) {
                listenerExists();
            }

            if (listenerType == DATA_CHANGE_TYPE) {
                listenerDataChange();
            }

            if (listenerType == CHILDREN_CHANGE) {
                listenerChildrenChange();
            }

        };
    }

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
        client.writeData("/ZWS", "Hello world");
        TimeUnit.SECONDS.sleep(2);
        client.writeData("/ZWS", "what`s your problem ?");

        TimeUnit.SECONDS.sleep(100);

        client.close();

    }

    @Test
    public void create() throws Exception {
        String s = zooKeeper.create("/qjw", "路漫漫其修远兮".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(s);
    }

    @Test
    public void getData() throws Exception {
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/qjw", false, stat);

        System.out.println(new String(data));
    }

    /**
     * 监听事件
     *
     * @throws Exception
     */
    @Test
    public void listener() throws Exception {

        listenerExists();
        listenerDataChange();
        listenerChildrenChange();
        TimeUnit.SECONDS.sleep(10000);
    }

    /**
     * 重复 监听目录是否存在
     *
     * @return
     */
    public static boolean listenerExists() {
        Stat exists = null;
        try {
            exists = zooKeeper.exists("/qjw", getWatcher(EXISTS_TYPE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists != null;
    }

    /**
     * 重复 监听目录的数据变动情况
     */
    public static void listenerDataChange() {
        try {
            Stat stat = new Stat();
            byte[] data = zooKeeper.getData("/qjw", getWatcher(DATA_CHANGE_TYPE), stat);
            System.out.println(new String(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重复 监听子节点数量变动
     */
    public static void listenerChildrenChange() {
        try {
            List<String> children = zooKeeper.getChildren("/qjw", getWatcher(CHILDREN_CHANGE));
            children.stream().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

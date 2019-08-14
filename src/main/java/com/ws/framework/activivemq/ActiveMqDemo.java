package com.ws.framework.activivemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Date: 2019/8/14 0014 12:47
 * demo
 *
 * 类别        点对点                   发布/订阅 API差别
 * 队列     Destination                     Topic
 * 消费     receive接受返回结果处理            Listener监听器处理
 *
 */
public class ActiveMqDemo {

    public static Connection connection;

    static {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    ActiveMQConnectionFactory.DEFAULT_USER,
                    ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                    "tcp://server-1:61616");
            connection = connectionFactory.createConnection();
            connection.start();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
    }

    /**
     * 点对点发布
     *
     * @throws Exception
     */
    @Test
    public void p2pProducer() throws Exception {
        //param1: 是否使用事务
        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        //指定一个队列 往里面写数据或者读数据
        Destination destination = session.createQueue("MessageQueue");

        MessageProducer producer = session.createProducer(destination);
        //设置是否持久化??
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        ObjectMessage objectMessage = session.createObjectMessage("Hello,this is my first active MQ");

        producer.send(objectMessage);

        session.commit();

        session.close();

        connection.close();
    }

    /**
     * 点对点消费 同一条数据只有一个消费者能消费
     *
     * @throws Exception
     */
    @Test
    public void p2pConsumer() throws Exception {
        //创建会话
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        //需要消费的队列
        Destination destination = session.createQueue("MessageQueue");
        //创建消费者
        MessageConsumer consumer = session.createConsumer(destination);

        //消费数据
        while (true) {
            ObjectMessage receive = (ObjectMessage) consumer.receive(10000);
            if (receive != null) {
                System.out.println((String) receive.getObject());
            } else {
                break;
            }
        }

        //关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

    /**
     * 发布/订阅
     */
    @Test
    public void psProducer() throws Exception {

        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic("Message-Topic");

        MessageProducer producer = session.createProducer(topic);

        TextMessage textMessage = session.createTextMessage("This is my first Message-Topic");

        producer.send(textMessage);

        session.commit();

        session.close();
        connection.close();
    }

    /**
     * 订阅消息
     */
    @Test
    public void psConsumer() throws Exception {

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic("Message-Topic");

        MessageConsumer consumer = session.createConsumer(topic);

        //消息处理
        consumer.setMessageListener(message -> {
            try {
                System.out.println(message);
                if (message instanceof TextMessage) {
                    System.out.println(((TextMessage) message).getText());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        TimeUnit.SECONDS.sleep(11111);

        consumer.close();
        session.close();
        connection.close();
    }


}

package com.dazong.mq.activemq;

import com.dazong.mq.core.consumer.activemq.ActiveMQListener;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author huqichao
 * @create 2017-11-06 09:18
 **/
public class TestVirtualTopicConsumer {

    public static void main(String[] args) throws JMSException, InterruptedException {
        // 连接到ActiveMQ服务器
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://172.16.21.13:61616");
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

        for (int i =0 ;i<4;i++){
            Queue topic = session.createQueue("Consumer."+i+".VirtualTopic.test11");
            MessageConsumer consumer = session.createConsumer(topic);
            consumer.setMessageListener(new ActiveMQListener(null, null, null, null));
        }

        // 创建主题
//        Queue topicA = session.createQueue("Consumer.A.VirtualTopic.test11");
//        Queue topicB = session.createQueue("Consumer.B.VirtualTopic.test11");
        // 消费者A组创建订阅
//        MessageConsumer consumerA1 = session.createConsumer(topicA);

//        MessageConsumer consumerA2 = session.createConsumer(topicA);

        //消费者B组创建订阅
//        MessageConsumer consumerB1 = session.createConsumer(topicB);

//        MessageConsumer consumerB2 = session.createConsumer(topicB);


//        consumerA1.setMessageListener(new ActiveMQListener(null, null, null));

//        consumerA1.setMessageListener(new MessageListener() {
//            // 订阅接收方法
//            public void onMessage(Message message) {
//                TextMessage tm = (TextMessage) message;
//                try {
//                    System.out.println("Received message A1: " + tm.getText()+":"+tm.getStringProperty("property"));
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        consumerA2.setMessageListener(new ActiveMQListener(null, null, null));

//        consumerA2.setMessageListener(new MessageListener() {
//            // 订阅接收方法
//            public void onMessage(Message message) {
//                TextMessage tm = (TextMessage) message;
//                try {
//                    System.out.println("Received message A2: " + tm.getText()+":"+tm.getStringProperty("property"));
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        consumerB1.setMessageListener(new ActiveMQListener(null, null, null));

//        consumerB1.setMessageListener(new MessageListener() {
//            // 订阅接收方法
//            public void onMessage(Message message) {
//                TextMessage tm = (TextMessage) message;
//                try {
//                    System.out.println("Received message B1: " + tm.getText()+":"+tm.getStringProperty("property"));
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        consumerB2.setMessageListener(new ActiveMQListener(null, null, null));

//        consumerB2.setMessageListener(new MessageListener() {
//            // 订阅接收方法
//            public void onMessage(Message message) {
//                TextMessage tm = (TextMessage) message;
//                try {
//                    System.out.println("Received message B2: " + tm.getText()+":"+tm.getStringProperty("property"));
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        session.close();
//        connection.close();
    }


    private static class MyMessageListener implements MessageListener {
        private String name;
        public MyMessageListener(String name){
            this.name = name;
        }

        @Override
        public void onMessage(Message message) {
            TextMessage tm = (TextMessage) message;
            try {
                System.out.println("Received message " +name+ ": " + tm.getText()+":"+tm.getStringProperty("property"));
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}

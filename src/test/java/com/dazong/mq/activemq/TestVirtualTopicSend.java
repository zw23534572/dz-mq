package com.dazong.mq.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author huqichao
 * @create 2017-11-06 09:16
 **/
public class TestVirtualTopicSend {
    public static void main(String[] args) throws JMSException {
        // 连接到ActiveMQ服务器
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://172.16.21.13:61616");
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        // 创建主题
        Topic topic = session.createTopic("VirtualTopic.test11");
        MessageProducer producer = session.createProducer(topic);
        // NON_PERSISTENT 非持久化 PERSISTENT 持久化,发送消息时用使用持久模式
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        TextMessage message = session.createTextMessage();
        message.setText("topic 消息。" + System.currentTimeMillis());
        message.setStringProperty("property", "消息Property");
        // 发布主题消息
        producer.send(message);
        System.out.println("Sent message: " + message.getText());
        session.close();
        connection.close();
    }
}

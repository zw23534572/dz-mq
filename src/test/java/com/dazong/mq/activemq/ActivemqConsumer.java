package com.dazong.mq.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.qpid.jms.JmsConnectionFactory;

import javax.jms.*;

public class ActivemqConsumer {

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://master:61616");
//        JmsConnectionFactory connectionFactory = new JmsConnectionFactory("amqp://master:5672");

        // Create a Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();

        connection.setExceptionListener(new ExceptionListener() {
            @Override
            public void onException(JMSException e) {
                System.out.println("JMS Exception occured.  Shutting down client.");
            }
        });

        // Create a Session
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        Destination destination = session.createQueue("test11");

        // Create a MessageConsumer from the Session to the Topic or Queue
        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = null;
                    try {
                        text = textMessage.getText();
                        System.out.println("111Received: " + text + "----" + message.getJMSMessageID());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        System.out.println("2222Received: " + message + "====" + message.getJMSMessageID());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
//                try {
//                    message.acknowledge();
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
            }
        });
//        consumer.close();
//        session.close();
//        connection.close();
    }

}

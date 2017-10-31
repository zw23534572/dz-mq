package com.dazong.mq.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Recv {

    private final static String QUEUE_NAME = "hello";

    private static Map<String, IListener> listenerMap = new HashMap<>();
    static {
        listenerMap.put("hello", new PayListener());
    }

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.16.22.38");
        factory.setUsername("admin");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                System.out.println("consumerTag:"+consumerTag);
                System.out.println("properties:"+properties.getMessageId());
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                if (message.equals("pay")){
                    listenerMap.get(QUEUE_NAME).process(message);
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, false, consumer);

    }

}

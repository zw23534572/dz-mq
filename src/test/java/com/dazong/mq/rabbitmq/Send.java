package com.dazong.mq.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.16.22.38");
        factory.setUsername("admin");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

//        channel.exchangeDeclare();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

//        SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("replyCode: " + replyCode + " replyText: " + replyText);
            }
        });

        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("ok========" + deliveryTag + "--" + multiple);
            }

            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("fail========" + deliveryTag+ "--" + multiple);
            }
        });



        String message = "Hello World!－－－" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties().builder().messageId("pay");

        long nextSeqNo = channel.getNextPublishSeqNo();
        channel.basicPublish("", QUEUE_NAME, true, builder.build(), message.getBytes("UTF-8"));
//        confirmSet.add(nextSeqNo);
        System.out.println(" ["+nextSeqNo+"] Sent '" + message + "'");
        channel.waitForConfirmsOrDie();



//        if (channel.waitForConfirms(1000)){
//            System.out.println("ok----");
//        } else {
//            System.out.println("fail----");
//        }
//
//        channel.close();
//        connection.close();
    }
}

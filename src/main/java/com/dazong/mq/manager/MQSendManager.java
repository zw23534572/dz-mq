package com.dazong.mq.manager;

import com.alibaba.fastjson.JSON;
import com.dazong.mq.constant.Constants;
import com.dazong.mq.dao.mapper.MQMessageMapper;
import com.dazong.mq.domian.DZMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.jms.*;

/**
 * @author huqichao
 * @create 2017-10-31 09:47
 **/
@Component
public class MQSendManager {

    private Logger logger = LoggerFactory.getLogger(MQSendManager.class);

    @Autowired
    private MQMessageMapper messageMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Async
    public void send(final DZMessage message){
        try {
            logger.debug("发送消息------>{}", message);
            jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
            if (message.isQueue()){
                ActiveMQQueue queue = new ActiveMQQueue(message.getDestination());
                jmsTemplate.convertAndSend(queue, JSON.toJSONString(message));
            } else {
                ActiveMQTopic topic = new ActiveMQTopic(Constants.TOPIC_PREFIX + message.getDestination());
                jmsTemplate.convertAndSend(topic, JSON.toJSONString(message));
            }
            message.setStatus(DZMessage.STATUS_DONE);
            messageMapper.updateMessage(message);
        } catch (Exception e) {
            logger.error("send", e);
            logger.error("发送消息失败, eventId:{}, 原因:{}", message.getEventId(), e.getCause().getMessage());
        }
    }
}

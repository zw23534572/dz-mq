package com.dazong.mq.manager;

import com.alibaba.fastjson.JSON;
import com.dazong.mq.dao.mapper.MQMessageMapper;
import com.dazong.mq.domian.DZMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.core.ProducerCallback;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import java.util.List;

/**
 * @author huqichao
 * @create 2017-10-31 09:47
 **/
@Configuration
@EnableScheduling
public class MQSendManager {

    private Logger logger = LoggerFactory.getLogger(MQSendManager.class);

    @Autowired
    private MQMessageMapper messageMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Async
    public void send(final DZMessage message){
        try {
            logger.debug("发送消息------>topic:{}, eventId:{}, body:{}", message.getTopic(), message.getEventId(), message.getBody());
            jmsTemplate.convertAndSend(message.getTopic(), JSON.toJSONString(message));
            message.setStatus(DZMessage.STATUS_已处理);
            messageMapper.updateMessage(message);
        } catch (Exception e) {
            logger.error("发送消息失败, eventId:{}, 原因:{}", message.getEventId(), e.getCause().getMessage());
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void sendJob(){
        List<DZMessage> messageList = messageMapper.queryMessageByStatus(DZMessage.STATUS_未处理, 50);
        logger.debug("定时重发未发送成功的消息：{}", messageList.size());
        for (DZMessage message : messageList){
            send(message);
        }
    }
}

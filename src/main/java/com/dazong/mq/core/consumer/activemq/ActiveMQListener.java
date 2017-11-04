package com.dazong.mq.core.consumer.activemq;

import com.alibaba.fastjson.JSON;
import com.dazong.mq.core.consumer.IMessageListener;
import com.dazong.mq.dao.mapper.MQMessageMapper;
import com.dazong.mq.domian.DZConsumerMessage;
import com.dazong.mq.domian.DZMessage;
import com.dazong.mq.manager.MQNotifyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author huqichao
 * @create 2017-11-02 13:46
 **/
public class ActiveMQListener implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(ActiveMQListener.class);

    private MQMessageMapper messageMapper;

    private MQNotifyManager notifyManager;

    private IMessageListener listener;

    public ActiveMQListener(MQMessageMapper messageMapper, MQNotifyManager notifyManager, IMessageListener listener){
        this.messageMapper = messageMapper;
        this.notifyManager = notifyManager;
        this.listener = listener;
    }

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            DZMessage dzMessage = JSON.parseObject(textMessage.getText(), DZMessage.class);
            DZConsumerMessage consumerMessage = new DZConsumerMessage(dzMessage);

            messageMapper.insertConsumerMessage(consumerMessage);
            notifyManager.notifyMessage(listener, consumerMessage);
        } catch (JMSException e) {
            logger.error("接收消息失败", e);
        }
    }
}

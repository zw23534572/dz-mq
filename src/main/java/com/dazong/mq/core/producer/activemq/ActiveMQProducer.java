package com.dazong.mq.core.producer.activemq;

import com.dazong.mq.dao.mapper.MQMessageMapper;
import com.dazong.mq.domian.DZMessage;
import com.dazong.mq.core.producer.AbstractProducer;
import com.dazong.mq.manager.MQSendManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * @author huqichao
 * @create 2017-10-30 15:32
 **/
@Service
public class ActiveMQProducer extends AbstractProducer {

    @Autowired
    private MQMessageMapper messageMapper;

    @Autowired
    private MQSendManager sendManager;

    @Override
    public void sendMessage(DZMessage message) throws Exception {
        Assert.notNull(message, "消息体不能空");
        Assert.notNull(message.getBody(), "消息内容不能空");
        Assert.notNull(message.getTopic(), "消息主题不能空");
        message.setEventId(UUID.randomUUID().toString());
        message.setStatus(DZMessage.STATUS_DOING);
        messageMapper.insertMessage(message);

        sendManager.send(message);
    }
}

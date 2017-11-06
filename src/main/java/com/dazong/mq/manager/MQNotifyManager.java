package com.dazong.mq.manager;

import com.dazong.mq.annotation.Subscribe;
import com.dazong.mq.core.consumer.IMessageListener;
import com.dazong.mq.dao.mapper.MQMessageMapper;
import com.dazong.mq.domian.DZConsumerMessage;
import com.dazong.mq.domian.DZMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author huqichao
 * @create 2017-10-31 09:47
 **/
@Component
public class MQNotifyManager {

    private Logger logger = LoggerFactory.getLogger(MQNotifyManager.class);

    @Autowired
    private MQMessageMapper messageMapper;

    private Map<Subscribe, IMessageListener> listenerMap = new HashMap<>();

    @Async
    public void notifyMessage(final DZConsumerMessage message){
        try {
            logger.debug("接收消息------>{}", message);
            IMessageListener listener = null;
            for (Map.Entry<Subscribe, IMessageListener> entry : listenerMap.entrySet()){
                if (entry.getKey().topic().equals(message.getTopic()) && entry.getKey().name().equals(message.getName())){
                    listener = entry.getValue();
                    break;
                }
            }
            if (listener == null){
                logger.warn("没有 Listener 监听 {} 消息", message.getTopic());
                message.setStatus(DZMessage.STATUS_DONE);
                messageMapper.updateConsumerMessage(message);
                return;
            }

            listener.receive(message.getBody());

            message.setStatus(DZMessage.STATUS_DONE);
            messageMapper.updateConsumerMessage(message);
        } catch (Exception e) {
            logger.error("接收消息失败, eventId:{}, 原因:{}", message.getEventId(), e.getCause().getMessage());
        }
    }

    @Async
    public void notifyMessage(final IMessageListener listener, final DZConsumerMessage message){
        try {
            logger.debug("接收消息------>{}", message);
            listener.receive(message.getBody());
            message.setStatus(DZMessage.STATUS_DONE);
            messageMapper.updateConsumerMessage(message);
        } catch (Exception e) {
            logger.error("接收消息失败, eventId:{}, 原因:{}", message.getEventId(), e.getCause().getMessage());
        }
    }

    public void registerListener(Subscribe subscribe, IMessageListener listener) {
        listenerMap.put(subscribe, listener);
    }

    public Map<Subscribe, IMessageListener> getListenerMap() {
        return listenerMap;
    }
}

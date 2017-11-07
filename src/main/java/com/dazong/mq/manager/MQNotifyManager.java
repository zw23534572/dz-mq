package com.dazong.mq.manager;

import com.dazong.mq.core.consumer.IMessageListener;
import com.dazong.mq.dao.mapper.MQMessageMapper;
import com.dazong.mq.domian.Consumer;
import com.dazong.mq.domian.DZConsumerMessage;
import com.dazong.mq.domian.DZMessage;
import com.dazong.mq.exception.MQException;
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

    private Map<Consumer, IMessageListener> listenerMap = new HashMap<>();

    @Async
    public void notifyMessage(final DZConsumerMessage message){
        try {
            logger.debug("接收消息------>{}", message);
            IMessageListener listener = null;
            for (Map.Entry<Consumer, IMessageListener> entry : listenerMap.entrySet()){
                //消息为队列时，则只判断消息目标。消息为topic时，判断消息目标和名称
                if (message.isQueue()){
                    if (entry.getKey().getDestination().equals(message.getDestination())){
                        listener = entry.getValue();
                        break;
                    }
                } else if (entry.getKey().getDestination().equals(message.getDestination()) && entry.getKey().getName().equals(message.getName())){
                    listener = entry.getValue();
                    break;
                }

            }
            if (listener == null){
                logger.warn("没有 Listener 监听 {} 消息", message);
                messageMapper.updateStatusById(message.getId(), DZMessage.STATUS_DONE);
                return;
            }

            listener.receive(message.copy(messageMapper));
        } catch (Exception e) {
            logger.error("接收消息失败, eventId:{}, 原因:{}", message.getEventId(), e.getCause().getMessage());
        } finally {
            messageMapper.updateConsumerMessage(message);
        }
    }

    @Async
    public void notifyMessage(final IMessageListener listener, final DZConsumerMessage message){
        try {
            logger.debug("接收消息------>{}", message);
            listener.receive(message.copy(messageMapper));
        } catch (Exception e) {
            logger.error("接收消息失败, eventId:{}, 原因:{}", message.getEventId(), e.getCause().getMessage());
        } finally {
            messageMapper.updateConsumerMessage(message);
        }
    }

    public void registerListener(Consumer consumer, IMessageListener listener) {
        for (Map.Entry<Consumer, IMessageListener> entry : listenerMap.entrySet()){
            if (consumer.getName().equals(entry.getKey().getName())){
                throw new MQException("Consumer name must be unique: [%s],[%s]", entry.getValue().getClass().getName(), listener.getClass().getName());
            }
        }
        listenerMap.put(consumer, listener);
    }

    public Map<Consumer, IMessageListener> getListenerMap() {
        return listenerMap;
    }
}

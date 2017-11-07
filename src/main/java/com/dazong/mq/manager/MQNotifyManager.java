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
                if (entry.getKey().queue().length() > 0 && entry.getKey().queue().equals(message.getDestination())){
                    listener = entry.getValue();
                    break;
                } else if (entry.getKey().topic().length() > 0 && entry.getKey().topic().equals(message.getDestination()) && entry.getKey().name().equals(message.getName())){
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

    public void registerListener(Subscribe subscribe, IMessageListener listener) {
        listenerMap.put(subscribe, listener);
    }

    public Map<Subscribe, IMessageListener> getListenerMap() {
        return listenerMap;
    }
}

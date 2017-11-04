package com.dazong.mq.core.consumer.activemq;

import com.dazong.mq.annotation.Subscribe;
import com.dazong.mq.constant.Constants;
import com.dazong.mq.core.consumer.AbstractConsumer;
import com.dazong.mq.core.consumer.IMessageListener;
import com.dazong.mq.dao.mapper.MQMessageMapper;
import com.dazong.mq.manager.MQNotifyManager;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import java.util.Map;

/**
 * @author huqichao
 * @create 2017-11-03 09:32
 **/
public class ActiveMQConumer extends AbstractConsumer {

    private JmsTemplate jmsTemplate;

    private MQNotifyManager notifyManager;

    private MQMessageMapper messageMapper;

    public ActiveMQConumer(JmsTemplate jmsTemplate, MQNotifyManager notifyManager, MQMessageMapper messageMapper){
        this.jmsTemplate = jmsTemplate;
        this.notifyManager = notifyManager;
        this.messageMapper = messageMapper;
    }

    public void init() throws Exception {
        Map<Subscribe, IMessageListener> listenerMap = notifyManager.getListenerMap();
        for (Map.Entry<Subscribe, IMessageListener> entry : listenerMap.entrySet()) {
            Subscribe subscribe = entry.getKey();
            if (subscribe.type().equals(Constants.TYPE_ACTIVEMQ)){
                Connection connection = jmsTemplate.getConnectionFactory().createConnection();
                connection.setExceptionListener(new ExceptionListener() {
                    @Override
                    public void onException(JMSException e) {
                        logger.error("JMS Exception occured.  Shutting down client.", e);
                    }
                });
                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = session.createQueue(subscribe.name() + "." + Constants.TOPIC_PREFIX + subscribe.topic());
                MessageConsumer consumer = session.createConsumer(destination);
                consumer.setMessageListener(new ActiveMQListener(messageMapper, notifyManager, entry.getValue()));
                connection.start();
            }
        }
    }
}

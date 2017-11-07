package com.dazong.mq.domian;

import com.dazong.mq.dao.mapper.MQMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huqichao
 * @create 2017-11-06 15:30
 **/
public class Message {

    private Logger logger = LoggerFactory.getLogger(Message.class);

    protected MQMessageMapper messageMapper;

    protected Long id;

    protected String body;


    public String getBody(){
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void acknowledge(){
        messageMapper.updateStatusById(id, DZConsumerMessage.STATUS_DONE);
        logger.debug("ack event id: {}", id);
    }
}

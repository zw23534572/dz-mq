package com.dazong.mq.domian;

import com.dazong.mq.dao.mapper.MQMessageMapper;

/**
 * @author huqichao
 * @create 2017-11-06 15:30
 **/
public class Message {

    protected MQMessageMapper messageMapper;

    protected Long id;

    protected String body;


    public String getBody(){
        return this.body;
    }

    public void acknowledge(){
        messageMapper.updateStatusById(id, DZConsumerMessage.STATUS_DONE);
    }
}

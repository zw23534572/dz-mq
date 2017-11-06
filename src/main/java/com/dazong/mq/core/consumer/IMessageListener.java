package com.dazong.mq.core.consumer;

import com.dazong.mq.domian.Message;

/**
 * @author huqichao
 * @create 2017-11-02 14:04
 **/
public interface IMessageListener {

    void receive(Message message);
}

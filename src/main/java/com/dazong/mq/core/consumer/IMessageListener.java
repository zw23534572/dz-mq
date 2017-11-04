package com.dazong.mq.core.consumer;

/**
 * @author huqichao
 * @create 2017-11-02 14:04
 **/
public interface IMessageListener {

    void receive(String message);
}

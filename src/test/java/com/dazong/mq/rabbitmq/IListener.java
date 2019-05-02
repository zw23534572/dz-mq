package com.dazong.mq.rabbitmq;

public interface IListener {

    void process(String message);
}

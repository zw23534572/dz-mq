package com.dazong.mq.constant;

/**
 * @author huqichao
 * @create 2017-11-06 14:27
 **/
public enum SubscribeType {

    ACTIVEMQ("activemq");

    private String type;
    private SubscribeType(String type){
        this.type = type;
    }
}

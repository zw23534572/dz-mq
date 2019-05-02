package com.dazong.mq.exception;

/**
 * 消息组件异常
 */
public class MQException extends RuntimeException {

    public MQException(String message, Object... objs){
        super(String.format(message, objs));
    }

    public MQException(Throwable cause, String message, Object... objs) {
        super(String.format(message, objs), cause);
    }
}

package com.dazong.mq.domian;

import com.alibaba.fastjson.annotation.JSONField;
import com.dazong.mq.dao.mapper.MQMessageMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author huqichao
 * @create 2017-10-30 15:58
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper=true)
public class DZConsumerMessage {

    public static final int STATUS_DOING = 0;
    public static final int STATUS_DONE = 1;

    @JSONField(serialize = false)
    protected Long id;

    protected String name;

    protected String eventId;

    protected String groupId;

    protected String body;

    @JSONField(serialize = false)
    protected int status;

    @JSONField(serialize = false)
    protected String topic;

    /**接收到发送消息的时间*/
    protected Long sendTime;

    /**接收到消息的时间*/
    protected Long receiveTime;

    protected Integer notifyCount;

    protected Date lastNotifyTime;

    public DZConsumerMessage(){}

    public DZConsumerMessage(DZMessage message){
        this.topic = message.getTopic();
        this.body = message.getBody();
        this.eventId = message.getEventId();
        this.groupId = message.getGroupId();
        this.sendTime = message.getSendTime();
        this.notifyCount = 0;
        this.receiveTime = System.currentTimeMillis();
    }

    public Message copy(MQMessageMapper messageMapper){
        Message message = new Message();
        message.id = id;
        message.body = body;
        message.messageMapper = messageMapper;

        return message;
    }
}

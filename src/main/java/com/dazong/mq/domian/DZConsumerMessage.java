package com.dazong.mq.domian;

import com.alibaba.fastjson.annotation.JSONField;
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
    private Long id;

    private String name;

    private String eventId;

    private String groupId;

    private String body;

    @JSONField(serialize = false)
    private int status;

    @JSONField(serialize = false)
    private String topic;

    /**接收到发送消息的时间*/
    private Long sendTime;

    /**接收到消息的时间*/
    private Long receiveTime;

    private Integer notifyCount;

    private Date lastNotifyTime;

    public DZConsumerMessage(){}

    public DZConsumerMessage(DZMessage message){
        this.topic = message.getTopic();
        this.body = message.getBody();
        this.eventId = message.getEventId();
        this.groupId = message.getGroupId();
        this.sendTime = message.getSendTime();
        this.notifyCount = 1;
        this.lastNotifyTime = new Date();
        this.receiveTime = System.currentTimeMillis();
    }
}

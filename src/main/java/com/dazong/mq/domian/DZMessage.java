package com.dazong.mq.domian;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author huqichao
 * @create 2017-10-30 15:58
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper=true)
public class DZMessage {

    public static final int STATUS_DOING = 0;
    public static final int STATUS_DONE = 1;

    @JSONField(serialize = false)
    private Long id;

    private String eventId;

    private String groupId;

    private boolean immediate;

    private String body;

    @JSONField(serialize = false)
    private int status;

    @JSONField(serialize = false)
    private String topic;

    /**接收到发送消息的时间*/
    private Long sendTime;

    public DZMessage(){}

    public DZMessage(String topic, String body){
        this.topic = topic;
        this.body = body;
    }

    public DZMessage(String topic, String body, String groupId){
        this.topic = topic;
        this.body = body;
        this.groupId = groupId;
    }

    public static DZMessage wrap(String topic, String body) {
        return new DZMessage(topic, body);
    }

    public static DZMessage wrap(String topic, String body, String groupId) {
        return new DZMessage(topic, body, groupId);
    }
}

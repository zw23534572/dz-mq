package com.dazong.mq.domian;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author huqichao
 * @create 2017-10-30 15:58
 **/
@Data
public class DZMessage {

    public static final int STATUS_未处理 = 0;
    public static final int STATUS_已处理 = 1;


    @JSONField(serialize = false)
    private Long id;

    private String eventId;

    private String body;

    @JSONField(serialize = false)
    private int status;

    @JSONField(serialize = false)
    private String topic;

    public DZMessage(){}

    public DZMessage(String topic, String body){
        this.topic = topic;
        this.body = body;
    }

    public static DZMessage wrap(String topic, String body) {
        return new DZMessage(topic, body);
    }
}

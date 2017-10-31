package com.dazong.mq.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dazong.mq.dao.mapper.MQMessageMapper;
import com.dazong.mq.domian.DZMessage;
import com.dazong.mq.manager.MQSendManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author huqichao
 * @create 2017-10-31 18:23
 **/
@Component
public class ReTrySendJob implements SimpleJob {

    private static final int SEND_MQ_BATCH = 20;

    private Logger logger = LoggerFactory.getLogger(ReTrySendJob.class);

    @Autowired
    private MQMessageMapper messageMapper;

    @Autowired
    private MQSendManager mqSendManager;

    /**
     * 执行作业.
     *
     * @param shardingContext 分片上下文
     */
    @Override
    public void execute(ShardingContext shardingContext) {
        List<DZMessage> messageList = messageMapper.queryMessageByStatus(DZMessage.STATUS_未处理, SEND_MQ_BATCH);
        logger.debug("定时重发未发送成功的消息：{}", messageList.size());
        for (DZMessage message : messageList){
            mqSendManager.send(message);
        }
    }
}

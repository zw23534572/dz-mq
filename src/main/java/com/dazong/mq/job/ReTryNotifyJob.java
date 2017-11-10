package com.dazong.mq.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dazong.mq.dao.mapper.MQMessageMapper;
import com.dazong.mq.domian.DZConsumerMessage;
import com.dazong.mq.domian.DZMessage;
import com.dazong.mq.manager.MQNotifyManager;
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
public class ReTryNotifyJob implements SimpleJob {

    private Logger logger = LoggerFactory.getLogger(ReTryNotifyJob.class);

    @Autowired
    private MQMessageMapper messageMapper;

    @Autowired
    private MQNotifyManager notifyManager;

    /**
     * 执行作业.
     *
     * @param shardingContext 分片上下文
     */
    @Override
    public void execute(ShardingContext shardingContext) {
        List<DZConsumerMessage> messageList = messageMapper.queryConsumerMessageByStatus(DZMessage.STATUS_DOING);
        logger.debug("定时重复通知未处理成功的消息：{}", messageList.size());
        for (DZConsumerMessage message : messageList){
            if (message.getNotifyCount() > 10){
                logger.warn("失败次数");
            }
            notifyManager.notifyMessage(message);
        }
    }
}

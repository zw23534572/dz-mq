package com.dazong.mq.dao.mapper;

import com.dazong.mq.domian.DZConsumerMessage;
import com.dazong.mq.domian.DZMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huqichao
 * @create 2017-10-30 15:57
 **/
public interface MQMessageMapper {

    void insertMessage(DZMessage message);

    void updateMessage(DZMessage message);

    DZMessage queryMessageByEventId(@Param("eventId") String eventId);

    List<DZMessage> queryMessageByStatus(@Param("status") int status, @Param("size") int size);

    void insertConsumerMessage(DZConsumerMessage consumerMessage);

    void updateConsumerMessage(DZConsumerMessage message);

    List<DZConsumerMessage> queryConsumerMessageByStatus(@Param("status") int status);

    DZConsumerMessage queryConsumerMessageByEventId(@Param("eventId") String eventId,
                                                    @Param("name") String name);

    List<DZConsumerMessage> queryConsumerMessageByGroupId(@Param("groupId") String groupId,
                                                          @Param("name") String name,
                                                          @Param("status") int status);

    void updateStatusById(@Param("id") Long id, @Param("status") int status);
}

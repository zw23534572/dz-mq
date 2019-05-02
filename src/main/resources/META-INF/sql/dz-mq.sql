DROP TABLE IF EXISTS `dz_mq_producer`;
CREATE TABLE `dz_mq_producer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `event_id` varchar(45) DEFAULT NULL COMMENT '消息唯一id',
  `group_id` varchar(128) DEFAULT NULL COMMENT '消息组id，相同组id的，按照发送时间依次通知',
  `destination` varchar(128) DEFAULT NULL COMMENT 'queue/topic名',
  `body` text COMMENT '消息体',
  `immediate` tinyint(4) DEFAULT NULL COMMENT '消费了消息是否立即通知应用 [1 立即通知 0 不立即通知，等待定时任务]',
  `queue` tinyint(4) DEFAULT NULL COMMENT '该消息是否为队列 [1 是 0 否]',
  `status` tinyint(4) DEFAULT NULL COMMENT '是否发送成功 [1 成功 0 不成功，等待定时任务]',
  `send_time` bigint(20) NOT NULL COMMENT '消息发送时间',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发送消息本地表-0';


DROP TABLE IF EXISTS `dz_mq_consumer`;
CREATE TABLE `dz_mq_consumer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `event_id` varchar(45) DEFAULT NULL COMMENT '消息唯一id',
  `group_id` varchar(128) DEFAULT NULL COMMENT '消息组id，相同组id的，按照发送时间依次通知',
  `name` varchar(128) DEFAULT NULL COMMENT '消费该消息的listener名称',
  `destination` varchar(128) DEFAULT NULL COMMENT 'queue/topic名',
  `body` text COMMENT '消息体',
  `status` tinyint(4) DEFAULT NULL COMMENT '应用是否消费成功 [1 成功 0 不成功，等待定时任务]',
  `queue` tinyint(4) DEFAULT NULL COMMENT '该消息是否为队列 [1 是 0 否]',
  `send_time` bigint(20) NOT NULL COMMENT '消息发送时间',
  `receive_time` bigint(20) NOT NULL COMMENT '消息接收时间',
  `notify_count` int(11) NOT NULL COMMENT '通知应用次数',
  `last_notify_time` datetime(3) DEFAULT NULL COMMENT '最后通知应用时间',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接收消息本地表';
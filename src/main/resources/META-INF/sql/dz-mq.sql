DROP TABLE IF EXISTS `dz_mq_producer`;
CREATE TABLE `dz_mq_producer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_id` varchar(45) DEFAULT NULL,
  `group_id` varchar(128) DEFAULT NULL,
  `destination` varchar(128) DEFAULT NULL,
  `body` text,
  `immediate` tinyint(4) DEFAULT NULL,
  `queue` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `send_time` bigint(20) NOT NULL,
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发送消息本地表-0';


DROP TABLE IF EXISTS `dz_mq_consumer`;
CREATE TABLE `dz_mq_consumer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_id` varchar(45) DEFAULT NULL,
  `group_id` varchar(128) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `destination` varchar(128) DEFAULT NULL,
  `body` text,
  `status` tinyint(4) DEFAULT NULL,
  `queue` tinyint(4) DEFAULT NULL,
  `send_time` bigint(20) NOT NULL,
  `receive_time` bigint(20) NOT NULL,
  `notify_count` int(11) NOT NULL,
  `last_notify_time` datetime(3) DEFAULT NULL,
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接收消息本地表';
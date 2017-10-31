## 大宗可靠消息

---

### 简介
目前仅支持发送消息时的可靠性。通过发送时本地事务来保证可靠性。

### 使用说明
1、在pom.xml文件引入dz-mq的jar
```xml
<dependency>
    <groupId>com.dazong.mq</groupId>
    <artifactId>dz-mq</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

2、在系统启动时，初始化dz-mq
```java
@Autowired
private MQStartup mqStartup;
...
mqStartup.init();
```
后续会通过该方法增加参数配置，以适用不同系统的性能需求

3、发送消息
```java
@Autowired
private ActiveMQProducer producer;
...
DZMessage message = DZMessage.wrap("test11", "哈哈");
producer.sendMessage(message);
```
发送时必须保证与调用者存在同一个事务中


### Release Note

#### 1.0
1、支持在线更新本系统的数据库表

2、发送可靠性
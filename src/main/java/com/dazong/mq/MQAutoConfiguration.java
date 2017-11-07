package com.dazong.mq;

import com.dazong.mq.annotation.Subscribe;
import com.dazong.mq.core.consumer.IMessageListener;
import com.dazong.mq.core.consumer.activemq.ActiveMQConsumer;
import com.dazong.mq.dao.mapper.MQMessageMapper;
import com.dazong.mq.domian.TableInfo;
import com.dazong.mq.manager.DBManager;
import com.dazong.mq.manager.MQNotifyManager;
import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author huqichao
 * @create 2017-10-30 19:25
 **/
public class MQAutoConfiguration implements ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(MQAutoConfiguration.class);

//    private static final int SQL_VERSION = 1;
    private static final int SQL_VERSION = 0;

    private static final String TABLE_NAME = "dz_mq_producer";

    @Autowired
    private DBManager dbManager;

    @Autowired
    private MQNotifyManager mqNotifyManager;

    private ApplicationContext context;

    @Autowired
    private MQMessageMapper messageMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${db.name}")
    private String dbName;

    private void init() throws Exception {
        TableInfo tableInfo = dbManager.selectTable(dbName, TABLE_NAME);
        String path;
        if (tableInfo == null) {
            path = "META-INF/sql/dz-mq.sql";
            logger.debug("执行数据库脚本: {}", path);
            dbManager.executeSqlFile(Resources.getResourceAsReader(path));
        } else {
            int version = tableInfo.getVersion();
            if (version < SQL_VERSION){
                for (int i = version + 1; i<=SQL_VERSION; i++){
                    path = String.format("META-INF/sql/%s/dz-mq.sql", i);
                    logger.debug("执行数据库脚本: {}", path);
                    dbManager.executeSqlFile(Resources.getResourceAsReader(path), true, tableInfo, i);
                }
            }
        }
        addListener();
        new ActiveMQConsumer(jmsTemplate, mqNotifyManager, messageMapper).init();
    }

    private void addListener(){
        String[] names = context.getBeanNamesForAnnotation(Subscribe.class);
        for (String name : names){
            IMessageListener listener = context.getBean(name, IMessageListener.class);
            Subscribe subscribe = AnnotationUtils.findAnnotation(listener.getClass(), Subscribe.class);
            mqNotifyManager.registerListener(subscribe, listener);
        }
    }

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
     * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

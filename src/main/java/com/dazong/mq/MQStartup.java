package com.dazong.mq;

import com.dazong.mq.domian.TableInfo;
import com.dazong.mq.manager.DBManager;
import org.apache.ibatis.io.Resources;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author huqichao
 * @create 2017-10-30 19:25
 **/
@Lazy
@Component
public class MQStartup {

    private Logger logger = LoggerFactory.getLogger(MQStartup.class);

//    private static final int SQL_VERSION = 1;
    private static final int SQL_VERSION = 0;

    private static final String DB_NAME = "trade_hermes";
    private static final String TABLE_NAME = "dz_mq_producer";

    @Autowired
    private DBManager dbManager;

    public void init() throws Exception {
        TableInfo tableInfo = dbManager.selectTable(DB_NAME, TABLE_NAME);
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
    }
}

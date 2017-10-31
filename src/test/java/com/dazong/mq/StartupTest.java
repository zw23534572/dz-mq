package com.dazong.mq;

import com.dazong.mq.manager.DBManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author huqichao
 * @create 2017-10-30 20:34
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/dz-mq.xml" })
public class StartupTest {

    @Autowired
    private DBManager dbManager;

    @Test
    public void test(){
        try {
            System.out.println(dbManager.selectTable("trade_hermes", "dz_mq_producer"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

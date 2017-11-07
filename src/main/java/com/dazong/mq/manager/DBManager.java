package com.dazong.mq.manager;

import com.dazong.mq.domian.TableInfo;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author huqichao
 * @create 2017-10-30 19:49
 **/
@Component
public class DBManager {

    private Logger logger = LoggerFactory.getLogger(DBManager.class);

    @Autowired
    private DataSource dataSource;

    public TableInfo selectTable(String dbName, String tableName) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            String sql = String.format("select `table_schema`, `table_name`, `table_comment` " +
                    "from `information_schema`.`tables` where `table_schema`='%s' and `table_name`='%s';", dbName, tableName);

            rs = stmt.executeQuery(sql);
            TableInfo tableInfo = null;
            if (rs.next()){
                tableInfo = new TableInfo();
                tableInfo.setDbName(dbName);
                tableInfo.setTableName(tableName);
                tableInfo.setTableDesc(rs.getString("table_comment"));
            }
            return tableInfo;
        } finally {
            close(rs, stmt, conn);
        }
    }

    private void close(ResultSet rs, Statement pstmt, Connection conn) {
        try {
            if (rs != null){
                rs.close();
            }
            if (pstmt != null){
                pstmt.close();
            }
            if (conn != null){
                conn.close();
            }
        } catch (Exception e) {
            logger.error("close", e);
        }
    }

    public void executeSqlFile(Reader reader) throws Exception {
        executeSqlFile(reader, false, null, 0);
    }

    public void executeSqlFile(Reader reader, boolean updateVersion, TableInfo tableInfo, int version) throws Exception {
        ScriptRunner runner = null;
        try {
            Connection conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            runner = new ScriptRunner(conn);
            runner.setFullLineDelimiter(false);
            runner.setDelimiter(";");
            runner.runScript(reader);
            if (updateVersion){
                updateTableVersion(conn, tableInfo, version);
            }
            conn.commit();
        } finally {
            if (runner != null){
                runner.closeConnection();
            }
        }
    }

    private void updateTableVersion(Connection conn, TableInfo tableInfo, int version) throws Exception {
        Statement stmt = conn.createStatement();
        String sql = String.format("ALTER TABLE `%s`.`%s` COMMENT='%s';",
                tableInfo.getDbName(), tableInfo.getTableName(), tableInfo.getComment(version));
        stmt.execute(sql);
        close(null, stmt, null);
    }
}

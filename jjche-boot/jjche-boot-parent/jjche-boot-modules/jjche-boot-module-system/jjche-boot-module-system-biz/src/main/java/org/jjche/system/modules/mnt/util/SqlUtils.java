package org.jjche.system.modules.mnt.util;

import cn.hutool.log.StaticLog;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import org.jjche.common.util.StrUtil;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * <p>SqlUtils class.</p>
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 */
public class SqlUtils {

    private static DataSource getDataSource(String jdbcUrl, String userName, String password) {
        DruidDataSource druidDataSource = new DruidDataSource();
        String className;
        try {
            className = DriverManager.getDriver(jdbcUrl.trim()).getClass().getName();
        } catch (SQLException e) {
            throw new RuntimeException("Get class name error: =" + jdbcUrl);
        }
        if (StrUtil.isEmpty(className)) {
            DataTypeEnum dataTypeEnum = DataTypeEnum.urlOf(jdbcUrl);
            if (null == dataTypeEnum) {
                throw new RuntimeException("Not supported data type: jdbcUrl=" + jdbcUrl);
            }
            druidDataSource.setDriverClassName(dataTypeEnum.getDriver());
        } else {
            druidDataSource.setDriverClassName(className);
        }


        druidDataSource.setUrl(jdbcUrl);
        druidDataSource.setUsername(userName);
        druidDataSource.setPassword(password);
        // 配置获取连接等待超时的时间
        druidDataSource.setMaxWait(3000);
        // 配置初始化大小、最小、最大
        druidDataSource.setInitialSize(1);
        druidDataSource.setMinIdle(1);
        druidDataSource.setMaxActive(1);

        // 如果链接出现异常则直接判定为失败而不是一直重试
        druidDataSource.setBreakAfterAcquireFailure(true);
        try {
            druidDataSource.init();
        } catch (SQLException e) {
            StaticLog.error("Exception during pool initialization", e);
            throw new RuntimeException(e.getMessage());
        }

        return druidDataSource;
    }

    private static Connection getConnection(String jdbcUrl, String userName, String password) {
        DataSource dataSource = getDataSource(jdbcUrl, userName, password);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (Exception ignored) {
        }
        try {
            int timeOut = 5;
            if (null == connection || connection.isClosed() || !connection.isValid(timeOut)) {
                StaticLog.info("connection is closed or invalid, retry get connection!");
                connection = dataSource.getConnection();
            }
        } catch (Exception e) {
            StaticLog.error("create connection error, jdbcUrl: {}", jdbcUrl);
            throw new RuntimeException("create connection error, jdbcUrl: " + jdbcUrl);
        }
        return connection;
    }

    private static void releaseConnection(Connection connection) {
        if (null != connection) {
            try {
                connection.close();
            } catch (Exception e) {
                StaticLog.error(e.getMessage(), e);
                StaticLog.error("connection close error：" + e.getMessage());
            }
        }
    }

    /**
     * <p>testConnection.</p>
     *
     * @param jdbcUrl  a {@link java.lang.String} object.
     * @param userName a {@link java.lang.String} object.
     * @param password a {@link java.lang.String} object.
     * @return a boolean.
     */
    public static boolean testConnection(String jdbcUrl, String userName, String password) {
        Connection connection = null;
        try {
            connection = getConnection(jdbcUrl, userName, password);
            if (null != connection) {
                return true;
            }
        } catch (Exception e) {
            StaticLog.info("Get connection failed:" + e.getMessage());
        } finally {
            releaseConnection(connection);
        }
        return false;
    }

    /**
     * <p>executeFile.</p>
     *
     * @param jdbcUrl  a {@link java.lang.String} object.
     * @param userName a {@link java.lang.String} object.
     * @param password a {@link java.lang.String} object.
     * @param sqlFile  a {@link java.io.File} object.
     * @return a {@link java.lang.String} object.
     */
    public static String executeFile(String jdbcUrl, String userName, String password, File sqlFile) {
        Connection connection = getConnection(jdbcUrl, userName, password);
        String result = null;
        try {
            result = batchExecute(connection, readSqlList(sqlFile));
        } catch (Exception e) {
            StaticLog.error("sql脚本执行发生异常:{}", e.getMessage());
            result = e.getMessage();
        } finally {
            releaseConnection(connection);
        }
        return result;
    }

    /**
     * <p>
     * 批量执行sql
     * </p>
     *
     * @param connection /
     * @param sqlList    /
     * @return /
     */
    public static String batchExecute(Connection connection, List<String> sqlList) {
        String result = "success";
        Statement st = null;
        try {
            st = connection.createStatement();
            for (String sql : sqlList) {
                if (sql.endsWith(";")) {
                    sql = sql.substring(0, sql.length() - 1);
                }
                st.addBatch(sql);
            }
            st.executeBatch();
        } catch (SQLException throwables) {
            StaticLog.error(throwables.getMessage(), throwables);
            result = throwables.getMessage();
        } finally {
            try {
                st.close();
            } catch (SQLException throwables) {
                StaticLog.error(throwables.getMessage(), throwables);
            }
        }
        return result;
    }

    /**
     * 将文件中的sql语句以；为单位读取到列表中
     *
     * @param sqlFile /
     * @return /
     * @throws Exception e
     */
    private static List<String> readSqlList(File sqlFile) throws Exception {
        List<String> sqlList = Lists.newArrayList();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(sqlFile), StandardCharsets.UTF_8))) {
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                if (tmp.endsWith(";")) {
                    sb.append(tmp);
                    sqlList.add(sb.toString());
                    sb.delete(0, sb.length());
                } else {
                    sb.append(tmp);
                }
            }
            if (!"".endsWith(sb.toString().trim())) {
                sqlList.add(sb.toString());
            }
        }

        return sqlList;
    }

}

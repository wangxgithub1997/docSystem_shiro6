package com.jf.weidong.doc.utils;


import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCTools {
    private static ComboPooledDataSource ds = new ComboPooledDataSource("jf");
    private static ThreadLocal<Connection> tl = new ThreadLocal<>();

    // 获得数据源
    public static DataSource getDataSource() {
        return ds;
    }

    // 获取连接
    public static Connection getConnection() throws SQLException {
        Connection conn = tl.get();
        if (conn == null) {
            // 第一次获取
            conn = ds.getConnection();
            // 绑定
            tl.set(conn);
        }
        return conn;
    }

    //将开启事务的方法抽取到工具类中

    //开启事务
    public static void start() throws SQLException {
        Connection conn = getConnection();
        if (conn != null) {
            conn.setAutoCommit(false);
        }
    }

    //提交事务
    public static void commit() throws SQLException {
        Connection conn = tl.get();
        if (conn != null) {
            conn.commit();
        }
    }


    //回滚事务
    public static void rollback() throws SQLException {
        Connection conn = tl.get();
        if (conn != null) {
            conn.rollback();
        }
    }

    //关闭连接
    public static void close() {
        Connection conn = tl.get();
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                tl.remove();
                conn = null;
            }
        }
    }

}

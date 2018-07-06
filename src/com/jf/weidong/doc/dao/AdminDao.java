package com.jf.weidong.doc.dao;

import com.jf.weidong.doc.domain.data.AdminDO;
import com.jf.weidong.doc.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * 管理员dao
 */
@Repository
public class AdminDao {

    /**
     * 通过管理员名称获取管理员信息
     */
    public AdminDO getAdminByName(AdminDO adminDO) throws SQLException {
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        String sql = "select * from admin where username = ? and state = 1 limit 1";
        return qr.query(sql, new BeanHandler<AdminDO>(AdminDO.class), adminDO.getUsername());
    }

    /**
     * 通过管理员id获取管理员信息
     */
    public AdminDO getAdminById(AdminDO adminDO) throws SQLException {
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        String sql = "select * from admin where id = ? and state = 1 limit 1";
        return qr.query(sql, new BeanHandler<AdminDO>(AdminDO.class), adminDO.getId());
    }

    /**
     * 更新管理员信息
     */
    public int updateAdminInfo(AdminDO adminDO) throws SQLException {
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        String sql = "update admin set name = ?, phone = ? where id = ?";
        return qr.update(sql, adminDO.getName(), adminDO.getPhone(), adminDO.getId());
    }

    /**
     * 更新管理员密码
     */
    public int updateAdminPassword(AdminDO adminDO) throws SQLException {
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        String sql = "update admin set password = ? where id = ?";
        return qr.update(sql, adminDO.getPassword(), adminDO.getId());
    }


}

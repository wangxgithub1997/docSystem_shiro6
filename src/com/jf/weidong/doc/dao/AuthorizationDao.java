package com.jf.weidong.doc.dao;

import com.jf.weidong.doc.domain.data.AuthorizationDO;
import com.jf.weidong.doc.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * 权限Dao
 */
@Repository
public class AuthorizationDao {

    public AuthorizationDO getAuthorizationById(AuthorizationDO authorizationDO) throws SQLException {
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        String sql = "select * from authorization where id = ?";
        return qr.query(sql, new BeanHandler<AuthorizationDO>(AuthorizationDO.class), authorizationDO.getId());
    }

    public int addAuthorization(AuthorizationDO a) throws SQLException {
        String sql = "INSERT INTO `docsystem`.`authorization` (`id`, `bookSet`, `readerSet`, " +
                "`borrowSet`, `typeSet`, `backSet`, `forfeitSet`, `sysSet`, `superSet`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        QueryRunner qr = new QueryRunner();
        return qr.update(JDBCTools.getConnection(),sql, a.getId(), a.getBookSet(), a.getReaderSet(), a.getBorrowSet(), a.getTypeSet(), a.getBackSet(), a.getForfeitSet(), a.getSysSet(), 0);
    }


    public int updateAuthorization(AuthorizationDO a) throws SQLException {
        String sql = "update authorization set bookSet = ?, readerSet = ?, " +
                "borrowSet = ?, typeSet = ?, backSet = ?,  forfeitSet = ?, sysSet =? where id = ?";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.update(sql, a.getBookSet(), a.getReaderSet(), a.getBorrowSet(), a.getTypeSet(), a.getBackSet(), a.getForfeitSet(), a.getSysSet(), a.getId());
    }


}

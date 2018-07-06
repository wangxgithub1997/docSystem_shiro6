package com.jf.weidong.doc.dao;

import com.jf.weidong.doc.domain.data.AdminDO;
import com.jf.weidong.doc.domain.data.ReaderDO;
import com.jf.weidong.doc.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
public class ReaderDao {

    public ReaderDO getReaderByPNO(ReaderDO readerDO) throws SQLException {
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        String sql = "select * from reader where paperNO = ?";
        return qr.query(sql, new BeanHandler<ReaderDO>(ReaderDO.class), readerDO.getPaperNO());
    }


}

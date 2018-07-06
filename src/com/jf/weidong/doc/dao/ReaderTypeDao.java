package com.jf.weidong.doc.dao;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.ReaderTypeDO;
import com.jf.weidong.doc.domain.query.Query;
import com.jf.weidong.doc.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class ReaderTypeDao {

    public PageBean<ReaderTypeDO> pageSearch(Query query) throws SQLException {
        PageBean<ReaderTypeDO> pb = new PageBean<>();
        pb.setCurrentPage(query.getPageCode());
        pb.setPageSize(query.getPageSize());
        String sql_count = "SELECT COUNT(*) FROM readertype";
        String sql_data = "SELECT * FROM readertype LIMIT ?,?";

        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());

        Object o = qr.query(sql_count, new ScalarHandler<>());
        int count = Integer.parseInt(o.toString());
        pb.setTotalCount(count);
        int start = (query.getPageCode() - 1) * query.getPageSize();
        List<ReaderTypeDO> list = qr.query(sql_data, new BeanListHandler<ReaderTypeDO>(ReaderTypeDO.class), start, query.getPageSize());
        pb.setList(list);
        return pb;
    }

    /**
     * 通过读者类型id获取读者类型信息
     */
    public ReaderTypeDO getReaderTypeById(ReaderTypeDO readerTypeDO) throws SQLException {
        String sql = "SELECT * FROM readertype WHERE id = ?";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.query(sql, new BeanHandler<ReaderTypeDO>(ReaderTypeDO.class), readerTypeDO.getId());
    }

    /**
     * 通过读者类型名称获取读者类型信息
     */
    public ReaderTypeDO getReaderTypeByName(ReaderTypeDO readerTypeDO) throws SQLException {
        String sql = "SELECT * FROM readertype WHERE name = ?";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.query(sql, new BeanHandler<ReaderTypeDO>(ReaderTypeDO.class), readerTypeDO.getName());
    }

    /**
     *
     * @param r
     * @return
     * @throws SQLException
     */
    public int updateReaderType(ReaderTypeDO r) throws SQLException {
        String sql = "UPDATE readertype SET name = ?, maxNum = ?, bday = ?, penalty = ?,renewDays = ? WHERE id = ?";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.update(sql, r.getName(), r.getMaxNum(), r.getBday(), r.getPenalty(), r.getRenewDays(), r.getId());

    }

    public int addReaderType(ReaderTypeDO r) throws SQLException {
        String sql = "INSERT INTO `docsystem`.`readertype` (`id`, `name`, `maxNum`, `bday`, `penalty`, `renewDays`) VALUES (null, ?, ?, ?, ?, ?)";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.update(sql, r.getName(), r.getMaxNum(), r.getBday(), r.getPenalty(), r.getRenewDays());
    }

    public List<ReaderTypeDO> getReaderTypes() throws SQLException {
        String sql = "SELECT * FROM readertype";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.query(sql, new BeanListHandler<ReaderTypeDO>(ReaderTypeDO.class));
    }
}

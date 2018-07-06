package com.jf.weidong.doc.dao;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.BorrowInfoDO;
import com.jf.weidong.doc.domain.data.ReaderDO;
import com.jf.weidong.doc.domain.vo.BorrowInfoListVO;
import com.jf.weidong.doc.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

/**
 * 借阅dao
 */
@Repository
public class BorrowManageDao {


    /**
     * 借阅表：
     * 借阅编号、借阅日期、截止还书日期
     * 图书表
     * 图书ISBN号、图书名称
     * 读者表
     * 读者证件号	读者名称
     */
    public PageBean<BorrowInfoListVO> pageSearch(int pageCode, int pageSize) throws SQLException {
        String sql_data = "SELECT " +
                "bi.id borrowId, " +
                "bi.borrowDate, " +
                "bi.endDate, " +
                "bk.`name` bookName, " +
                "bk.ISBN, " +
                "r.paperNO, " +
                "r.`name` readerName " +
                "FROM " +
                "	borrowinfo bi, " +
                "	book bk, " +
                "	reader r " +
                "WHERE " +
                "	bi.fk_book = bk.id " +
                "AND bi.fk_reader = r.id ORDER BY bi.id DESC LIMIT ?,?";
        String sql_count = "SELECT " +
                "COUNT(*) " +
                "FROM " +
                "	borrowinfo bi, " +
                "	book bk, " +
                "	reader r " +
                "WHERE " +
                "	bi.fk_book = bk.id " +
                "AND bi.fk_reader = r.id";
        PageBean<BorrowInfoListVO> pb = new PageBean<BorrowInfoListVO>();
        pb.setCurrentPage(pageCode);
        pb.setPageSize(pageSize);


        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        Object o = qr.query(sql_count, new ScalarHandler<>());
        pb.setTotalCount(Integer.parseInt(o.toString()));


        int start = (pageCode - 1) * pageSize;
        List<BorrowInfoListVO> list = qr.query(sql_data,
                new BeanListHandler<BorrowInfoListVO>(BorrowInfoListVO.class), start, pageSize);
        pb.setList(list);

        return pb;
    }

    /**
     * 根据读者id查询为归还的记录
     * @param readerDO
     * @return
     * @throws SQLException
     */
    public int getNoBackNumberByReaderId(ReaderDO readerDO) throws SQLException {
        String sql = "SELECT COUNT(*) FROM borrowinfo b WHERE b.state = 0 OR b.state = 1 OR b.state = 3 OR b.state = 4 AND b.fk_reader = ?";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        Object o = qr.query(sql, new ScalarHandler<>(), readerDO.getId());
        return Integer.parseInt(o.toString());
    }

    /**
     * 查询为归还的列表
     * @return
     * @throws SQLException
     */
    public List<BorrowInfoDO> getNoBackBorrowList() throws SQLException {
        String sql = "SELECT * FROM borrowinfo b WHERE b.state = 0 OR b.state = 1 OR b.state = 3 OR b.state = 4";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        List<BorrowInfoDO> list = qr.query(sql, new BeanListHandler<BorrowInfoDO>(BorrowInfoDO.class));
        return list;
    }


    /**
     * 添加借阅信息
     *
     * @return 借阅id
     */
    public int addBorrow(BorrowInfoDO b) throws SQLException {
        String sql = "INSERT INTO `borrowinfo` (`id`, `borrowDate`, `endDate`, `overday`, " +
                "`state`, `fk_reader`, `fk_book`, `penalty`, `fk_admin`) VALUES (null, ?, ?,?,?,?, ?,?,?)";
        QueryRunner qr = new QueryRunner();
        int count = qr.update(JDBCTools.getConnection(), sql, b.getBorrowDate(),
                b.getEndDate(), b.getOverday(),
                b.getState(), b.getFk_reader(), b.getFk_book(),
                b.getPenalty(), b.getFk_admin());

        if (count > 0) {
            BigInteger bigInteger = qr.query(JDBCTools.getConnection(), "SELECT LAST_INSERT_ID()", new ScalarHandler<>(1));
            return bigInteger.intValue();
        }
        return -1;
    }

    /**
     * 根据借阅id查询借阅信息
     *
     * @param borrowInfoDO
     * @return
     * @throws SQLException
     */
    public BorrowInfoDO getBorrowInfoById(BorrowInfoDO borrowInfoDO) throws SQLException {
        String sql = "SELECT * FROM borrowInfo b WHERE b.id = ?";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.query(sql, new BeanHandler<BorrowInfoDO>(BorrowInfoDO.class), borrowInfoDO.getId());
    }


    public int updateBorrowInfo(BorrowInfoDO borrowInfoDO) throws SQLException {
        String sql = "UPDATE `borrowinfo` SET `state`=?  WHERE (`id`=?)";
        QueryRunner qr = new QueryRunner();
        return qr.update(JDBCTools.getConnection(), sql, borrowInfoDO.getState(), borrowInfoDO.getId());
    }

    public int updateBorrowStateAndEndDate(BorrowInfoDO borrowInfoDO) throws SQLException {
        String sql = "UPDATE `borrowinfo` SET `state`=? , `endDate`= ?  WHERE (`id`=?)";
        QueryRunner qr = new QueryRunner();
        return qr.update(JDBCTools.getConnection(), sql, borrowInfoDO.getState(),borrowInfoDO.getEndDate(), borrowInfoDO.getId());
    }

    public int updateBorrowStateAndOverdue(BorrowInfoDO borrowInfoDO) throws SQLException {
        String sql = "UPDATE `borrowinfo` SET `state`=? , `overday`= ?  WHERE (`id`=?)";
        QueryRunner qr = new QueryRunner();
        return qr.update(JDBCTools.getConnection(), sql, borrowInfoDO.getState(),borrowInfoDO.getOverday(), borrowInfoDO.getId());
    }


}

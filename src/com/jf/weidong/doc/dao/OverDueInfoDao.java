package com.jf.weidong.doc.dao;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.OverDueInfoDO;
import com.jf.weidong.doc.domain.data.ReaderDO;
import com.jf.weidong.doc.domain.query.OverdueListQuery;
import com.jf.weidong.doc.domain.vo.OverdueListVO;
import com.jf.weidong.doc.utils.DataUtils;
import com.jf.weidong.doc.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class OverDueInfoDao {

    public PageBean<OverdueListVO> pageSearch(OverdueListQuery query) throws SQLException {
        String sql_count = "SELECT count(*) FROM overdueinfo f "
                + "LEFT JOIN borrowinfo bi on f.id = bi.id "
                + "LEFT JOIN book bk on bk.id = bi.fk_book "
                + "LEFT JOIN reader r on bi.fk_reader = r.id where 1=1";
        String sql_data = "SELECT bi.id borrowId,bk.ISBN,bk.name bookName,r.paperNO,r.name readerName," +
                "bi.overday,f.forfeit FROM overdueinfo f "
                + "LEFT JOIN borrowinfo bi on f.id = bi.id "
                + "LEFT JOIN book bk on bk.id = bi.fk_book "
                + "LEFT JOIN reader r on bi.fk_reader = r.id where 1=1";

        StringBuilder and = new StringBuilder();
        if (query != null) {
            if (query.getBorrowId() != 0) {
                and.append(" and f.id like '%" + query.getBorrowId() + "%'");
            }
            if (DataUtils.isValid(query.getPaperNO())) {
                and.append(" and r.paperNO like '%" + query.getPaperNO() + "%'");
            }
            if (DataUtils.isValid(query.getISBN())) {
                and.append(" and bk.ISBN like '%" + query.getISBN() + "%'");
            }
            if (query.getReaderId() != 0) {
                and.append(" and bi.fk_reader = " + query.getReaderId());
            }
        }


        sql_count += and.toString();
        sql_data += and.toString() + " LIMIT ?,?";
        System.out.println(sql_count + sql_data);

        PageBean<OverdueListVO> pb = new PageBean<OverdueListVO>();    //pageBean对象，用于分页

        pb.setCurrentPage(query.getPageCode());//设置当前页码
        pb.setPageSize(query.getPageSize());//设置页面记录数

        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        Object o = qr.query(sql_count, new ScalarHandler());
        pb.setTotalCount(Integer.parseInt(o.toString()));    //设置总记录数

        int start = (query.getPageCode() - 1) * query.getPageSize();

        List<OverdueListVO> list = qr.query(sql_data, new BeanListHandler<>(OverdueListVO.class), start, query.getPageSize());
        if (list != null && list.size() > 0) {
            pb.setList(list);
            return pb;
        }
        return null;
    }


    /**
     * 通过读者id获取该读者的逾期列表信息
     */
    public List<OverDueInfoDO> getOverdueByReaderId(ReaderDO readerDO) throws SQLException {
        String sql = "SELECT * FROM borrowinfo b,overdueinfo o WHERE b.id = o.id AND b.fk_reader = ?";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.query(sql, new BeanListHandler<OverDueInfoDO>(OverDueInfoDO.class), readerDO.getId());
    }


    public int deleteOverDueInfo(OverDueInfoDO overDueInfoDO) throws SQLException {
        String sql = "DELETE FROM overdueInfo WHERE id = ?";
        QueryRunner qr = new QueryRunner();
        return qr.update(JDBCTools.getConnection(),sql,overDueInfoDO.getId());
    }


    public OverDueInfoDO getOverDueById(OverDueInfoDO overDueInfoDO) throws SQLException {
        String sql = "SELECT * FROM overdueinfo o WHERE o.id = ?";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.query(sql, new BeanHandler<OverDueInfoDO>(OverDueInfoDO.class), overDueInfoDO.getId());
    }


    public int addOverdueInfo(OverDueInfoDO o) throws SQLException {
        String sql = "INSERT INTO `overdueinfo` (`id`, `forfeit`, `isPay`, `fk_admin`) VALUES (?, ?, 0, NULL)";
        QueryRunner qr = new QueryRunner();
        return qr.update(JDBCTools.getConnection(),sql,o.getId(),o.getForfeit());
    }

    public int updateOverdueInfo(OverDueInfoDO o) throws SQLException {
        String sql = "UPDATE `overdueinfo` SET `forfeit` = ? WHERE id=?";
        QueryRunner qr = new QueryRunner();
        return qr.update(JDBCTools.getConnection(),sql,o.getForfeit(),o.getId());
    }




}

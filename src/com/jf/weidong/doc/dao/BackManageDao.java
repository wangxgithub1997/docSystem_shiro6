package com.jf.weidong.doc.dao;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.BackInfoDO;
import com.jf.weidong.doc.domain.query.BackListQuery;
import com.jf.weidong.doc.domain.vo.BackListVO;
import com.jf.weidong.doc.utils.DataUtils;
import com.jf.weidong.doc.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
@Repository
public class BackManageDao {
    /**
     * 添加归还信息
     * @param backInfoDO
     * @return
     * @throws SQLException
     */
    public int addBack(BackInfoDO backInfoDO) throws SQLException {
        String sql = "INSERT INTO `backinfo` (`id`, `backDate`, `fk_admin`) VALUES (?, ?, ?);";
        QueryRunner qr = new QueryRunner();
        return qr.update(JDBCTools.getConnection(), sql, backInfoDO.getId(), backInfoDO.getBackDate(), backInfoDO.getFk_admin());
    }

    public PageBean<BackListVO> pageSearch(BackListQuery query) throws SQLException {
        String sql_data = "SELECT" +
                " bi.id borrowId," +
                " bk.ISBN," +
                " bk.`name` bookName," +
                " r.paperNO," +
                " r.`name` readerName," +
                " b.backDate," +
                " bi.endDate" +
                " FROM backinfo b " +
                " LEFT JOIN borrowinfo bi ON b.id = bi.id" +
                " LEFT JOIN book bk ON bk.id = bi.fk_book" +
                " LEFT JOIN reader r ON r.id = bi.fk_reader WHERE 1= 1 ";

        String sql_count = "SELECT COUNT(*) FROM backinfo b " +
                " LEFT JOIN borrowinfo bi ON b.id = bi.id" +
                " LEFT JOIN book bk ON bk.id = bi.fk_book" +
                " LEFT JOIN reader r ON r.id = bi.fk_reader WHERE 1= 1 ";

        StringBuilder and = new StringBuilder();
        if (query != null) {
            if(DataUtils.isValid(query.getISBN())){
                and.append(" and bk.ISBN like '%" + query.getISBN() +"%'");
            }
            if(DataUtils.isValid(query.getPaperNO())){
                and.append(" and r.paperNO like '%" + query.getPaperNO() +"%'");
            }
            if(query.getBorrowId() > 0){
                and.append(" and bi.id like '%" + query.getBorrowId() +"%'");
            }
        }

        sql_count += and.toString();
        sql_data += and.toString() + " LIMIT ?,?";


        PageBean<BackListVO> pb = new PageBean<BackListVO>();
        pb.setCurrentPage(query.getPageCode());
        pb.setPageSize(query.getPageSize());


        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        Object o = qr.query(sql_count, new ScalarHandler<>());
        pb.setTotalCount(Integer.parseInt(o.toString()));


        int start = (query.getPageCode() - 1) * query.getPageSize();
        List<BackListVO> list = qr.query(sql_data,
                new BeanListHandler<BackListVO>(BackListVO.class), start, query.getPageSize());
        pb.setList(list);
        return pb;

    }

    public int updateBackInfo(BackInfoDO backInfoDO) throws SQLException {
        String sql = "UPDATE backinfo SET backDate = ?, fk_admin = ? WHERE id = ?";
        QueryRunner qr = new QueryRunner();
        return qr.update(JDBCTools.getConnection(), sql, backInfoDO.getBackDate(), backInfoDO.getFk_admin(), backInfoDO.getId());
    }








}

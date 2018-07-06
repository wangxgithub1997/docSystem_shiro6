package com.jf.weidong.doc.dao;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.query.BorrowSearchQuery;
import com.jf.weidong.doc.domain.vo.BorrowSearchListVO;
import com.jf.weidong.doc.utils.DataUtils;
import com.jf.weidong.doc.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class BorrowSearchDao {
    public PageBean<BorrowSearchListVO> pageSearch(BorrowSearchQuery query) throws SQLException {
        PageBean<BorrowSearchListVO> pb = new PageBean<BorrowSearchListVO>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setCurrentPage(query.getPageCode());//设置当前页码
        pb.setPageSize(query.getPageSize());//设置页面记录数

        StringBuilder and = new StringBuilder();

        String sql_count = "SELECT count(*) FROM backinfo back "
                + "LEFT JOIN borrowinfo bi on back.id = bi.id "
                + "LEFT JOIN admin a on bi.fk_admin = a.id "
                + "LEFT JOIN reader r on bi.fk_reader = r.id "
                + "LEFT JOIN book bk on bi.fk_book = bk.id where 1=1";
        String sql_data = "SELECT bi.id borrowId,bk.ISBN ISBN ,bk.name bookName,r.paperNO paperNO,"
                + "r.name readerName,bi.borrowDate, back.backDate,bi.endDate FROM backinfo back "
                + "LEFT JOIN borrowinfo bi on back.id = bi.id "
                + "LEFT JOIN admin a on bi.fk_admin = a.id "
                + "LEFT JOIN reader r on bi.fk_reader = r.id "
                + "LEFT JOIN book bk on bi.fk_book = bk.id where 1=1 ";

        if (query != null) {
            if (DataUtils.isValid(query.getISBN())) {
                and.append(" and bk.ISBN like '%" + query.getISBN() + "%'");
            }
            if (DataUtils.isValid(query.getPaperNO())) {
                and.append(" and r.paperNO like '%" + query.getPaperNO() + "%'");
            }
            if (query.getBorrowId() > 0) {
                and.append(" and bi.id like '%" + query.getBorrowId() + "%'");
            }
        }
        sql_count += and.toString();
        sql_data += and.toString() + " limit ?,?";
        System.out.println(sql_count);
        System.out.println(sql_data);

        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());

        Object o = qr.query(sql_count, new ScalarHandler());
        String ss = o.toString();
        int record = Integer.parseInt(ss);
        pb.setTotalCount(record);    //设置总记录数

        int start = (query.getPageCode() - 1) * query.getPageSize();
        List<BorrowSearchListVO> backInfos = qr.query(sql_data, new BeanListHandler<BorrowSearchListVO>(BorrowSearchListVO.class),
                start, query.getPageSize());
        if (backInfos != null && backInfos.size() > 0) {
            pb.setList(backInfos);
            return pb;
        }
        return null;
    }
}

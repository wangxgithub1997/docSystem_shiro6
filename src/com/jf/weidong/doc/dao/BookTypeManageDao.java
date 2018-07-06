package com.jf.weidong.doc.dao;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.BookTypeDO;
import com.jf.weidong.doc.domain.query.BookTypeQuery;
import com.jf.weidong.doc.utils.DataUtils;
import com.jf.weidong.doc.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * 图书分类Dao
 */
@Repository
public class BookTypeManageDao {


    public PageBean<BookTypeDO> pageSearch(BookTypeQuery query) throws SQLException {
        String sql_data = "SELECT * FROM booktype b ";
        String sql_count = "SELECT count(*) FROM booktype b";
        PageBean<BookTypeDO> pb = new PageBean<>();
        StringBuilder sb = new StringBuilder();
        if(query!=null){
            if(DataUtils.isValid(query.getTypeName())){//查询的条件有可能为空
                sb.append(" where b.name like '%" + query.getTypeName() +"%'");
            }
        }
        sql_count += sb.toString();
        sql_data += sb.toString();
        sql_data += " LIMIT ?,?";

        System.out.println(sql_data);
        System.out.println(sql_count);

        pb.setCurrentPage(query.getPageCode());
        pb.setPageSize(query.getPageSize());
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        Object o = qr.query(sql_count, new ScalarHandler<>());
        int count = Integer.parseInt(o.toString());
        pb.setTotalCount(count);

        //开始
        int start = (query.getPageCode() - 1) * (query.getPageSize());
        List<BookTypeDO> list = qr.query(sql_data, new BeanListHandler<BookTypeDO>(BookTypeDO.class), start, query.getPageSize());
        pb.setList(list);

        return pb;
    }

    public int deleteBookTypeById(BookTypeDO typeDO) throws SQLException {
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
         return qr.update("delete from BookType where id = ?", typeDO.getId());
    }

    public BookTypeDO getBookTypeById(BookTypeDO typeDO) throws SQLException {
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        String sql = "SELECT * FROM booktype WHERE id = ? limit 1";
        return qr.query(sql, new BeanHandler<BookTypeDO>(BookTypeDO.class), typeDO.getId());
    }


    public int updateBookTypeInfo(BookTypeDO typeDO) throws SQLException {
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        String sql = "update booktype set name = ?  WHERE id = ?";
        return qr.update(sql, typeDO.getName(), typeDO.getId());
    }


    public BookTypeDO getBookTypeByName(BookTypeDO typeDO) throws SQLException {
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        String sql = "SELECT * FROM booktype WHERE name = ? limit 1";
        return qr.query(sql, new BeanHandler<BookTypeDO>(BookTypeDO.class), typeDO.getName());
    }



    public int addBookType(BookTypeDO typeDO) throws SQLException {
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        String sql = "INSERT INTO booktype VALUES(null,?)";
        return qr.update(sql, typeDO.getName());
    }


}

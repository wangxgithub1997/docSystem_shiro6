package com.jf.weidong.doc.dao;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.BookDO;
import com.jf.weidong.doc.domain.data.BookTypeDO;
import com.jf.weidong.doc.domain.query.BookManageQuery;
import com.jf.weidong.doc.domain.vo.BookDetailsVO;
import com.jf.weidong.doc.domain.vo.BookManageListVO;
import com.jf.weidong.doc.utils.DataUtils;
import com.jf.weidong.doc.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图书管理
 */
@Repository
public class BookManageDao {

    /**
     * 分页查询图书
     *      String sql = "select * from book where";
     * 		if(bookName != null){
     * 			sql += " bookName = 'Java' ";
     *                }
     * 		if(ISBN != null){
     * 			sql += " and ISBN = '111'"
     *        }
     *      如果所有的条件都满足：
     *          select * from book where bookName = 'Java'  and ISBN = '111'
     *
     *      如果只有bookName满足
     *          select * from book where bookName = 'Java'
     *      如果只有ISBN 满足
     *          select * from book where and ISBN = '111'
     *
     *      解决：添加sql 后面 加 where 1=1 ，之后所有拼接的条件都添加and
     *
     *      String sql = "select * from book where 1=1";
     *      if(bookName != null){
     * 			sql += " and bookName = 'Java' ";
     *      }
     * 		if(ISBN != null){
     * 			sql += " and ISBN = '111'"
     *      }
     *      如果只有ISBN 满足
     *          select * from book where 1=1 and ISBN = '111'
     */
    public PageBean<BookManageListVO> pageSearch(BookManageQuery query, int pageSize) throws SQLException {
        PageBean<BookManageListVO> pb = new PageBean<>();
        pb.setCurrentPage(query.getPageCode());
        pb.setPageSize(pageSize);
        String sql_count = "SELECT COUNT(*) FROM book b WHERE 1=1";
        String sql_data = "SELECT b.*,bt.name bookType FROM book b,booktype bt WHERE b.fk_booktype = bt.id ";
        StringBuilder and = new StringBuilder();
        if (DataUtils.isValid(query.getISBN())) {
            and.append(" and b.ISBN like '%"+query.getISBN()+"%'");
        }

        if (DataUtils.isValid(query.getBookName())) {
            and.append(" and b.name like '%"+query.getBookName()+"%'");
        }

        if (DataUtils.isValid(query.getAutho())) {
            and.append(" and b.autho like '%"+query.getAutho()+"%'");
        }
        if (DataUtils.isValid(query.getPress())) {
            and.append(" and b.press like '%"+query.getPress()+"%'");
        }
        if (query.getBookTypeId() != null && query.getBookTypeId() != -1) {
            and.append(" and b.fk_booktype = "+query.getBookTypeId());
        }
        sql_count += and.toString();
        sql_data += and.toString();
        sql_data += " LIMIT ?,?";
        System.out.println(sql_count);
        System.out.println(sql_data);
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        Object o = qr.query(sql_count,new ScalarHandler<>());
        int count = Integer.parseInt(o.toString());
        pb.setTotalCount(count);
        int start = (query.getPageCode() - 1) * pageSize;
        List<BookManageListVO> list = qr.query(sql_data,
                new BeanListHandler<BookManageListVO>(BookManageListVO.class), start, pageSize);
        pb.setList(list);
        return pb;
    }

    /**
     * 获取图书分类
     */
    public List<BookTypeDO> getAllBookTypes() throws SQLException {
        String sql = "SELECT * FROM booktype";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return  qr.query(sql, new BeanListHandler<BookTypeDO>(BookTypeDO.class));
    }

    /**
     * 添加图书
     */
    public int addBook(BookDO bookDO) throws SQLException {
        String sql = "INSERT INTO `book` (`id`, `name`, `ISBN`, `autho`, `num`, `currentNum`, `press`," +
                " `description`, `price`, `putdate`, `fk_booktype`, `fk_admin`) " +
                "VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.update(sql,
                bookDO.getName(),
                bookDO.getISBN(),
                bookDO.getAutho(),
                bookDO.getNum(),
                bookDO.getNum(),
                bookDO.getPress(),
                bookDO.getDescription(),
                bookDO.getPrice(),
                bookDO.getPutdate(),
                bookDO.getFk_booktype(),
                bookDO.getFk_admin());
    }

    /**
     * 通过书籍ISBN获取书籍信息
     * @param bookDO
     * @return
     * @throws SQLException
     */
    public BookDO getBookByISBN(BookDO bookDO) throws SQLException {
        String sql = "SELECT * FROM book WHERE ISBN = ?";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.query(sql, new BeanHandler<BookDO>(BookDO.class), bookDO.getISBN());
    }

    /**
     * 通过书籍id获取书籍信息
     * @param bookDO
     * @return
     * @throws SQLException
     */
    public BookDO getBookById(BookDO bookDO) throws SQLException {
        String sql = "SELECT * FROM book WHERE id = ?";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.query(sql, new BeanHandler<BookDO>(BookDO.class), bookDO.getId());
    }

    /**
     * 更新书籍信息
     */
    public int updateBook(BookDO bookDO) throws SQLException {
        String sql = "UPDATE `book` SET `name`=?, `ISBN`=?, `autho`=?, `press`=?, " +
            "`description`=?, `price`=? ,`fk_booktype`=? WHERE (`id`=?)";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.update(sql, bookDO.getName(),
                bookDO.getISBN(), bookDO.getAutho(),
                bookDO.getPress(), bookDO.getDescription(),
                bookDO.getPrice(), bookDO.getFk_booktype(),
                bookDO.getId());
    }

    /**
     * 更新书籍在馆数量
     */
    public int updateBookCurrentNum(BookDO bookDO) throws SQLException {
        String sql = "UPDATE `book` SET currentNum = ? WHERE (`id`=?)";
        QueryRunner qr = new QueryRunner();
        return qr.update(JDBCTools.getConnection(),sql, bookDO.getCurrentNum(),
                bookDO.getId());
    }



    /**
     * 修改书籍数量
     */
    public int addBookNum(BookDO bookDO) throws SQLException {
        String sql = "UPDATE `book` SET `num`=?, `currentNum`=? WHERE (`id`=?)";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.update(sql, bookDO.getNum(), bookDO.getCurrentNum(), bookDO.getId());
    }

    /**
     * 获取导出的书籍列表
     */
    public List<BookDetailsVO> exportBook() throws SQLException {
        String sql = "SELECT b.*,b.name bookName,bt.name bookType,a.name adminName FROM book" +
                " b,booktype bt,admin a WHERE b.fk_booktype = bt.id AND b.fk_admin = a.id";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return  qr.query(sql, new BeanListHandler<BookDetailsVO>(BookDetailsVO.class));

    }

    /**
     * 变量插入数据
     * @param bookDOS   源数据
     * @return 失败的数据
     */
    public List<BookDO> batchAddBook(List<BookDO> bookDOS){
        List<BookDO> fBooks = new ArrayList<>();
        for (int i = 0; i < bookDOS.size(); i++) {
            try {
                addBook(bookDOS.get(i));
            } catch (SQLException e) {
                e.printStackTrace();
                fBooks.add(bookDOS.get(i));
            }
        }
        return fBooks;
    }





}

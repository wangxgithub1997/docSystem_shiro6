package com.jf.weidong.doc.dao;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.ReaderDO;
import com.jf.weidong.doc.domain.query.ReaderQuery;
import com.jf.weidong.doc.domain.vo.ReaderDetailsVO;
import com.jf.weidong.doc.domain.vo.ReaderListVO;
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
public class ReaderManagerDao {
    /**
     * 分页查询读者信息
     */
    public PageBean<ReaderListVO> pageSearch(ReaderQuery query) throws SQLException {
        PageBean<ReaderListVO> pb = new PageBean<>();
        pb.setPageSize(query.getPageSize());
        int pageCode=DataUtils.getPageCode(query.getPageCode()+"");
        pb.setCurrentPage(pageCode);
        String sql_data = "SELECT r.id,r.paperNO,r.`name` readerName,r.phone,r.createTime,rt.`name` readerTypeName FROM reader r,readertype rt WHERE r.fk_readertype = rt.id";
        String sql_count = "SELECT COUNT(*) FROM reader r,readertype rt WHERE r.fk_readertype = rt.id";
        StringBuilder and = new StringBuilder();
        if (DataUtils.isValid(query.getPaperNO())) {
            and.append(" and r.paperNO like '%" + query.getPaperNO() + "%'");
        }
        if (DataUtils.isValid(query.getName())) {
            and.append(" and r.name like '%" + query.getName() + "%'");
        }
        if (query.getReaderType() != null && query.getReaderType() > 0) {
            and.append(" and rt.id =" + query.getReaderType());
        }
        sql_data += and.toString();
        sql_count += and.toString();
        sql_data += " limit ?,?";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        int start = (pageCode - 1) * query.getPageSize();
        List<ReaderListVO> list = qr.query(sql_data, new BeanListHandler<ReaderListVO>(ReaderListVO.class), start, query.getPageSize());
        Object o = qr.query(sql_count, new ScalarHandler<>());
        pb.setTotalCount(Integer.parseInt(o.toString()));
        pb.setList(list);
        return pb;
    }

    /**
     * 根据读者paperNO查询读者do
     */
    public ReaderDO getReaderDOByPaperNO(ReaderDO readerDO) throws SQLException {
        String sql = "SELECT * FROM reader r WHERE r.paperNO = ?";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.query(sql, new BeanHandler<ReaderDO>(ReaderDO.class), readerDO.getPaperNO());
    }

    /**
     * 根据读者id查询读者详情
     */
    public ReaderDetailsVO getReaderDetails(ReaderDO readerDO) throws SQLException {
        String sql = "SELECT r.id, r.paperNO, r.`name` readerName, rt.`name` readerType, r.phone, r.email, a.name adminName FROM reader r, readertype rt, admin a WHERE r.fk_readertype = rt.id AND r.fk_admin = a.id AND r.id = ?";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.query(sql, new BeanHandler<ReaderDetailsVO>(ReaderDetailsVO.class), readerDO.getId());
    }

    /**
     * 根据读者PaperNO查询读者详情
     */
    public ReaderDetailsVO getReaderDetailsByPaperNO(ReaderDO readerDO) throws SQLException {
        String sql = "SELECT r.id, r.paperNO, r.`name` readerName, rt.`name` readerType, r.phone, r.email, a.name adminName FROM reader r, readertype rt, admin a WHERE r.fk_readertype = rt.id AND r.fk_admin = a.id AND r.paperNO = ?";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.query(sql, new BeanHandler<ReaderDetailsVO>(ReaderDetailsVO.class), readerDO.getPaperNO());
    }


    /**
     * 更新读者信息
     */
    public int updateReader(ReaderDO r) throws SQLException {
        String sql = "UPDATE `reader` SET `name`=?, `phone`=?, `email`=?, `fk_readertype`=? WHERE (`id`=?)";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.update(sql, r.getName(), r.getPhone(), r.getEmail(), r.getFk_readerType(), r.getId());
    }

    public List<ReaderDetailsVO> exportReader() throws SQLException {
        String sql = "SELECT r.paperNO, r.`name` readerName, rt.`name` readerType, r.phone, r.email FROM reader r, readertype rt, admin a WHERE r.fk_readertype = rt.id AND r.fk_admin = a.id";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.query(sql, new BeanListHandler<ReaderDetailsVO>(ReaderDetailsVO.class));
    }

    public int addReader(ReaderDO r) throws SQLException {
        String sql = "INSERT INTO `reader` (`id`, `password`, `name`, `paperNO`, `phone`, `email`, `createTime`, " +
                "`fk_readertype`, `fk_admin`) VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?);";
        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        return qr.update(sql, r.getPassword(),
                r.getName(), r.getPaperNO(), r.getPhone(), r.getEmail(),
                r.getCreateTime(), r.getFk_readerType(), r.getFk_admin());
    }

    /**
     * 批量添加
     */
    public void batchAddReader(List<ReaderDO> readerDOS){
        for (ReaderDO r : readerDOS) {
            try {
                addReader(r);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }




}

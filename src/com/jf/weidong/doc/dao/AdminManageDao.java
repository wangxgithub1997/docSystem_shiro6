package com.jf.weidong.doc.dao;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.AdminDO;
import com.jf.weidong.doc.domain.data.AuthorizationDO;
import com.jf.weidong.doc.domain.query.AdminListQuery;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.utils.DataUtils;
import com.jf.weidong.doc.utils.JDBCTools;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Repository
public class AdminManageDao {

    ////管理员用户名	管理员姓名	联系号码  id

    /**
     * 需要返回管理员+权限的信息
     * @param query
     * @return
     * @throws SQLException
     */
    public PageBean<AdminDetailsVO> pageSearch(AdminListQuery query) throws SQLException, InvocationTargetException, IllegalAccessException {
        String sql_data = "SELECT a.id,a.username,a.`name`,a.phone,au.* FROM admin a,authorization au WHERE a.state = 1 AND a.id = au.id ";
        String sql_count = "SELECT COUNT(*) FROM admin a WHERE a.state = 1";

        StringBuilder and = new StringBuilder();
        if (DataUtils.isValid(query.getAdminName())) {
            and.append(" and a.name = '%" + query.getAdminName() + "%'");
        }
        if (DataUtils.isValid(query.getAdminUserName())) {
            and.append(" and a.username = '%" + query.getAdminUserName() + "%'");
        }

        sql_data += and.toString() + " LIMIT ?,?";
        sql_count += and.toString();

        PageBean<AdminDetailsVO> pb = new PageBean<>();

        pb.setCurrentPage(query.getPageCode());
        pb.setPageSize(query.getPageSize());


        QueryRunner qr = new QueryRunner(JDBCTools.getDataSource());
        Object o = qr.query(sql_count, new ScalarHandler<>());
        pb.setTotalCount(Integer.parseInt(o.toString()));

        int start = (query.getPageCode() - 1) * query.getPageSize();
//       List<AdminDetailsVO> list = qr.query(sql_data, new BeanListHandler<AdminDetailsVO>(AdminDetailsVO.class), start, pageSize);
//       pb.setList(list);
        List<Map<String, Object>> resultList = qr.query(sql_data, new MapListHandler(), start, query.getPageSize());
        List<AdminDetailsVO> list = new ArrayList<>();
        for (Map<String, Object> map : resultList) {
            AdminDetailsVO adminDetailsVO = new AdminDetailsVO();
            BeanUtils.copyProperties(adminDetailsVO, map);

            AuthorizationDO au = new AuthorizationDO();
            BeanUtils.copyProperties(au, map);

            adminDetailsVO.setAuthorization(au);
            list.add(adminDetailsVO);
        }
        pb.setList(list);
        return pb;
    }

    /**
     * 添加管理员
     * @param a
     * @return 管理员的id
     * @throws SQLException
     */
    public int addAdmin(AdminDO a) throws SQLException {
        String sql = "INSERT INTO `docsystem`.`admin` (`id`, `username`, `name`, `phone`, `state`, `password`)" +
                " VALUES (null, ?, ?, ?, ?, ?)";
        //获取最近insert的那行记录的自增字段值
        String sql_id = "SELECT LAST_INSERT_ID();";
        QueryRunner qr = new QueryRunner();
        //两个方法用的是同一个Connection连接
        int count = qr.update(JDBCTools.getConnection(),sql, a.getUsername(), a.getName(), a.getPhone(), a.getState(), a.getPassword());
        if (count > 0) {
            BigInteger id = (BigInteger) qr.query(JDBCTools.getConnection(),sql_id, new ScalarHandler<>());
            return id.intValue();
        }
        return  -1;
    }


}

package com.jf.weidong.doc.service;

import com.jf.weidong.doc.dao.AdminManageDao;
import com.jf.weidong.doc.dao.AuthorizationDao;
import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.AdminDO;
import com.jf.weidong.doc.domain.data.AuthorizationDO;
import com.jf.weidong.doc.domain.query.AdminListQuery;
import com.jf.weidong.doc.domain.query.Query;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.mapper.AdminManageMapper;
import com.jf.weidong.doc.mapper.AuthorizationMapper;
import com.jf.weidong.doc.utils.JDBCTools;
import com.jf.weidong.doc.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

@Service
public class AdminManageService {
    @Autowired
    AdminManageDao adminManageDao = new AdminManageDao();

    @Autowired
    AuthorizationDao authorizationDao = new AuthorizationDao();

    @Autowired
    AdminManageMapper adminManageMapper;

    @Autowired
    AuthorizationMapper authorizationMapper;
    public PageBean<AdminDetailsVO> pageSearch(AdminListQuery query) {
        PageBean<AdminDetailsVO> pb=new PageBean<>();
        query.setStart((query.getPageCode()-1)*query.getPageSize());
        List<AdminDetailsVO> list = adminManageMapper.pageSearch(query);
        if(!list.isEmpty()){
            pb.setList(list);
        }
        Integer totalCount = adminManageMapper.totalCount(query);
        pb.setTotalCount(totalCount);
        pb.setCurrentPage(query.getPageCode());
        pb.setPageSize(query.getPageSize());
        return pb;
    }

    /**
     * 添加管理员
     * @param a
     * @return
     */
    @Transactional
    public int addAdmin(AdminDO a) {
            a.setState(1);//设置删除状态
            a.setPassword(Md5Utils.md5("123456"));
            int i = adminManageMapper.addAdmin(a);
            if (i != -1) {
                int id=a.getId();
                AuthorizationDO authorizationDO = new AuthorizationDO(id,
                        0, 0, 0, 0, 0, 0, 0);
                //authorizationDO.setId(id);
               //int j = 1 / 0;
                int count = authorizationMapper.addAuthorization(authorizationDO);
                if (count > 0) {
                    return 1;
                }
            }
        return 0;
    }
}

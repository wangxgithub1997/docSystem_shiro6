package com.jf.weidong.doc.service;

import com.jf.weidong.doc.dao.AdminDao;
import com.jf.weidong.doc.domain.data.AdminDO;
import com.jf.weidong.doc.domain.data.AuthorizationDO;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.mapper.AdminMapper;
import com.jf.weidong.doc.mapper.AuthorizationMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

@Service
public class AdminService {
    @Autowired
    AdminDao adminDao; //= new AdminDao();
    @Autowired
    AuthorizationService authorizationService; //= new AuthorizationService();
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    AuthorizationMapper authorizationMapper;
    /**
     * @return null 没有该用户
     */
    public AdminDetailsVO getAdminByName(AdminDO adminDO) {
        try {
            AdminDO admiDO = adminMapper.getAdminByName(adminDO.getUsername());
            //2018年6月1日09:23:24  用户为空，没有这个用户
            if (admiDO != null) {
                //do  ---> vo
                AdminDetailsVO vo = new AdminDetailsVO();
                BeanUtils.copyProperties(vo, admiDO);

                //根据管理员id查询相应的权限
                AuthorizationDO au = authorizationMapper.getAuthorizationById(admiDO.getId());
                vo.setAuthorization(au);
                System.out.println(vo);

                return vo;
            }
        }  catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updateAdminInfo(AdminDO adminDO) {
        try {
            return adminDao.updateAdminInfo(adminDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int updateAdminPassword(AdminDO adminDO) {
        try {
            return adminDao.updateAdminPassword(adminDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

package com.jf.weidong.doc.service;

import com.jf.weidong.doc.dao.AuthorizationDao;
import com.jf.weidong.doc.domain.data.AuthorizationDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
@Service
public class AuthorizationService {
    @Autowired
    AuthorizationDao dao; //= new AuthorizationDao();


    public AuthorizationDO getAuthorizationById(AuthorizationDO authorizationDO) {
        try {
            return dao.getAuthorizationById(authorizationDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新权限
     * @param authorizationDO
     * @return
     */
    public int updateAuthorizationById(AuthorizationDO authorizationDO) {
        try {
            return dao.updateAuthorization(authorizationDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}

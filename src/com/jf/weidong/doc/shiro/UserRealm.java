package com.jf.weidong.doc.shiro;

import com.jf.weidong.doc.domain.data.AdminDO;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.service.AdminService;
import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthenticatingRealm {

    @Autowired
    AdminService adminService;

    @Override
    public String getName(){
        return "UserRealm";
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户名
        String userName = (String)authenticationToken.getPrincipal();
        AdminDetailsVO admin = adminService.getAdminByName(new AdminDO(userName));
        if(admin==null){
            throw new UnknownAccountException("没有用户");
        }
        String password = admin.getPassword();
        return new SimpleAuthenticationInfo(admin,password,getName());
    }
}

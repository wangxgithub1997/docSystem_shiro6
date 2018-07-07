package com.jf.weidong.doc.shiro;

import com.jf.weidong.doc.domain.data.AdminDO;
import com.jf.weidong.doc.domain.data.AuthorizationDO;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.service.AdminService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    AdminService adminService;

    @Override
    public String getName(){
        return "UserRealm";
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
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

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
       /*获得此时登陆（即认证过的）对象*/
        AdminDetailsVO vo =(AdminDetailsVO) principalCollection.getPrimaryPrincipal();
        //获得此管理员权限
        AuthorizationDO authorization = vo.getAuthorization();
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        if(authorization.getBookSet()==1){
            info.addStringPermission("bookSet");
        }
        if(authorization.getReaderSet()==1){
            info.addStringPermission("readerSet");
        }
        if(authorization.getBorrowSet()==1){
            info.addStringPermission("borrowSet");
        }
        if(authorization.getTypeSet()==1){
            info.addStringPermission("typeSet");
        }
        if(authorization.getBackSet()==1){
            info.addStringPermission("backSet");
        }
        if(authorization.getForfeitSet()==1){
            info.addStringPermission("forfeitSet");
        }
        if(authorization.getSysSet()==1){
            info.addStringPermission("sysSet");
        }
        if(authorization.getSuperSet()==1){
            info.addStringPermission("superSet");
        }
        return info;
    }
}

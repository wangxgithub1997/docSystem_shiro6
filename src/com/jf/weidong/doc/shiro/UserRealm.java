package com.jf.weidong.doc.shiro;

import com.jf.weidong.doc.domain.data.AdminDO;
import com.jf.weidong.doc.domain.data.AuthorizationDO;
import com.jf.weidong.doc.domain.data.ReaderDO;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.service.AdminService;
import com.jf.weidong.doc.service.ReaderService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    AdminService adminService;

    @Autowired
    ReaderService readerService;

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

        //使用我们写的 UsernamePasswordTypeToken
        UsernamePasswordTypeToken token=(UsernamePasswordTypeToken)authenticationToken;
        if(token.getType()==UsernamePasswordTypeToken.READER){
            //principle 和username的区别
            //Object principal = token.getPrincipal();
            String username = token.getUsername();
            ReaderDO readerByPNO = readerService.getReaderByPNO(new ReaderDO(username));
            if(readerByPNO==null){
                throw new UnknownAccountException("没有读者");
            }
            String password=readerByPNO.getPassword();
            SimpleAuthenticationInfo info=
                    new SimpleAuthenticationInfo(readerByPNO,password,getName());
            info.setCredentialsSalt(ByteSource.Util.bytes("12345"));
            return info;
        }else{
             /*subject.login(usernamePasswordToken);
        我的理解
        * 当调用login方法，进入到此认证方法中，根据用户名查找admin对象
        * */

            //获取用户名
            String userName = (String)authenticationToken.getPrincipal();
            AdminDetailsVO admin = adminService.getAdminByName(new AdminDO(userName));
            if(admin==null){
                throw new UnknownAccountException("没有用户");
            }
            String password = admin.getPassword();

            SimpleAuthenticationInfo info=
                    new SimpleAuthenticationInfo(admin,password,getName());
            info.setCredentialsSalt(ByteSource.Util.bytes("12345"));
            return info;
          /*这块对比逻辑是先对比username，但是username肯定是相等的，所以真正对比的是password。
        从这里传入的password（这里是从数据库获取的）和token（filter中登录时生成的）
        中的password做对比，如果相同就允许登录，不相同就抛出异常。
        如果验证成功，最终这里返回的信息authenticationInfo 的值与传入的第一个字段的值相同
        （我这里传的是user对象）*/
        }
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

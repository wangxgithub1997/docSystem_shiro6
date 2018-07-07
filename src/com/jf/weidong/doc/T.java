package com.jf.weidong.doc;

import com.jf.weidong.doc.dao.AdminManageDao;
import com.jf.weidong.doc.domain.data.AdminDO;
import com.jf.weidong.doc.service.AdminService;
import com.jf.weidong.doc.utils.Md5Utils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

import java.sql.SQLException;

public class T {
    @Test
    public void t1(){
        AdminService adminService = new AdminService();
        //ctrl + alt + v 生成返回值
        //AdminDO admin = adminService.getAdminByName(new AdminDO("weidong"));
        //System.out.println(admin.getName());
    }

    @Test
    public void t2() throws SQLException {
        AdminManageDao adminManageDao = new AdminManageDao();
        AdminDO adminDO = new AdminDO();
        adminDO.setName("wangwu");
        adminDO.setPhone("18511898645");
        adminDO.setUsername("wangwu");
        adminDO.setState(1);
        adminDO.setPassword(Md5Utils.md5("123456"));
        int id = adminManageDao.addAdmin(adminDO);
        System.out.println(id);
    }

    //shrio加密
    @Test
    public void t3(){
        System.out.println(Md5Utils.md5("admin"));
        Md5Hash md=new Md5Hash("admin");
        System.out.println(md.toString());
                //ISMvKXpXpadDiUoOSoAfww==
                //21232f297a57a5a743894a0e4a801fc3
        //由上看出两种加密结果是不一致的
        Md5Hash md5Hash=new Md5Hash("admin","12345");
        System.out.println(md5Hash.toString());

    }
}

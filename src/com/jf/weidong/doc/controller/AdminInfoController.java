package com.jf.weidong.doc.controller;


import com.jf.weidong.doc.domain.data.AdminDO;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.service.AdminService;
import com.jf.weidong.doc.utils.Md5Utils;
import com.jf.weidong.doc.utils.myspringmvc.ContextLoaderListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class AdminInfoController extends BaseController {

    @Autowired
    AdminService adminService; //= new AdminService();

    /**
     * 1. 点击个人资料弹出模态框（回显的数据使用el表达式进行取值显示）
     * 2. 点击修改按钮，获取到修改的数据，发起ajax请求，提交数据到服务器
     * 3. Controller接受到请求，调用Service进行修改
     */
    @RequestMapping("/adminInfoController_updateAdminInfo")
    public void updateAdminInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AdminDetailsVO vo = (AdminDetailsVO) request.getSession().getAttribute("admin");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");

        //alt + shift + 上/下
        int resultCode = -1;

        //ctrl + alt + t   包裹代码
        if (vo != null) {
            AdminDO adminDO = new AdminDO();
            adminDO.setId(vo.getId());
            adminDO.setName(name);
            adminDO.setPhone(phone);
            int count = adminService.updateAdminInfo(adminDO);
            if (count > 0) {
                //修改成功
                vo.setName(name);
                vo.setPhone(phone);
                request.getSession().setAttribute("admin", vo);
                resultCode = 1;
            }
        }
        response.getWriter().print(resultCode);
    }

    /**
     * 1. 点击修改密码，弹出修改密码的模态框
     * 2. 点击修改密码按钮，通过js获取到修改的数据，发起ajax请求，提交数据到服务器
     * 3. Controller接受到请求，获取旧密码的数据，再与提交过来的原密码进行匹配
     * 匹配
     * 判断两次密码是否一致
     * 一致
     * 调用Service执行修改密码的操作
     * 修改成功，将用户信息重新存入session域中
     * 不一致
     * 返回确认密码不一致
     * 不匹配
     * 返回原密码错误
     */
    @RequestMapping("/adminInfoController_updateAdminPasswrod")
    public void updateAdminPasswrod(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String oldPwd = request.getParameter("oldPwd");
        String newPwd = request.getParameter("newPwd");
        String confirmPwd = request.getParameter("confirmPwd");

        AdminDetailsVO vo = (AdminDetailsVO) request.getSession().getAttribute("admin");
        int resultCode = -1;//原密码不一致
        if (vo != null) {
            if (vo.getPassword().equals(Md5Utils.md5(oldPwd))) {
                if (newPwd.equals(confirmPwd)) {
                    AdminDO adminDO = new AdminDO();
                    adminDO.setId(vo.getId());
                    adminDO.setPassword(Md5Utils.md5(newPwd));
                    adminService.updateAdminPassword(adminDO);
                    vo.setPassword(Md5Utils.md5(newPwd));
                    request.getSession().setAttribute("admin", vo);
                    resultCode = 1;
                } else {
                    resultCode = -2;//确认密码不一致
                }
            }
        } else {
            resultCode = -3;
        }
        response.getWriter().print(resultCode);
    }
}

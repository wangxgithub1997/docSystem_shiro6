package com.jf.weidong.doc.controller;

import com.jf.weidong.doc.domain.data.AdminDO;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.service.AdminService;
import com.jf.weidong.doc.shiro.UsernamePasswordTypeToken;
import com.jf.weidong.doc.utils.Md5Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminLoginController extends BaseController{
    @Autowired
    AdminService adminService;


    //登陆页面
    @RequestMapping("/adminLogin")
    public ModelAndView toLogin(){
        ModelAndView view =new ModelAndView();
        view.setViewName("admin/adminLogin");
        return view;
    }

    //管理员首页
    @RequestMapping("/adminIndex")
    public String toIndex(){
        return "admin/index";
    }

    @RequestMapping("/adminLoginController_login")
    @ResponseBody
    public Integer login(HttpSession httpSession, AdminDO adminDO) throws IOException {

        Subject subject = SecurityUtils.getSubject();
        //使用框架自动加密
        UsernamePasswordTypeToken token=
                new UsernamePasswordTypeToken(adminDO.getUsername(),adminDO.getPassword(),UsernamePasswordTypeToken.ADMIN);
        int resultCode = 1;
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            resultCode=-2;
            return resultCode;
        }
        AdminDetailsVO vo = (AdminDetailsVO) SecurityUtils.getSubject().getPrincipal();
        httpSession.setAttribute("admin",vo);
        return resultCode;
    }

    /*@RequestMapping("/adminLoginController_logout")
    public RedirectView logout(HttpSession httpSession) throws IOException {
        httpSession.removeAttribute("admin");
        *//*response.sendRedirect("adminLogin.jsp");*//*
        return new RedirectView("/admin/adminLogin");
    }*/
}

package com.jf.weidong.doc.filter;
import com.jf.weidong.doc.domain.data.AuthorizationDO;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 登陆状态校验
 */
public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        /**
         * 1. 判断是否登录
         *  获取session域中的admin值
         *      如果为空：未登录
         *             跳转到登陆页面
         *      不为空：登陆
         *              放行
         */
        AdminDetailsVO vo = (AdminDetailsVO) request.getSession().getAttribute("admin");
        if (vo == null) {
            response.sendRedirect(request.getContextPath() + "/adminLogin.jsp");
            return;
        }

        String path = request.getRequestURI();

        if (path.contains("/index.jsp")) {
            System.out.println("index.jsp 通行");
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        AuthorizationDO au = vo.getAuthorization();


        //超级管理员不需要拦截
        if (au.getSuperSet() == 1) {
            System.out.println("超级管理员通行");
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        /**
         * 权限控制
         *  原理：
         *      拦截所有的管理员请求
         *      /admin/*
         *      获取到请求的路径，并判断是否有对应的权限
         *      例如：
         *          bookSet
         *          bookManageController_*
         *          如果请求的路径为bookManageController
         *          判断bookSet 的的值是否为1
         *              为1：表示有该权限
         *                  放行
         *              不为1：没有该权限
         *                  跳转到nopass.jsp
         *  不拦截的：
         *      管理员主页：index.jsp
         *      超级管理员
         */


        boolean allowInto = true;

        //如果路径包含图书管理
        if(path.contains("bookManageController") && au.getBookSet() == 1){
            allowInto = true;
        }else if(path.contains("readerTypeManageController") && au.getSysSet() == 1) {//读者类型
            allowInto = true;
        }else if(path.contains("readerManageController") && au.getReaderSet() == 1) {//读者管理
            allowInto = true;
        }else if(path.contains("bookTypeManageController") && au.getTypeSet() == 1) {//图书分类
            allowInto = true;
        }else{
            allowInto = false;
        }
        if(allowInto) {
            filterChain.doFilter(request, response);
        }else{
            request.getRequestDispatcher("/error/nopass.jsp").forward(request, response);
        }
    }
}

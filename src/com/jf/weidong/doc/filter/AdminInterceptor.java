package com.jf.weidong.doc.filter;

import com.jf.weidong.doc.domain.data.AuthorizationDO;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("访问地址是："+request.getRequestURI());
        AdminDetailsVO vo = (AdminDetailsVO) request.getSession().getAttribute("admin");
        if (vo == null) {
            response.sendRedirect(request.getContextPath() + "/admin/adminLogin");
            return false;
        }

        String path = request.getRequestURI();
        AuthorizationDO au = vo.getAuthorization();
        //超级管理员不需要拦截
        if (au.getSuperSet() == 1) {
            System.out.println("超级管理员通行");
            return true;
        }

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
            return true;
        }else{
            request.getRequestDispatcher("/error/nopass.jsp").forward(request, response);
        }
        return super.preHandle(request,response,handler);
    }
}

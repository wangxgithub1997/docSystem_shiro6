package com.jf.weidong.doc.controller;

import com.jf.weidong.doc.domain.data.AdminDO;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.interfaces.PBEKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class TestController {

    @RequestMapping("/t1")
    public void t1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //哈哈提交
        response.getWriter().write("hello  1");
    }

    @RequestMapping("/t2")
    public void t2() throws IOException {
        //哈哈提交
        System.out.println("t2 被访问了");
    }

    @RequestMapping("/t3")
    public void t3(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //哈哈提交
        response.getWriter().write("hello   3");
    }


    @RequestMapping("/t4")
    public ModelAndView t4()  {
        ModelAndView view = new ModelAndView();
        view.setViewName("/test.jsp");//访问t4方法后跳转到的页面
        //将msg存入域中，页面通过${msg}进行取值
        view.addObject("msg1", "我是Controller传递过来的数据");
        view.addObject("msg2", "我是第二个数据");
        return view;
    }


    @RequestMapping("/t5")
    public ModelAndView t5() throws IOException {
        ModelAndView view = new ModelAndView();
        view.addObject("msg1","消息1");
        view.setViewName("/test.jsp");
        return view;
    }

    //错误的
    @RequestMapping("/t6")
    public String t6() throws IOException {
        ModelAndView view = new ModelAndView();
        view.addObject("msg1","消息1");
        view.setViewName("/test.jsp");
        return "";
    }


    /**
     * http://localhost:7777/login.action?name=lisi&password=admin
     * 参数绑定
     * 1. 获取方法的参数名称列表
     *    名称必须和请求时携带的参数名称一致
     * 2. Controller方法中的参数类型和使用反射调用方法时的类型不一致
     */
    @RequestMapping("/login")
    public void login(String name,String password) throws IOException {
//        String name = request.getParameter("name");
//        String password = request.getParameter("password");
        System.out.println(name+"   " + password);
    }


    @RequestMapping("/t7")
    public void t7(Integer i1) throws IOException {
        System.out.println("获取到的参数："+i1);
    }

    @RequestMapping("/t8")
    public void t8(double d1) throws IOException {
        System.out.println("获取到的参数："+d1);
    }


    @RequestMapping("/t9")
    public void t9(String name,int num) throws IOException {
        System.out.println("获取到的参数："+name +"   "+ num);
    }

    /**
     * http://localhost:7777/login.action?username=lisi&password=admin
     * @throws IOException
     */
    @RequestMapping("/10")
    public void t10(AdminDO adminDO) {
        System.out.println(adminDO.getUsername()+ "   " + adminDO.getPassword());
    }


}
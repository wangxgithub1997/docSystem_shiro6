package com.jf.weidong.doc.controller;

import com.jf.weidong.doc.domain.data.AuthorizationDO;
import com.jf.weidong.doc.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.jf.weidong.doc.utils.myspringmvc.RequestContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class AuthorizationController {
    @Autowired
    AuthorizationService authorizationService ;//= new AuthorizationService();

    @RequestMapping("/admin/authorizationController_getAuthorization")
    @ResponseBody
    public AuthorizationDO getAuthorization(Integer id) throws IOException {
        AuthorizationDO authorizationById = authorizationService.getAuthorizationById(new AuthorizationDO(id));
        return authorizationById;
    }

    @RequestMapping("/admin/authorizationController_addAuthorization")
    public void addAuthorization(int id, String power) throws IOException {
        AuthorizationDO a = new AuthorizationDO(id, 0,
                0, 0, 0, 0, 0, 0);

        String[] powers = power.split(",");
        for (String s : powers) {
            if (s.equals("typeSet")) {
                //图书分类设置权限
                a.setTypeSet(1);
            }
            if (s.equals("bookSet")) {
                //图书分类设置权限
                a.setBookSet(1);
            }
            if (s.equals("readerSet")) {
                //图书分类设置权限
                a.setReaderSet(1);
            }
            if (s.equals("borrowSet")) {
                //图书分类设置权限
                a.setBorrowSet(1);
            }
            if (s.equals("backSet")) {
                //图书分类设置权限
                a.setBackSet(1);
            }
            if (s.equals("forfeitSet")) {
                //图书分类设置权限
                a.setForfeitSet(1);
            }
            if (s.equals("sysSet")) {
                //图书分类设置权限
                a.setSysSet(1);
            }
        }
        int count = authorizationService.updateAuthorizationById(a);
        int resultCode = -1;
        if (count > 0) {
            resultCode = 1;
        }
        RequestContextHolder.getResponse().getWriter().print(resultCode);

    }


}

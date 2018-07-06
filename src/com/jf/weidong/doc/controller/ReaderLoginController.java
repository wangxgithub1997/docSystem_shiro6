package com.jf.weidong.doc.controller;

import com.jf.weidong.doc.domain.data.ReaderDO;
import com.jf.weidong.doc.service.ReaderService;
import com.jf.weidong.doc.utils.Md5Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ReaderLoginController extends BaseController{

    @RequestMapping("/readerLoginController_login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String paperNO = request.getParameter("paperNO");
        String password = request.getParameter("password");

        ReaderDO readerDO = new ReaderDO();
        readerDO.setPaperNO(paperNO);
        readerDO.setPassword(Md5Utils.md5(password));

        ReaderService readerService = new ReaderService();

        ReaderDO newDo = readerService.getReaderByPNO(readerDO);

        int resultCode = 1;

        if (newDo == null) {
            resultCode = -1;
        }else if(!newDo.getPassword().equals(readerDO.getPassword())){
            resultCode = -2;
        }else{
            request.getSession().setAttribute("reader",newDo);
        }
        response.getWriter().print(resultCode);
    }

    @RequestMapping("/readerLoginController_logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute("reader");
        response.sendRedirect("readerLogin");
    }

}

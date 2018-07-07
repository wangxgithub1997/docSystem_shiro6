package com.jf.weidong.doc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    @RequestMapping("/nopass")
    public ModelAndView nopass(){
        return new ModelAndView("error/nopass");
    }
}

package com.jf.weidong.doc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reader")
public class ReaderInfoController {

    @RequestMapping("/readerLogin")
    public String login(){
        return "reader/readerLogin";
    }

    @RequestMapping("/toIndex")
    public String toIndex(){
        return "reader/index";
    }
}

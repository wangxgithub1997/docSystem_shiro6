package com.jf.weidong.doc.controller;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.ReaderDO;
import com.jf.weidong.doc.domain.query.Query;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.domain.vo.BorrowInfoListVO;
import com.jf.weidong.doc.service.BorrowManageService;
import com.jf.weidong.doc.utils.DataUtils;
import com.jf.weidong.doc.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import com.jf.weidong.doc.utils.myspringmvc.RequestContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class BorrowManageController {
    @Autowired
    BorrowManageService borrowManageService;// = new BorrowManageService();

    /**
     * 图书借阅列表
     */
    @RequestMapping("/admin/borrowManageController_list")
    public ModelAndView list(Query query) {
        query.setPageCode(DataUtils.getPageCode(query.getPageCode()+""));
        PageBean<BorrowInfoListVO> pb = borrowManageService.pageSearch(query);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/borrowManage");
        modelAndView.addObject("pb", pb);
        return modelAndView;
    }

    /**
     * 管理员管理列表
     *
     * @return
     */
    @RequestMapping("/admin/borrowManageController_borrowBook")
    public void borrowBook(ReaderDO readerDO,String ISBN) throws IOException {
        readerDO.setPassword(Md5Utils.md5(readerDO.getPassword()));
        AdminDetailsVO adminDetailsVO = (AdminDetailsVO) RequestContextHolder.getSession().getAttribute("admin");
        int state = borrowManageService.addborrow(readerDO, ISBN, adminDetailsVO);
        RequestContextHolder.getResponse().getWriter().print(state);
    }





}

package com.jf.weidong.doc.controller;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.BackInfoDO;
import com.jf.weidong.doc.domain.query.BackListQuery;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.domain.vo.BackListVO;
import com.jf.weidong.doc.service.BackManagerService;
import com.jf.weidong.doc.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import com.jf.weidong.doc.utils.myspringmvc.RequestContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class BackManageController {
    @Autowired
    BackManagerService backManagerService ;//= new BackManagerService();

    /**
     * 管理员管理列表
     *
     * @return
     */
    @RequestMapping("/admin/backManageController_list")
    public ModelAndView list(BackListQuery query) {
        query.setPageCode(DataUtils.getPageCode(query.getPageCode() + ""));
        PageBean<BackListVO> pb = backManagerService.pageSearch(query);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/backManage");
        modelAndView.addObject("pb", pb);
        modelAndView.addObject("query", query);
        return modelAndView;
    }


    @RequestMapping("/admin/backManageController_backBook")
    public void backBook(int borrowId) throws IOException {
        AdminDetailsVO adminDetailsVO = (AdminDetailsVO) RequestContextHolder.getSession().getAttribute("admin");
        int result = backManagerService.addBackInfo(new BackInfoDO(borrowId),adminDetailsVO);
        RequestContextHolder.getResponse().getWriter().print(result);
    }




}

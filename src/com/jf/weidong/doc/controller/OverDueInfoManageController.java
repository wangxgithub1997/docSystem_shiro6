package com.jf.weidong.doc.controller;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.query.OverdueListQuery;
import com.jf.weidong.doc.domain.vo.OverdueListVO;
import com.jf.weidong.doc.service.OverDueInfoService;
import com.jf.weidong.doc.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 逾期处理
 */

@Controller
public class OverDueInfoManageController {
    @Autowired
    private OverDueInfoService forfeitService ;//= new OverDueInfoService();


    @RequestMapping("/admin/overDueManageController_list")
    public ModelAndView list(OverdueListQuery query) throws Exception {
        //获取页面传递过来的当前页码数
        int pageCode = DataUtils.getPageCode(query.getPageCode()+"");
        query.setPageCode(pageCode);
        PageBean<OverdueListVO> pb = forfeitService.pageSearch(query);
        ModelAndView view =new ModelAndView();
        view.addObject("pb", pb);
        view.addObject("query", query);
        view.setViewName("admin/overdueManage");
        return view;
    }


}

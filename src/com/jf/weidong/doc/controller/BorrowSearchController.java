package com.jf.weidong.doc.controller;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.BorrowInfoDO;
import com.jf.weidong.doc.domain.query.BorrowSearchQuery;
import com.jf.weidong.doc.domain.vo.BorrowSearchListVO;
import com.jf.weidong.doc.service.BorrowSearchService;
import com.jf.weidong.doc.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import com.jf.weidong.doc.utils.myspringmvc.RequestContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * 借阅查询
 */
@Controller
public class BorrowSearchController {
    @Autowired
    private BorrowSearchService borrowSearchService ;//= new BorrowSearchService();

    /**
     * 列表查询
     */
    @RequestMapping("/admin/borrowSearchController_list")
    public ModelAndView list(BorrowSearchQuery query) throws Exception {
        //获取页面传递过来的当前页码数
        int pageCode = DataUtils.getPageCode(query.getPageCode()+"");
        query.setPageCode(pageCode);
        PageBean<BorrowSearchListVO> pb = borrowSearchService.pageSearch(query);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/borrowSearch");
        modelAndView.addObject("pb",pb);
        modelAndView.addObject("query",query);
        return modelAndView;

    }


    @RequestMapping("/admin/borrowManageController_renewBook")
    public void renewBook(int borrowId) throws IOException {
        int state  = borrowSearchService.renewBook(new BorrowInfoDO(borrowId));
        RequestContextHolder.getResponse().getWriter().print(state);
    }
}

package com.jf.weidong.doc.controller;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.AdminDO;
import com.jf.weidong.doc.domain.query.AdminListQuery;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.service.AdminManageService;
import com.jf.weidong.doc.utils.DataUtils;
import com.jf.weidong.doc.utils.Result;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.jf.weidong.doc.utils.myspringmvc.RequestContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class AdminManageController {
    @Autowired
    AdminManageService adminManageService ;//= new AdminManageService();

    /**
     * 管理员管理列表
     *
     * @return
     */
    @RequestMapping("/admin/adminManageController_list")
    public ModelAndView list(AdminListQuery query){
        query.setPageCode(DataUtils.getPageCode(query.getPageCode()+""));
        PageBean<AdminDetailsVO> pb = adminManageService.pageSearch(query);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/adminManage");
        modelAndView.addObject("pb", pb);
        modelAndView.addObject("query", query);
        return modelAndView;
    }


    @RequestMapping("/admin/adminManageController_addAdmin")
    @ResponseBody
    public Result addAdmin(AdminDO adminDO) throws IOException {
        int i = adminManageService.addAdmin(adminDO);
        int resultCode = -1;
        if (i > 0) {
            resultCode = 1;
        }
        Result result=new Result(resultCode,"添加成功");
        return result;
    }
}

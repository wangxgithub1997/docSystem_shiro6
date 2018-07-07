package com.jf.weidong.doc.controller;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.ReaderTypeDO;
import com.jf.weidong.doc.domain.query.Query;
import com.jf.weidong.doc.service.ReaderTypeService;
import com.jf.weidong.doc.utils.DataUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 读者管理【系统设置】
 */

@Controller
public class ReaderTypeManageController {
    @Autowired
    ReaderTypeService service ;//= new ReaderTypeService();

    @RequestMapping("/admin/readerTypeManageController_list")
    @RequiresPermissions(value = {"sysSet","superSet"},logical =Logical.OR )
    public ModelAndView list(Query query) throws IOException, ServletException {
        query.setPageCode(DataUtils.getPageCode(query.getPageCode()+""));
        PageBean<ReaderTypeDO> pb = service.pageSearch(query);
        ModelAndView view =new ModelAndView();
        view.addObject("pb", pb);
        view.setViewName("admin/readerTypeManage");
        return view;
    }

    @RequestMapping("/admin/readerTypeManageController_getAllReaderTypes")
    public void getAllReaderTypes(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<ReaderTypeDO> types = service.getReaderTypes();
        JSONArray jsonArray = JSONArray.fromObject(types);
        response.getWriter().print(jsonArray.toString());
    }

    @RequestMapping("/admin/readerTypeManageController_getReaderTypeById")
    public void getReaderTypeById( HttpServletRequest request, HttpServletResponse response) throws IOException {
        ReaderTypeDO typeDO = new ReaderTypeDO();
        typeDO.setId(Integer.parseInt(request.getParameter("id")));
        ReaderTypeDO type = service.getReaderTypeById(typeDO);
        JSONObject jsonObject = JSONObject.fromObject(type);
        response.getWriter().print(jsonObject.toString());
    }


    @RequestMapping("/admin/readerTypeManageController_getReaderTypeByName")
    public void getReaderTypeByName( HttpServletRequest request, HttpServletResponse response) throws IOException {
        ReaderTypeDO typeDO = new ReaderTypeDO();
        typeDO.setName(request.getParameter("name"));
        ReaderTypeDO type = service.getReaderTypeByName(typeDO);
        int resultCode = -1;
        if (type == null) {
            resultCode = 1;
        }
        response.getWriter().print(resultCode);
    }


    @RequestMapping("/admin/readerTypeManageController_updateReaderType")
    public void updateReaderType( HttpServletRequest request, HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        ReaderTypeDO typeDO = new ReaderTypeDO();
        BeanUtils.copyProperties(typeDO, request.getParameterMap());
        int count = service.updateReaderType(typeDO);
        int resultCode = -1;
        if (count > 0) {
            resultCode = 1;
        }
        response.getWriter().print(resultCode);
    }


    @RequestMapping("/admin/readerTypeManageController_addReaderType")
    public void addReaderType( HttpServletRequest request, HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        ReaderTypeDO typeDO = new ReaderTypeDO();
        BeanUtils.copyProperties(typeDO, request.getParameterMap());
        int count = service.addReaderType(typeDO);
        int resultCode = -1;
        if (count > 0) {
            resultCode = 1;
        }
        response.getWriter().print(resultCode);
    }




}

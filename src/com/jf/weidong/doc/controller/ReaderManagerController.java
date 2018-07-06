package com.jf.weidong.doc.controller;


import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.ReaderDO;
import com.jf.weidong.doc.domain.query.ReaderQuery;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.domain.vo.ReaderDetailsVO;
import com.jf.weidong.doc.domain.vo.ReaderListVO;
import com.jf.weidong.doc.service.ReaderManagerService;
import com.jf.weidong.doc.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.jf.weidong.doc.utils.myspringmvc.RequestContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Controller
public class ReaderManagerController {
    @Autowired
    ReaderManagerService readerManagerService;// = new ReaderManagerService();
    /**
     * 分页查询读者信息
     */
    @RequestMapping("/admin/readerManageController_list")
    public ModelAndView list(ReaderQuery query) throws Exception {
        PageBean<ReaderListVO> pb = readerManagerService.pageSearch(query);
            ModelAndView view =new ModelAndView();
            view.addObject("pb",pb);
            view.addObject("query",query);
            view.setViewName("admin/readerManage");
            return view;
    }
    /**
     * 根据读者id查询读者详情
     */
    @RequestMapping("/admin/readerManageController_getReaderInfo")
    public void getReaderInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReaderDO readerDO = new ReaderDO();
        readerDO.setId(Integer.parseInt(request.getParameter("id")));
        ReaderDetailsVO vo = readerManagerService.getReaderDetails(readerDO);
        JSONObject jsonObject = JSONObject.fromObject(vo);

        response.getWriter().print(jsonObject);

    }

    @RequestMapping("/admin/readerManageController_getReader")
    public void getReader(HttpServletRequest request, HttpServletResponse response) throws Exception {
         getReaderInfo(request,response);
    }

    /**
     * 更新读者信息
     */
    @RequestMapping("/admin/readerManageController_updateReader")
    public void updateReader(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReaderDO readerDO = new ReaderDO();
        BeanUtils.copyProperties(readerDO, request.getParameterMap());
        readerDO.setId(Integer.parseInt(request.getParameter("readerId")));
        readerDO.setFk_readerType(Integer.parseInt(request.getParameter("readerType")));
        int count = readerManagerService.updateReader(readerDO);
        int resultCode = -1;
        if (count > 0) {
            resultCode = 1;
        }
        response.getWriter().print(resultCode);
    }



    @RequestMapping("/admin/readerManageController_exportReader")
    public void exportReader(HttpServletResponse response) throws IOException {
        String filename = readerManagerService.exportReader();
        response.getWriter().write("/fileDownloadController_fileDownload.action?fileType=4&fileName="+filename);
    }



    @RequestMapping("/admin/readerManageController_addReader")
    public void addReader(ReaderDO readerDO,int readerType) throws IOException {
        AdminDetailsVO vo = (AdminDetailsVO) RequestContextHolder.getSession().getAttribute("admin");
        readerDO.setCreateTime(new Date());
        readerDO.setFk_admin(vo.getId());
        readerDO.setFk_readerType(readerType);
        readerDO.setPassword(Md5Utils.md5("123456"));

        //判断读者是否重复

        ReaderDetailsVO paperNO = readerManagerService.getReaderDetailsByPaperNO(readerDO);

        int resultCode = -2;
        if (paperNO == null) {
            int count = readerManagerService.addReader(readerDO);
            if (count > 0) {
                resultCode = 1;
            }
        }else{
            resultCode = -1;
        }
        System.out.println(readerDO);
        RequestContextHolder.getResponse().getWriter().print(resultCode);
    }

    @RequestMapping("/admin/readerManageController_batchAddReader")
    public void batchAddReader(String fileName) throws IOException {
        AdminDetailsVO vo = (AdminDetailsVO) RequestContextHolder.getSession().getAttribute("admin");
        JSONObject jsonObject = readerManagerService.batchAddReader(fileName, vo);
        System.out.println(fileName);
        RequestContextHolder.getResponse().getWriter().print(jsonObject);

    }


}

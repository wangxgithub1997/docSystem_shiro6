package com.jf.weidong.doc.controller;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.BookDO;
import com.jf.weidong.doc.domain.data.BookTypeDO;
import com.jf.weidong.doc.domain.query.BookManageQuery;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.domain.vo.BookDetailsVO;
import com.jf.weidong.doc.domain.vo.BookManageListVO;
import com.jf.weidong.doc.service.BookManageService;
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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * 图书管理
 */
@Controller
public class BookManageController {
    @Autowired
    BookManageService service ;//= new BookManageService();

    @RequestMapping("/admin/bookManageController_list")
    @RequiresPermissions(value = {"bookSet","superSet"},logical =Logical.OR )
    public ModelAndView list(BookManageQuery query)  {
        query.setPageCode(DataUtils.getPageCode(query.getPageCode()+""));
        query.setStart((query.getPageCode()-1)*query.getPageSize());
        PageBean<BookManageListVO> pb = service.pageSearch(query);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pb", pb);
        modelAndView.addObject("query",query);
        modelAndView.setViewName("admin/bookManage");
        return modelAndView;
    }


    /**
     * 图书分类下拉列表
     * 1. 在图书管理页面加载成功后，发起ajax请求，请求图书分类列表
     * 2. Controller接受到请求，调用Service查询所有的图书分类，并以json格式返回
     * 3. ajax获取到json格式的数据，通过for循环遍历分类数据，并创建option，插入到指定的
     * select 中
     */
    @RequestMapping("/admin/bookManageController_getAllBookTypes")
    @ResponseBody
    public String getAllBookTypes() throws IOException {
        System.out.println("访问了");
        List<BookTypeDO> types = service.getAllBookTypes();
        String json = JSONArray.fromObject(types).toString();
        //response.getWriter().print(json);
        return json;
    }

    /**
     * 添加图书
     * 1. 点击添加图书，发起ajax请求，请求图书分类信息，使用for循环遍历图书分类信息
     * 创建option插入到select中，并弹出模态框
     * 2. 点击添加按钮，发起ajax请求，提交数据
     * 3.
     */
    @RequestMapping("/admin/bookManageController_addBook")
    public void addBook(BookDO bookDO,int bookTypeId) throws IOException {
        AdminDetailsVO vo = (AdminDetailsVO)RequestContextHolder.getSession().getAttribute("admin");
        bookDO.setFk_booktype(bookTypeId);
        bookDO.setFk_admin(vo.getId());
        //上架时间为添加时间
        bookDO.setPutdate(new Date(System.currentTimeMillis()));
        //数量
        bookDO.setCurrentNum(bookDO.getNum());
        BookDO byISBN = service.getBookByISBN(bookDO);
        int resultCode = -1;
        if (byISBN == null) {
            int count = service.addBook(bookDO);
            if (count > 0) {
                resultCode = 1;
            }
        } else {
            resultCode = -2;
        }
        RequestContextHolder.getResponse().getWriter().print(resultCode);
    }
    /**
     * 1. 点击列表的查看按钮，调用js方法，传递图书的id，发起ajax请求，根据id查询指定的图书信息
     * 2. Controller接受到请求，返回图书的信息
     * 3. ajax获取到图书信息，通过js设置文件到文本框中
     */
    @RequestMapping("/admin/bookManageController_getBookInfo")
    @ResponseBody
    public String getBookInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BookDO bookDO = new BookDO();
        bookDO.setId(Integer.parseInt(request.getParameter("bookId")));
        BookDetailsVO bookInfo = service.getBookInfo(bookDO);
        //bookInfo --> json
        String json = JSONObject.fromObject(bookInfo).toString();
        return json;
    }

    /**
     * 更新书籍信息
     */
    @RequestMapping("/admin/bookManageController_updateBook")
    @ResponseBody
    public Result updateBook(HttpServletRequest request) throws InvocationTargetException, IllegalAccessException, IOException {
        BookDO bookDO = new BookDO();
        bookDO.setId(Integer.parseInt(request.getParameter("bookId")));
        bookDO.setName(request.getParameter("bookName"));
        bookDO.setFk_booktype(Integer.parseInt(request.getParameter("bookTypeId")));
        BeanUtils.copyProperties(bookDO, request.getParameterMap());
        System.out.println(bookDO);
        //更新指定的字段
        int count = service.updateBook(bookDO);
        int resultCode = -1;
        if (count > 0) {
            resultCode = 1;
        }
        return new Result(resultCode);
    }

    /**
     * 修改书籍数量
     */
    @RequestMapping("/admin/bookManageController_addBookNum")
    public void addBookNum(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int num = Integer.parseInt(request.getParameter("num"));
        BookDO bookDO = new BookDO();
        bookDO.setId(bookId);
        bookDO.setNum(num);
        int count = service.addBookNum(bookDO);
        int resultCode = -1;
        if (count > 0) {
            resultCode = 1;
        }
        response.getWriter().print(resultCode);
    }

    /**
     * 1. 点击导出发起ajax请求，获取文件的下载地址
     */
    @RequestMapping("/admin/bookManageController_exportBook")
    public void exportBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //生成文件
        String book = service.exportBook();
        response.getWriter().print(book);
    }


    /**
     * 1. 修改表单
     * <form action="/admin/fileUploadController_fileUpload.action" method="post"
     *                       enctype="multipart/form-data" id="uploadForm">
     * 2. 什么时候提交文件（提交表单）
     *  监听表单的<input>的变动事件
     *  <input type="file" id="upload" name="upload"/><br/>
     *  监听的写法
     *  $(document).on('change',"#upload",function () {});
     *
     * 3. 提交表单
     *      //提交表单（模拟一个点击上传的操作）
     *      $("#uploadForm").ajaxSubmit({
     *  必须导入：
     *      jquery.form.js
     *      commons-fileupload-1.2.1.jar
     *      commons-io-1.3.2.jar
     * 4. Controller，接受上传的文件
     *     基本的配置
     *     DiskFileItemFactory factory = new DiskFileItemFactory();
     *     ServletFileUpload upload = new ServletFileUpload(factory);
     *     解析请求的内容获取文件数据
     *     List<FileItem> list = upload.parseRequest(request);
     *     移动文件到指定的目录
     *     String fileName = System.currentTimeMillis()+"_"+file.getName();
     *     File newFile = new File(Constant.uploadPath+fileName);
     *     //保存到指定的目录
     *     file.write(newFile);
     *
     * 点击添加时需要传递的数据：
     *  文件名
     */
    @RequestMapping("/admin/bookManageController_batchAddBook")
    public void batchAddBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AdminDetailsVO vo = (AdminDetailsVO) request.getSession().getAttribute("admin");
        JSONObject jsonObject = service.batchAddBook(request.getParameter("fileName"), vo);
        response.getWriter().write(jsonObject.toString());
    }
}

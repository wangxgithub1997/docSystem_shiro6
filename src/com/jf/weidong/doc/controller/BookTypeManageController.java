package com.jf.weidong.doc.controller;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.BookTypeDO;
import com.jf.weidong.doc.domain.query.BookTypeQuery;
import com.jf.weidong.doc.service.BookTypeManageService;
import com.jf.weidong.doc.utils.DataUtils;
import com.jf.weidong.doc.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 图书类型管理
 */

@Controller
public class BookTypeManageController extends BaseController {
    @Autowired
    BookTypeManageService service ;//= new BookTypeManageService();
    /**
     * 1. 点击图书分类管理，请求bookTypeManageController_list
     * 2. list方法里面获取到页码，调用Service进行分页查询，并使用PageBean封装数据
     * 3. 获取到PageBean，转发到bookTypeManage.jsp，使用jstl表达式判断PageBean里面的list是否为空
     *      为空
     *          显示暂无数据
     *      不为空
     *          使用<c:forEach>遍历list集合，创建每一行
     * 4. 添加条件查询
     *
     * 5.分页条
     *      <jsp:include page="../share/page.jsp" />
     *   页码的点击事件
     *      点击页码会调用page()方法，此方法由使用者来定义，为什么？
     *          因为分页条在整个项目中是通用的，但是在请求分页数据时需要携带参数（当前页码、查询条件）不一样
     *          所以只能将具体的实现由使用者来编写
     */
    @RequestMapping("/admin/bookTypeManageController_list")
    public ModelAndView list(BookTypeQuery query) throws Exception {
        query.setPageCode(DataUtils.getPageCode(query.getPageCode()+""));
        PageBean<BookTypeDO> pb = service.pageSearch(query);
        int i=1/0;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/bookTypeManage");
        modelAndView.addObject("pb", pb);
        modelAndView.addObject("query", query);
        return modelAndView;
    }


    /**
     *
     * ctrl + shift + r 浏览器强制刷新（刷新js文件）
     *
     *
     * 删除
     * 1. 给删除按钮绑定点击事件，点击删除发起ajax请求，携带删除的id
     * 2. Controller接受到请求，调用Service根据id删除指定的图书类型
     */
    @RequestMapping("/admin/bookTypeManageController_deleteBookType")
    public void deleteBookType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BookTypeDO typeDO = new BookTypeDO();
        typeDO.setId(Integer.parseInt(request.getParameter("id")));

        int count = service.deleteBookTypeById(typeDO);
        int resultCode = -1;
        if (count > 0) {
            resultCode = 1;
        }
        response.getWriter().print(resultCode);
    }

    /**
     * 修改
     * 1. 点击修改按钮，发起ajax请求，根据图书类型id查询图书信息(用于回显)
     * 将获取到的信息设置到文本框中，并弹出模态框
     * 2. 点击模态框的修改按钮，发起ajax请求，将修改的数据提交到服务器，Controller获取到
     * 数据后，调用Service进行修改
     */
    @RequestMapping("/admin/bookTypeManageController_updateBookType")
    @ResponseBody
    public Result updateBookType(BookTypeDO typeDO) throws IOException {
        int count = service.upadteBookTypeInfo(typeDO);
        int resultCode = -1;
        if (count > 0) {
            resultCode = 1;
        }
        Result result=new Result(resultCode);
        return result;
    }

    /**
     * 获取图书分类信息
     */
    @RequestMapping("/admin/bookTypeManageController_getBookType")
    @ResponseBody
    public Result getBookType(BookTypeDO typeDO) throws IOException {
        BookTypeDO newTypeDO = service.getBookTypeById(typeDO);
        Result result=new Result(1,newTypeDO);
        return result;
    }

    /**
     * 添加
     * 1. 给添加按钮绑定点击事件，点击添加按钮弹出模态框
     * 2. 点击模态框的添加按钮，发起ajax请求，携带图书类型名称
     * 3. Controller接受到请求，调用Service进行添加，需要判断图书类型名称是否存在
     *      存在
     *          返回-1，显示图书类型已经存在
     *      不存在
     *          插入数据库，并返回1，提示添加成功
     */
    @RequestMapping("/admin/bookTypeManageController_addBookType")
    public void addBookType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BookTypeDO typeDO = new BookTypeDO();
        typeDO.setName(request.getParameter("typeName"));

        //判断是否存在
        BookTypeDO oldBookTypeDO = service.getBookTypeByName(typeDO);
        int resultCode = -2;
        if (oldBookTypeDO != null) {
            resultCode = -1;
        }else{

            int count = service.addBookType(typeDO);
            if (count > 0) {
                resultCode = 1;
            }
        }
        response.getWriter().print(resultCode);
    }
}
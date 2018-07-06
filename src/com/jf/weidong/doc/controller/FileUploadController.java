package com.jf.weidong.doc.controller;

import com.jf.weidong.doc.utils.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class FileUploadController {

    /**
     *
     *
     *
     */
    @RequestMapping("/admin/fileUploadController_fileUpload")
    public void fileUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("我被调用了");

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        //解析请求的内容
        List<FileItem> list = upload.parseRequest(request);

        JSONObject jsonObject = new JSONObject();
        for (FileItem file:list) {
            System.out.println(file);
            if (file.getContentType() != null && file.getContentType().equals(Constant.xlsx)) {
                //不支持xlsx
                //{"code":-4,"msg":"不支持此格式的表格文件"}
                jsonObject.put("code", -4);
                jsonObject.put("msg", "不支持此格式(xlsx)的表格文件");
            }else if(file.getContentType().equals(Constant.xls)){
                if(!file.isFormField()){
                    String fileName = System.currentTimeMillis()+"_"+file.getName();
                    //D:/download/upload/book.xls
                    File newFile = new File(Constant.uploadPath+fileName);
                    //保存到指定的目录
                    file.write(newFile);
                    jsonObject.put("code", 1);
                    jsonObject.put("fileName", fileName);
                    jsonObject.put("msg", "上传成功");
                }
            }else{
                jsonObject.put("code", -1);
                jsonObject.put("msg", "不支持的文件类型");
            }
        }
        response.getWriter().write(jsonObject.toString());
    }


}

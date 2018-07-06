package com.jf.weidong.doc.controller;

import com.jf.weidong.doc.utils.Constant;
import com.jf.weidong.doc.utils.DataUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class FileDownloadController {

    /**
     * 下载文件：
     * 参数一：文件名
     * 参数二：文件类型
     *  //1 下载书籍导出
     *  //2 下载模板
     *  //3 下载错误
     *  //4 下载读者导出
     *
     *  http://localhost:7777/fileDownloadController_fileDownload.action?fileType=2&fileName=book.xls
     */
    @RequestMapping("/fileDownloadController_fileDownload")
    public void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = request.getParameter("fileName");
        String fileType = request.getParameter("fileType");

        //判断参数是否有效
        //类型必须是整数
        if(DataUtils.isValid(fileName) && DataUtils.isValid(fileType) && DataUtils.isNumber(fileType)){
            System.out.println("fileName:" + fileName + "fileType: " + fileType);
            int type = Integer.parseInt(fileType);
            String path = null;
            if(type == 1){
                //路径
                path = Constant.exportBook;
            }
            if(type == 2){
                path = Constant.template;
            }

            if(type == 3){
                path = Constant.exportError;
            }
            if (type == 4) {
                path = Constant.exportReader;
            }


            if(path != null){
                File file = new File(path  + fileName);

                if (file.exists()) {
                    //设置文件下载响应头
                    response.setContentType("application/zip");
                    //生成下载的文件名,解决中文文件名不显示的问题
                    response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
                    ServletOutputStream os = response.getOutputStream();
                    InputStream is = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int i = 0;
                    while ((i = is.read(bytes)) > 0) {
                        os.write(bytes, 0, i);
                    }
                    os.flush();
                    is.close();
                    os.close();
                }else{
                    response.getWriter().write("文件不存在");
                }
            }else{
                response.getWriter().write("下载的目录不存在");
            }
        }else{
            response.getWriter().write("缺少参数或者参数不合法");
        }
    }
}

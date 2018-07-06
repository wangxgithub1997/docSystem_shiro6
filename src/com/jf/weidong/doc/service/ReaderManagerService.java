package com.jf.weidong.doc.service;

import com.jf.weidong.doc.dao.ReaderManagerDao;
import com.jf.weidong.doc.dao.ReaderTypeDao;
import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.ReaderDO;
import com.jf.weidong.doc.domain.data.ReaderTypeDO;
import com.jf.weidong.doc.domain.query.ReaderQuery;
import com.jf.weidong.doc.domain.vo.*;
import com.jf.weidong.doc.mapper.ReaderManageMapper;
import com.jf.weidong.doc.utils.*;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReaderManagerService {
    @Autowired
    ReaderManagerDao readerManagerDao ;//= new ReaderManagerDao();
    @Autowired
    ReaderTypeDao readerTypeDao ;//= new ReaderTypeDao();
    @Autowired
    ReaderManageMapper readerManageMapper;

    /**
     * 分页查询读者信息
     */
    public PageBean<ReaderListVO> pageSearch(ReaderQuery query) {
        PageBean<ReaderListVO> pb=new PageBean<>();
        query.setPageCode(DataUtils.getPageCode(query.getPageCode()+""));
        query.setStart((query.getPageCode()-1)*query.getPageSize());
        List<ReaderListVO> list = readerManageMapper.pageSearch(query);
        pb.setList(list);
        Integer totalCount = readerManageMapper.totalCount(query);
        pb.setTotalCount(totalCount);
        pb.setPageSize(query.getPageSize());
        pb.setCurrentPage(query.getPageCode());
        return pb;
    }

    /**
     * 根据读者id查询读者详情
     */
    public ReaderDetailsVO getReaderDetails(ReaderDO readerDO) {
        try {
            return readerManagerDao.getReaderDetails(readerDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据读者证件号查询读者详情
     */
    public ReaderDetailsVO getReaderDetailsByPaperNO(ReaderDO readerDO) {
        try {
            return readerManagerDao.getReaderDetailsByPaperNO(readerDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 更新读者信息
     */
    public int updateReader(ReaderDO r) throws SQLException {
        try {
            return readerManagerDao.updateReader(r);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }



    public String exportReader(){
        try {
            List<ReaderDetailsVO> list = readerManagerDao.exportReader();
            System.out.println(list);
            String name = exportExcel(list);
            return name;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String exportExcel(List<ReaderDetailsVO> detailsVOS) {
        String fileName = null;
        try {
            //用数组存储表头
            String[] title = {"证件号码", "姓名", "读者类型","邮箱", "联系方式"};
            File file = new File(Constant.exportReader);
            // 判断路径是否存在，如果不存在则创建路径
            if (FileUtil.createOrExistsDir(file)) {
                fileName = System.currentTimeMillis() + ".xls";

                File filePath = new File(file, fileName);


                WritableWorkbook excel = Workbook.createWorkbook(filePath);
                WritableSheet sheet = excel.createSheet("sheet1", 0);
                Label label = null;

                // 设置第一行的表头
                for (int i = 0; i < title.length; i++) {
                    label = new Label(i, 0, title[i]);
                    sheet.addCell(label);
                }

                //追加数据

                // 追加 书籍信息
                for (int i = 1; i <= detailsVOS.size(); i++) {
                    label = new Label(0, i, detailsVOS.get(i - 1).getPaperNO() + ""); // label: 列，行，内容
                    sheet.addCell(label);
                    label = new Label(1, i, detailsVOS.get(i - 1).getReaderName()); // label: 列，行，内容
                    sheet.addCell(label);
                    label = new Label(2, i, detailsVOS.get(i - 1).getReaderType()); // label: 列，行，内容
                    sheet.addCell(label);
                    label = new Label(3, i, detailsVOS.get(i - 1).getEmail() + ""); // label: 列，行，内容
                    sheet.addCell(label);

                    label = new Label(4, i, detailsVOS.get(i - 1).getPhone() + ""); // label: 列，行，内容
                    sheet.addCell(label);
                }
                excel.write();
                excel.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return fileName;
    }


    public int addReader(ReaderDO r)  {
        try {
            return readerManagerDao.addReader(r);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public JSONObject batchAddReader(String fileName, AdminDetailsVO adminDetailsVO) {

        ArrayList<ReaderDO> success = new ArrayList<>();
        ArrayList<ReaderExportFailureVO> failure = new ArrayList<>();


        //合法的表头
        String str[] = new String[]{"证件号码", "姓名", "读者类型", "邮箱", "联系方式"};
        File uploadDir = new File(Constant.uploadPath + fileName);
        JSONObject jsonObject = new JSONObject();
        try {
            //加载Excel表格
            Workbook workbook = Workbook.getWorkbook(uploadDir);
            //获取工作表的第一个sheet
            Sheet sheet = workbook.getSheet(0);
            //校验数据格式是否合法
            if (sheet.getColumns() != 5) {
                //列数不够
                //{"code":-1,"msg":"数据格式不合法，请下载模板重新填写数据"}
                jsonObject.put("code", -1);
                jsonObject.put("msg","数据格式不合法，请下载模板重新填写数据");
                return jsonObject;
            }else{
                //遍历数据
                for (int i = 0; i < sheet.getColumns(); i++) {
                    Cell cell = sheet.getCell(i, 0);
                    if (!cell.getContents().equals(str[i])) {
                        jsonObject.put("code", -1);
                        jsonObject.put("msg","数据格式不合法（请不要修改表头）");
                        return jsonObject;
                    }
                }
            }

            //解析excel表格
            //接下来读取数据
            for (int i = 1; i <sheet.getRows() ; i++) {
                String paperNO = sheet.getCell(0, i).getContents().trim();
                String name = sheet.getCell(1, i).getContents().trim();
                String readerType = sheet.getCell(2, i).getContents().trim();
                String email = sheet.getCell(3, i).getContents().trim();
                String phone = sheet.getCell(4, i).getContents().trim();

                //空行--------------------------------------------------------------------------
                if("".equals(paperNO)
                        && "".equals(name)
                        && "".equals(readerType)
                        && "".equals(email)
                        && "".equals(phone)){
                    //数据是空的
                    continue;
                }
                //-------------------------------------------------------------------

                //判断必填的字段---------------------------------------------------
                if("".equals(paperNO) || "".equals(name) || "".equals(readerType)){
                    //缺少关键数据
                    //将失败的数据存入集合中
                    failure.add(ReaderExportFailureVO.createReaderExportFailureVO(paperNO,
                            name, readerType,
                            email, phone, "证件号、名称、读者类型是必填的"));
                    continue;
                }
                //-------------------------------------------------------------------

                ReaderDO readerDO = new ReaderDO();
                readerDO.setName(name);
                readerDO.setPaperNO(paperNO);
                readerDO.setEmail(email);
                readerDO.setPhone(phone);


                //判断证件号是否有重复-------------------------------------------------------------------
                ReaderDetailsVO byPaperNO = readerManagerDao.getReaderDetailsByPaperNO(readerDO);

                if (byPaperNO != null) {
                    failure.add(ReaderExportFailureVO.createReaderExportFailureVO(paperNO,
                            name, readerType,
                            email, phone, "证件号已经存在"));
                    continue;
                }
                //-------------------------------------------------------------------

                //批量插入的数据也有可能是重复的-------------------------------------------------------------------
                boolean isRepeat = false;
                for (int j = 0; j < success.size(); j++) {
                    if (success.get(j).getPaperNO().equals(readerDO.getPaperNO())) {
                        failure.add(ReaderExportFailureVO.createReaderExportFailureVO(paperNO,
                                name, readerType,
                                email, phone, "到数据的数据中有重复的证件号"));
                        isRepeat = true;
                        break;
                    }
                }
                if (isRepeat) {
                    continue;
                }
                //-------------------------------------------------------------------

                //判断读者类型是否存在-------------------------------------------------------------------

                ReaderTypeDO typeByName = readerTypeDao.getReaderTypeByName(new ReaderTypeDO(readerType));
                if (typeByName == null) {
                    failure.add(ReaderExportFailureVO.createReaderExportFailureVO(paperNO,
                            name, readerType,
                            email, phone, "读者类型不存在"));
                    continue;
                }
                readerDO.setFk_readerType(typeByName.getId());

                //判断手机号是否合法-------------------------------------------------------------------
                if (!CheckUtils.checkMobileNumber(readerDO.getPhone())) {
                    failure.add(ReaderExportFailureVO.createReaderExportFailureVO(paperNO,
                            name, readerType,
                            email, phone, "手机号格式不正确"));
                    continue;
                }
                //-------------------------------------------------------------------

                //判断邮箱是否合法-------------------------------------------------------------------
                if (!CheckUtils.checkEmail(readerDO.getEmail())) {
                    failure.add(ReaderExportFailureVO.createReaderExportFailureVO(paperNO,
                            name, readerType,
                            email, phone, "邮箱格式不正确"));
                    continue;
                }
                //-------------------------------------------------------------------
                readerDO.setPassword(Md5Utils.md5("123456"));
                readerDO.setCreateTime(new Date());
                readerDO.setFk_admin(adminDetailsVO.getId());
                success.add(readerDO);
            }
            workbook.close();
            readerManagerDao.batchAddReader(success);

            if (failure.size() != 0) {
                String filName = exportErrorExcel(failure);
                //有失败的数据
                jsonObject.put("code", -4);
                jsonObject.put("msg", "成功"+success.size()+"条,失败"+failure.size()+"条");
                //返回失败文件的下载地址
                jsonObject.put("filePath", filName);
                return jsonObject;
            }else{
                jsonObject.put("code", 1);
                jsonObject.put("msg", "成功"+success.size()+"条");
                return jsonObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 导出excel文件
     */
    public String exportErrorExcel(List<ReaderExportFailureVO> failReaders) {
        //用数组存储表头
        String[] title = {"证件号码", "姓名", "读者类型", "邮箱", "联系方式","导入错误描述"};
        String fileName = System.currentTimeMillis()+"_reader.xls";
        //创建Excel文件
        if (FileUtil.createOrExistsDir(new File(Constant.exportError))) {
            try {
                File file = new File(Constant.exportError, fileName);
                //创建工作簿
                WritableWorkbook workbook = Workbook.createWorkbook(file);
                WritableSheet sheet = workbook.createSheet("sheet1", 0);

                Label label = null;
                //第一行设置列名
                for (int i = 0; i < title.length; i++) {
                    label = new Label(i, 0, title[i]);
                    sheet.addCell(label);
                }
                //追加数据
                for (int i = 1; i <= failReaders.size(); i++) {
                    label = new Label(0, i, failReaders.get(i - 1).getPaperNO());
                    sheet.addCell(label);
                    label = new Label(1, i, failReaders.get(i - 1).getName());
                    sheet.addCell(label);
                    label = new Label(2, i, failReaders.get(i - 1).getReaderType());
                    sheet.addCell(label);
                    label = new Label(3, i, failReaders.get(i - 1).getEmail());
                    sheet.addCell(label);
                    label = new Label(4, i, failReaders.get(i - 1).getPhone());
                    sheet.addCell(label);
                    label = new Label(5, i, failReaders.get(i - 1).getErrorMsg());
                    sheet.addCell(label);
                }
                //写入数据
                workbook.write();
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }




}

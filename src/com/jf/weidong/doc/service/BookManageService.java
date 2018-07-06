package com.jf.weidong.doc.service;

import com.jf.weidong.doc.dao.AdminDao;
import com.jf.weidong.doc.dao.BookManageDao;
import com.jf.weidong.doc.dao.BookTypeManageDao;
import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.AdminDO;
import com.jf.weidong.doc.domain.data.BookDO;
import com.jf.weidong.doc.domain.data.BookTypeDO;
import com.jf.weidong.doc.domain.query.BookManageQuery;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.domain.vo.BookDetailsVO;
import com.jf.weidong.doc.domain.vo.BookExportFailureVO;
import com.jf.weidong.doc.domain.vo.BookManageListVO;
import com.jf.weidong.doc.mapper.AdminMapper;
import com.jf.weidong.doc.mapper.BookManageMapper;
import com.jf.weidong.doc.mapper.BookTypeManageMapper;
import com.jf.weidong.doc.utils.Constant;
import com.jf.weidong.doc.utils.FileUtil;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookManageService{
    @Autowired
    BookManageDao dao ;//= new BookManageDao();
    @Autowired
    BookTypeManageDao bookTypeManageDao;// = new BookTypeManageDao();
    @Autowired
    AdminDao adminDao;// = new AdminDao();
    @Autowired
    BookManageMapper bookManageMapper;
    @Autowired
    BookTypeManageMapper bookTypeManageMapper;
    @Autowired
    AdminMapper adminMapper;
    /**
     * @param query
     * @return
     */
    public PageBean<BookManageListVO> pageSearch(BookManageQuery query) {

        PageBean<BookManageListVO> pb=new PageBean<>();
        //数据集合
        List<BookManageListVO> list = bookManageMapper.pageSearch(query);
        if(!list.isEmpty()){
            pb.setList(list);
        }
        pb.setCurrentPage(query.getPageCode());
        Integer totalCount = bookManageMapper.totalCount(query);
        pb.setTotalCount(totalCount);
        return pb;
    }


    public List<BookTypeDO> getAllBookTypes() {
        try {
            return dao.getAllBookTypes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public int addBook(BookDO bookDO) {
        try {
            return dao.addBook(bookDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public BookDO getBookByISBN(BookDO bookDO) {
        try {
            return dao.getBookByISBN(bookDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过管理员id获取管理员信息VO
     */
    public BookDetailsVO getBookInfo(BookDO bookDO) {

        try {
            BookDetailsVO vo = new BookDetailsVO();
            BookDO book = bookManageMapper.getBookById(bookDO);
            BeanUtils.copyProperties(vo, book);
            vo.setBookName(book.getName());
            //根据书籍信息查询管理员和图书分类
            BookTypeDO bookTypeDO = bookTypeManageMapper.getBookTypeById(new BookTypeDO(book.getFk_booktype()));
            AdminDO adminDO = adminMapper.getAdminById(new AdminDO(book.getFk_admin()));
            vo.setBookType(bookTypeDO.getName());
            vo.setAdminName(adminDO.getName());
            return vo;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
       return null;
    }
    /**
     * 更新书籍信息
     */
    public int updateBook(BookDO bookDO) {
        try {
            return dao.updateBook(bookDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 修改书籍数量
     */
    public int addBookNum(BookDO bookDO) {
        try {
            //获取之前的数据
            BookDO book = dao.getBookById(bookDO);
            //设置数量
            int bookDONum = bookDO.getNum();
            bookDO.setNum(book.getNum() + bookDONum);
            bookDO.setCurrentNum(book.getCurrentNum() + bookDONum);
            return dao.addBookNum(bookDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 生成存储书籍信息的Excel表格，并返回文件的名称
     */
    public String exportBook() {
        try {
            List<BookDetailsVO> list = dao.exportBook();
            System.out.println(list);
            String name = exportExcel(list);
            return name;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String exportExcel(List<BookDetailsVO> bookDetailsVOS) {
        String fileName = null;
        try {
            //用数组存储表头
            String[] title = {"图书ISBN号", "图书类型", "图书名称",
                    "作者名称", "出版社", "价格", "数量", "在馆数量",
                    "上架时间", "操作管理员", "描述"};
            File file = new File(Constant.exportBook);
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
                for (int i = 1; i <= bookDetailsVOS.size(); i++) {
                    label = new Label(0, i, bookDetailsVOS.get(i - 1).getISBN() + ""); // label: 列，行，内容
                    sheet.addCell(label);
                    label = new Label(1, i, bookDetailsVOS.get(i - 1).getBookType()); // label: 列，行，内容
                    sheet.addCell(label);
                    label = new Label(2, i, bookDetailsVOS.get(i - 1).getBookName()); // label: 列，行，内容
                    sheet.addCell(label);
                    label = new Label(3, i, bookDetailsVOS.get(i - 1).getAutho() + ""); // label: 列，行，内容
                    sheet.addCell(label);

                    label = new Label(4, i, bookDetailsVOS.get(i - 1).getPress() + ""); // label: 列，行，内容
                    sheet.addCell(label);

                    label = new Label(5, i, bookDetailsVOS.get(i - 1).getPrice() + ""); // label: 列，行，内容
                    sheet.addCell(label);

                    label = new Label(6, i, bookDetailsVOS.get(i - 1).getNum() + ""); // label: 列，行，内容
                    sheet.addCell(label);

                    label = new Label(7, i, bookDetailsVOS.get(i - 1).getCurrentNum() + ""); // label: 列，行，内容
                    sheet.addCell(label);

                    label = new Label(8, i, bookDetailsVOS.get(i - 1).getPutdate() + ""); // label: 列，行，内容
                    sheet.addCell(label);

                    label = new Label(9, i, bookDetailsVOS.get(i - 1).getAdminName() + ""); // label: 列，行，内容
                    sheet.addCell(label);

                    label = new Label(10, i, bookDetailsVOS.get(i - 1).getDescription() + ""); // label: 列，行，内容
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


    //批量导入
    public JSONObject batchAddBook(String fileName, AdminDetailsVO adminDetailsVO){

        //存储成功【合法】的数据
        List<BookDO> success = new ArrayList<>();
        //存储失败的数据【非法】
        List<BookExportFailureVO> failure = new ArrayList<>();

        //合法的表头
        String str[] = new String[]{"图书ISBN号", "图书类型", "图书名称", "作者名称",
                "出版社", "价格", "数量", "描述"};
        File uploadDir = new File(Constant.uploadPath + fileName);
        JSONObject jsonObject = new JSONObject();
        try {
            //加载Excel表格
            Workbook workbook = Workbook.getWorkbook(uploadDir);
            //获取工作表的第一个sheet
            Sheet sheet = workbook.getSheet(0);
            //校验数据格式是否合法
            if (sheet.getColumns() != 8) {
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

            //接下来读取数据
            for (int i = 1; i <sheet.getRows() ; i++) {
                //i 行
                String ISBN = sheet.getCell(0, i).getContents().trim();
                String type = sheet.getCell(1, i).getContents().trim();
                String bookName = sheet.getCell(2, i).getContents().trim();
                String author = sheet.getCell(3, i).getContents().trim();
                String press = sheet.getCell(4, i).getContents().trim();
                String price = sheet.getCell(5, i).getContents().trim();
                String num = sheet.getCell(6, i).getContents().trim();
                String desc = sheet.getCell(7, i).getContents().trim();
                System.out.println("测试");
                //空行--------------------------------------------------------------------------
                if("".equals(ISBN)
                        && "".equals(type)
                        && "".equals(bookName)
                        && "".equals(author)
                        && "".equals(press)
                        && "".equals(price)
                        && "".equals(num)
                        && "".equals(desc)){
                    //数据是空的
                    continue;
                }
                //-------------------------------------------------------------------


                //判断必填的字段---------------------------------------------------
                if("".equals(ISBN) || "".equals(bookName) || "".equals(price) || "".equals(type)){
                    //缺少关键数据
                    //将失败的数据存入集合中
                    failure.add(BookExportFailureVO.createBookExportFailureVO(ISBN,
                            bookName, author,
                            press, num, price,
                            desc, type, "图书的ISBN、图书名称、价格、图书类型是必填的"));
                    continue;
                }
                //-------------------------------------------------------------------


                //封装数据
                BookDO bookDO = new BookDO();
                bookDO.setName(bookName);
                bookDO.setAutho(author);
                bookDO.setDescription(desc);
                bookDO.setPress(press);
                bookDO.setISBN(ISBN);

                //判断价格是否正确------------------------------------------------------------------------
                try {
                    if(Double.parseDouble(price) <= 0){
                        failure.add(BookExportFailureVO.createBookExportFailureVO(ISBN,
                                bookName, author,
                                press, num, price,
                                desc, type, "价格必须大于0"));
                        continue;
                    }
                    //正确
                    bookDO.setPrice(Double.parseDouble(price));
                } catch (NumberFormatException e) {
                    failure.add(BookExportFailureVO.createBookExportFailureVO(ISBN,
                            bookName, author,
                            press, num, price,
                            desc, type, "价格必须是数字"));
                    continue;
                }
                //-------------------------------------------------------------------


                //判断书的数量   如果不填默认为1---------------------------------------------------------------------
                //ctrl + alt + t
                try {
                    //如果不填，默认为1
                    if ("".equals(num)) {
                        bookDO.setNum(1);
                        bookDO.setCurrentNum(bookDO.getNum());
                    }else{

                        //数量必须是整数，并且大于0
                        if(Integer.parseInt(num) <= 0){
                            failure.add(BookExportFailureVO.createBookExportFailureVO(ISBN,
                                    bookName, author,
                                    press, num, price,
                                    desc, type, "数量必须大于0"));
                            continue;
                        }
                        bookDO.setNum(Integer.parseInt(num));
                        bookDO.setCurrentNum(Integer.parseInt(num));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    failure.add(BookExportFailureVO.createBookExportFailureVO(ISBN,
                            bookName, author,
                            press, num, price,
                            desc, type, "数量必须是数字"));
                    continue;
                }
                //-------------------------------------------------------------------


                //判断插入的数据中是否有重复的ISBN-------------------------------------------
                boolean isRepeat = false;
                for (int j = 0; j < success.size(); j++) {
                    if(success.get(j).getISBN().equals(ISBN)){
                        failure.add(BookExportFailureVO.createBookExportFailureVO(ISBN,
                                bookName, author,
                                press, num, price,
                                desc, type, "在添加的数据中有重复的ISBN"));
                        isRepeat = true;
                        break;
                    }
                }
                if (isRepeat) {
                    continue;
                }
                //-------------------------------------------------------------------


                //ISBN在数据库中也可能重复-------------------------------------------
                //根据isbn查询图书信息
                BookDO byISBN = dao.getBookByISBN(new BookDO(ISBN));
                if (byISBN != null) {
                    failure.add(BookExportFailureVO.createBookExportFailureVO(ISBN,
                            bookName, author,
                            press, num, price,
                            desc, type, "ISBN已经存在"));
                    continue;
                }
                //-------------------------------------------------------------------


                //判断图书类型是否存在--------------------------------------------------------------------------
                //根据图书类型名称查询对应的图书类型
                BookTypeDO byTypeName = bookTypeManageDao.getBookTypeByName(new BookTypeDO(type));
                if (byTypeName == null) {
                    failure.add(BookExportFailureVO.createBookExportFailureVO(ISBN,
                            bookName, author,
                            press, num, price,
                            desc, type, "没有图书类型"));
                    continue;
                }
                bookDO.setFk_booktype(byTypeName.getId());
                //-------------------------------------------------------------------------------------------

                //设置管理员
                bookDO.setFk_admin(adminDetailsVO.getId());
                //如果不登陆系统，那么会自动跳转到登陆页面


                //设置添加的时间
                bookDO.setPutdate(new Date(System.currentTimeMillis()));
                success.add(bookDO);
            }
            workbook.close();
            //插入数据库
            dao.batchAddBook(success);
            if(!failure.isEmpty()){
                //有失败的数据
                jsonObject.put("code", -4);
                jsonObject.put("msg", "成功"+success.size()+"条,失败"+failure.size()+"条");
                String filePath = errorExcel(failure);
                //返回失败文件的下载地址
                jsonObject.put("filePath", filePath);
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

    private String errorExcel(List<BookExportFailureVO> failBooks) {
        String fileName = null;
        try {
            //用数组存储表头
            String[] title = {"图书ISBN号", "图书类型", "图书名称", "作者名称", "出版社", "价格", "数量", "描述", "导入错误描述"};

            File file = new File(Constant.exportError);
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
                for (int i = 1; i <= failBooks.size(); i++) {
                    label = new Label(0, i, failBooks.get(i - 1).getISBN());
                    sheet.addCell(label);
                    label = new Label(1, i, failBooks.get(i - 1).getBooktype());
                    sheet.addCell(label);
                    label = new Label(2, i, failBooks.get(i - 1).getBookName());
                    sheet.addCell(label);
                    label = new Label(3, i, failBooks.get(i - 1).getAuthor());
                    sheet.addCell(label);
                    label = new Label(4, i, failBooks.get(i - 1).getPress());
                    sheet.addCell(label);
                    label = new Label(5, i, failBooks.get(i - 1).getPrice());
                    sheet.addCell(label);
                    label = new Label(6, i, failBooks.get(i - 1).getNum());
                    sheet.addCell(label);
                    label = new Label(7, i, failBooks.get(i - 1).getDescription());
                    sheet.addCell(label);
                    label = new Label(8, i, failBooks.get(i - 1).getErrorMsg());
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


}

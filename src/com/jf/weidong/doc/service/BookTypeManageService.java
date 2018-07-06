package com.jf.weidong.doc.service;

import com.jf.weidong.doc.dao.BookTypeManageDao;
import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.BookTypeDO;
import com.jf.weidong.doc.domain.query.BookTypeQuery;
import com.jf.weidong.doc.mapper.BookTypeManageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class BookTypeManageService {
    @Autowired
    BookTypeManageDao bookTypeManageDao ;//= new BookTypeManageDao();
    @Autowired
    BookTypeManageMapper bookTypeManageMapper;
    public PageBean<BookTypeDO> pageSearch(BookTypeQuery query) {
        query.setStart((query.getPageCode()-1)*query.getPageSize());
            PageBean<BookTypeDO> pb =new PageBean<>();
            List<BookTypeDO> bookTypeDOS = bookTypeManageMapper.pageSearch(query);
            if(!bookTypeDOS.isEmpty()){
                pb.setList(bookTypeDOS);
            }
        Integer totalCount = bookTypeManageMapper.totalCount(query);
        pb.setTotalCount(totalCount);
        pb.setCurrentPage(query.getPageCode());
        pb.setPageSize(query.getPageSize());
        return pb;
    }

    public int deleteBookTypeById(BookTypeDO typeDO) {
        try {
            return bookTypeManageDao.deleteBookTypeById(typeDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public BookTypeDO getBookTypeById(BookTypeDO typeDO){
         // int i=1/0;//异常
        return bookTypeManageMapper.getBookTypeById(typeDO.getId());
    }


    public int upadteBookTypeInfo(BookTypeDO typeDO){
        int i=1/0;
        return bookTypeManageMapper.upadteBookTypeInfo(typeDO);
    }

    public BookTypeDO getBookTypeByName(BookTypeDO typeDO){
        try {
            return bookTypeManageDao.getBookTypeByName(typeDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int addBookType(BookTypeDO typeDO){
        try {
            return bookTypeManageDao.addBookType(typeDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }




}

package com.jf.weidong.doc.service;

import com.jf.weidong.doc.dao.ReaderTypeDao;
import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.ReaderTypeDO;
import com.jf.weidong.doc.domain.query.Query;
import com.jf.weidong.doc.mapper.ReaderTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ReaderTypeService {
    @Autowired
    ReaderTypeDao readerTypeDao ;//= new ReaderTypeDao();
    @Autowired
    ReaderTypeMapper readerTypeMapper;
    public PageBean<ReaderTypeDO> pageSearch(Query query)  {
        PageBean<ReaderTypeDO> pb=new PageBean<>();
        query.setStart((query.getPageCode()-1)*query.getPageSize());
        List<ReaderTypeDO> list = readerTypeMapper.pageSearch(query);
        pb.setList(list);
        Integer totalCount = readerTypeMapper.totalCount();
        pb.setTotalCount(totalCount);
        pb.setCurrentPage(query.getPageCode());
        pb.setPageSize(query.getPageSize());
        return pb;
    }


    public ReaderTypeDO getReaderTypeById(ReaderTypeDO readerTypeDO){
        try {
            return readerTypeDao.getReaderTypeById(readerTypeDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public ReaderTypeDO getReaderTypeByName(ReaderTypeDO readerTypeDO){
        try {
            return readerTypeDao.getReaderTypeByName(readerTypeDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updateReaderType(ReaderTypeDO readerTypeDO) {
        try {
            return readerTypeDao.updateReaderType(readerTypeDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int addReaderType(ReaderTypeDO readerTypeDO) {
        try {
            return readerTypeDao.addReaderType(readerTypeDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public List<ReaderTypeDO> getReaderTypes(){
        try {
            return readerTypeDao.getReaderTypes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}

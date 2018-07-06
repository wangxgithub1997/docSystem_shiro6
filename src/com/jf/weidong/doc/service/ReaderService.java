package com.jf.weidong.doc.service;

import com.jf.weidong.doc.dao.ReaderDao;
import com.jf.weidong.doc.domain.data.ReaderDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class ReaderService {
    @Autowired
    ReaderDao readerDao ;//= new ReaderDao();

    public ReaderDO getReaderByPNO(ReaderDO readerDO)  {
        try {
            return readerDao.getReaderByPNO(readerDO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

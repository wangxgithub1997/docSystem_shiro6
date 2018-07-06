package com.jf.weidong.doc.service;

import com.jf.weidong.doc.dao.BorrowManageDao;
import com.jf.weidong.doc.dao.OverDueInfoDao;
import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.BorrowInfoDO;
import com.jf.weidong.doc.domain.data.OverDueInfoDO;
import com.jf.weidong.doc.domain.query.OverdueListQuery;
import com.jf.weidong.doc.domain.vo.OverdueListVO;
import com.jf.weidong.doc.mapper.OverdueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class OverDueInfoService {
    @Autowired
    OverDueInfoDao overDueInfoDao ;//= new OverDueInfoDao();
    @Autowired
    BorrowManageDao borrowManageDao ;//= new BorrowManageDao();

    @Autowired
    OverdueMapper overdueMapper;
    public PageBean<OverdueListVO> pageSearch(OverdueListQuery query) {
        PageBean<OverdueListVO> pb=new PageBean<>();
        query.setStart((query.getPageCode()-1)*query.getPageSize());
        List<OverdueListVO> list = overdueMapper.pageSearch(query);
        pb.setList(list);
        Integer totalCount = overdueMapper.totalCount(query);
        pb.setTotalCount(totalCount);
        pb.setPageSize(query.getPageSize());
        return pb;
    }


}

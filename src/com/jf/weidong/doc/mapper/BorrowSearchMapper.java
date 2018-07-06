package com.jf.weidong.doc.mapper;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.query.BorrowSearchQuery;
import com.jf.weidong.doc.domain.vo.BorrowSearchListVO;

import java.util.List;

public interface BorrowSearchMapper {
    List<BorrowSearchListVO> pageSearch(BorrowSearchQuery query);
    Integer totalCount(BorrowSearchQuery query);
}

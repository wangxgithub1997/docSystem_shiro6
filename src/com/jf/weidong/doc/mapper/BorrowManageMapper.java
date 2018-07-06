package com.jf.weidong.doc.mapper;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.query.Query;
import com.jf.weidong.doc.domain.vo.BorrowInfoListVO;

import java.util.List;

public interface BorrowManageMapper {
    List<BorrowInfoListVO> pageSearch(Query query);
    Integer totalCount ();
}

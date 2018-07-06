package com.jf.weidong.doc.mapper;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.query.ReaderQuery;
import com.jf.weidong.doc.domain.vo.ReaderListVO;

import java.util.List;

public interface ReaderManageMapper {
    List<ReaderListVO> pageSearch(ReaderQuery query);
    Integer totalCount(ReaderQuery query);
}

package com.jf.weidong.doc.mapper;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.query.OverdueListQuery;
import com.jf.weidong.doc.domain.vo.OverdueListVO;

import java.util.List;

public interface OverdueMapper {
    List<OverdueListVO> pageSearch(OverdueListQuery query);
    Integer totalCount(OverdueListQuery query);
}

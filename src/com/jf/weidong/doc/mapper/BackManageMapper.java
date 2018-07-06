package com.jf.weidong.doc.mapper;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.query.BackListQuery;
import com.jf.weidong.doc.domain.vo.BackListVO;

import java.util.List;

public interface BackManageMapper {
    List<BackListVO> pageSearch(BackListQuery query);
    Integer totalCount(BackListQuery query);
}

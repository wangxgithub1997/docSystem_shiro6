package com.jf.weidong.doc.mapper;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.ReaderTypeDO;
import com.jf.weidong.doc.domain.query.Query;

import java.util.List;

public interface ReaderTypeMapper {
    List<ReaderTypeDO> pageSearch(Query query);
    Integer totalCount();
}

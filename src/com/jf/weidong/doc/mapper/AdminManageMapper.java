package com.jf.weidong.doc.mapper;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.AdminDO;
import com.jf.weidong.doc.domain.data.AuthorizationDO;
import com.jf.weidong.doc.domain.query.AdminListQuery;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;

import java.util.List;

public interface AdminManageMapper {
    List<AdminDetailsVO> pageSearch(AdminListQuery query);
    Integer totalCount(AdminListQuery query);
    Integer addAdmin(AdminDO adminDO);
}

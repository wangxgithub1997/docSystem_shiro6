package com.jf.weidong.doc.mapper;

import com.jf.weidong.doc.domain.data.AdminDO;

public interface AdminMapper {
    AdminDO getAdminByName(String name);
    AdminDO getAdminById(AdminDO adminDO);
}

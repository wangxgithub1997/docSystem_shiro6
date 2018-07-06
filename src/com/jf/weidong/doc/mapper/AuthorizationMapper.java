package com.jf.weidong.doc.mapper;

import com.jf.weidong.doc.domain.data.AuthorizationDO;

public interface AuthorizationMapper {
    AuthorizationDO getAuthorizationById(Integer id);
    Integer addAuthorization(AuthorizationDO authorizationDO);
}

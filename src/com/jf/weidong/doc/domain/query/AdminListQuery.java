package com.jf.weidong.doc.domain.query;

/**
 **/
public class AdminListQuery extends Query {
    public String adminUserName;//管理员用户名
    public String adminName;//管理员名称

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}

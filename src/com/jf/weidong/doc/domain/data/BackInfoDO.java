package com.jf.weidong.doc.domain.data;

import java.util.Date;

/**
 * 归还信息
 */
public class BackInfoDO {
    private Integer id;    //借阅编号
    private Integer fk_admin;//操作员
    private Date backDate;    //归还时间

    public BackInfoDO(Integer id) {
        this.id = id;
    }

    public BackInfoDO() {
    }

    public Date getBackDate() {
        return backDate;
    }

    public void setBackDate(Date backDate) {
        this.backDate = backDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFk_admin() {
        return fk_admin;
    }

    public void setFk_admin(Integer fk_admin) {
        this.fk_admin = fk_admin;
    }
}

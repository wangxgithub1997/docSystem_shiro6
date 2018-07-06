package com.jf.weidong.doc.domain.query;

import com.jf.weidong.doc.utils.DataUtils;

public class Query {

    Integer pageCode;
    int start;
    int pageSize=5;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Integer getPageCode() {
        return pageCode;
    }

    public void setPageCode(Integer pageCode) {
        this.pageCode = pageCode;
    }
}

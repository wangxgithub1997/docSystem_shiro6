package com.jf.weidong.doc.domain.query;

import com.jf.weidong.doc.utils.DataUtils;

public class BookTypeQuery extends Query{
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}

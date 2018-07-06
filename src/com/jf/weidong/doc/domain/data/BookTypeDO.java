package com.jf.weidong.doc.domain.data;

public class BookTypeDO {
    private Integer id;
    private String name;

    public BookTypeDO() {
    }

    public BookTypeDO(String name) {
        this.name = name;
    }

    public BookTypeDO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

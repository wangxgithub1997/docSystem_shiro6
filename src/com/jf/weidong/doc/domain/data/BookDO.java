package com.jf.weidong.doc.domain.data;

import java.util.Date;

public class BookDO {

    private Integer id; // 图书编号
    private String ISBN;// ISBN 国际标准书号

    private String name; // 图书名称
    private String autho; // 作者名称
    private String press; // 出版社
    private Date putdate; // 上架日期
    private Integer num; // 总数量
    private Integer currentNum; // 在馆数量
    private Double price; // 价格
    private String description; // 简介

    private Integer fk_admin;//操作管理员id
    private Integer fk_booktype;//图书类型

    public BookDO() {
    }

    public BookDO(Integer id) {
        this.id = id;
    }

    public BookDO(String ISBN) {
        this.ISBN = ISBN;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAutho() {
        return autho;
    }

    public void setAutho(String autho) {
        this.autho = autho;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public Date getPutdate() {
        return putdate;
    }

    public void setPutdate(Date putdate) {
        this.putdate = putdate;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(Integer currentNum) {
        this.currentNum = currentNum;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFk_admin() {
        return fk_admin;
    }

    public void setFk_admin(Integer fk_admin) {
        this.fk_admin = fk_admin;
    }

    public Integer getFk_booktype() {
        return fk_booktype;
    }

    public void setFk_booktype(Integer fk_booktype) {
        this.fk_booktype = fk_booktype;
    }
}

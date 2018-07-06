package com.jf.weidong.doc.domain.vo;

import java.util.Date;

/**
 * 图书管理列表
 */
public class BookManageListVO {

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

    private String bookType; // 图书类型





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

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }
}

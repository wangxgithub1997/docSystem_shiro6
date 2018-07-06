package com.jf.weidong.doc.domain.query;

/**
 * 图书管理查询
 */
public class BookManageQuery extends Query{

    //搜索的条件
    String ISBN;
    //图书名称
    String bookName;
    //图书分类id
    Integer bookTypeId;
    //出版社
    String press;
    //作者名称
    String autho;

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getBookTypeId() {
        return bookTypeId;
    }

    public void setBookTypeId(Integer bookTypeId) {
        this.bookTypeId = bookTypeId;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getAutho() {
        return autho;
    }

    public void setAutho(String autho) {
        this.autho = autho;
    }
}

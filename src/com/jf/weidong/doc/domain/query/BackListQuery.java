package com.jf.weidong.doc.domain.query;

/**
 * 归还列表查询VO
 * @description:
 * @author: weidong
 * @create: 2018-05-16 15:53
 **/
public class BackListQuery extends Query {
    String ISBN;
    String paperNO;
    Integer borrowId;

    public BackListQuery() {
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getPaperNO() {
        return paperNO;
    }

    public void setPaperNO(String paperNO) {
        this.paperNO = paperNO;
    }

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }
}

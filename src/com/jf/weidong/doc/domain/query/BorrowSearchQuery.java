package com.jf.weidong.doc.domain.query;

public class BorrowSearchQuery extends Query {
    private String ISBN;
    private String paperNO;
    private Integer borrowId;

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

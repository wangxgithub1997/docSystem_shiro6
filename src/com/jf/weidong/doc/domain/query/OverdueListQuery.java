package com.jf.weidong.doc.domain.query;

/**
 * 逾期查询
 */
public class OverdueListQuery extends Query {

    Integer borrowId;//借阅编号
    String paperNO;//证件号
    String ISBN;//图书ISBN号

    private int readerId;

    public OverdueListQuery() {
    }

    public OverdueListQuery(Integer borrowId, String paperNO, String ISBN, int readerId) {
        this.borrowId = borrowId;
        this.paperNO = paperNO;
        this.ISBN = ISBN;
        this.readerId = readerId;
    }

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public String getPaperNO() {
        return paperNO;
    }

    public void setPaperNO(String paperNO) {
        this.paperNO = paperNO;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getReaderId() {
        return readerId;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }
}

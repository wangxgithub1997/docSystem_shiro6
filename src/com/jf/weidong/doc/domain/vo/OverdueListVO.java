package com.jf.weidong.doc.domain.vo;

public class OverdueListVO {

    private Integer borrowId;    //借阅编号

    private String ISBN;
    private String bookName;

    private String paperNO;
    private String readerName;

    //逾期天数
    private String overDay;

    public double forfeit;//罚金

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

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

    public String getPaperNO() {
        return paperNO;
    }

    public void setPaperNO(String paperNO) {
        this.paperNO = paperNO;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getOverDay() {
        return overDay;
    }

    public void setOverDay(String overDay) {
        this.overDay = overDay;
    }

    public double getForfeit() {
        return forfeit;
    }

    public void setForfeit(double forfeit) {
        this.forfeit = forfeit;
    }
}

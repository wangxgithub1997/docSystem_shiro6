package com.jf.weidong.doc.domain.vo;

import java.util.Date;

/**
 * 借阅查询VO
 */
public class BorrowSearchListVO {
    private Integer borrowId;    //借阅编号
    private String ISBN;//借阅书籍ISBN号
    private String bookName;//借阅书籍名称
    //读者
    private String paperNO;
    private String readerName;
    private Date borrowDate;//借阅日期
    private Date backDate;    //归还日期
    private Date endDate;    //截止归还日期
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

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getBackDate() {
        return backDate;
    }

    public void setBackDate(Date backDate) {
        this.backDate = backDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

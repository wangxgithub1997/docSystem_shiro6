package com.jf.weidong.doc.domain.query;

public class ReaderQuery extends Query {

    private String paperNO;
    private String name;
    private Integer readerType;

    public String getPaperNO() {
        return paperNO;
    }

    public void setPaperNO(String paperNO) {
        this.paperNO = paperNO;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReaderType() {
        return readerType;
    }

    public void setReaderType(Integer readerType) {
        this.readerType = readerType;
    }


}

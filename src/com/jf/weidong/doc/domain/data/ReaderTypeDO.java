package com.jf.weidong.doc.domain.data;

public class ReaderTypeDO {

    private Integer id;
    private String name;//读者类型名称
    private Integer maxNum;	//最大借书量
    private Double penalty;	//每日罚金
    private Integer bday;	//可借天数
    private Integer renewDays;	//续借天数
    public ReaderTypeDO() {
    }

    public ReaderTypeDO(Integer id) {
        this.id = id;
    }

    public ReaderTypeDO(String name) {
        this.name = name;
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

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }

    public Integer getBday() {
        return bday;
    }

    public void setBday(Integer bday) {
        this.bday = bday;
    }

    public Integer getRenewDays() {
        return renewDays;
    }

    public void setRenewDays(Integer renewDays) {
        this.renewDays = renewDays;
    }
}

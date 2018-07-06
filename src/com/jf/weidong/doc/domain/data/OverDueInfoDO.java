package com.jf.weidong.doc.domain.data;

/**
 * 罚金信息类
 */
public class OverDueInfoDO {
    private Integer id;    //借阅编号
    private Double forfeit;    //罚金金额
    private int isPay;    //是否已经支付罚金
    private Integer fk_admin;//操作的管理员

    public OverDueInfoDO(Integer id) {
        this.id = id;
    }

    public Integer getFk_admin() {
        return fk_admin;
    }

    public void setFk_admin(Integer fk_admin) {
        this.fk_admin = fk_admin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getForfeit() {
        return forfeit;
    }

    public void setForfeit(Double forfeit) {
        this.forfeit = forfeit;
    }

    public int getIsPay() {
        return isPay;
    }


    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public OverDueInfoDO() {
    }

    @Override
    public String toString() {
        return "OverDueInfoDO{" +
                "id=" + id +
                ", forfeit=" + forfeit +
                ", isPay=" + isPay +
                ", fk_admin=" + fk_admin +
                '}';
    }
}

package com.jf.weidong.doc.domain.vo;

/**
 * 记录导入错误的信息
 * 所有的字段都是String类型
 */
public class ReaderExportFailureVO {
    private String paperNO;    //证件号码
    private String name;    //真实名称
    private String readerType;//读者类型(学生或者教师)
    private String email;    //邮箱
    private String phone;    //联系方式
    private String errorMsg;    //错误提示


    public ReaderExportFailureVO(String paperNO, String name, String readerType, String email,
                                 String phone, String errorMsg) {
        this.paperNO = paperNO;
        this.name = name;
        this.readerType = readerType;
        this.email = email;
        this.phone = phone;
        this.errorMsg = errorMsg;
    }


    public static ReaderExportFailureVO createReaderExportFailureVO(String paperNO, String name, String readerType, String email,
                                                                    String phone, String errorMsg) {
        return new ReaderExportFailureVO(paperNO, name, readerType, email,
                phone, errorMsg);
    }

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

    public String getReaderType() {
        return readerType;
    }

    public void setReaderType(String readerType) {
        this.readerType = readerType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

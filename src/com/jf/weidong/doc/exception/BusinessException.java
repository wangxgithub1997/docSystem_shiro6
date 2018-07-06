package com.jf.weidong.doc.exception;

/**
 * 系统业务异常
 */
public class BusinessException extends RuntimeException {
    private Integer code;//异常码
    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

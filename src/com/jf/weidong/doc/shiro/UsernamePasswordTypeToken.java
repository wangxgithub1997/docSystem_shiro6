package com.jf.weidong.doc.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UsernamePasswordTypeToken extends UsernamePasswordToken {
    public static final int ADMIN=1;
    public static final int READER=2;

    private Integer type;

    public UsernamePasswordTypeToken(String username, String password, Integer type) {
        super(username, password);
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

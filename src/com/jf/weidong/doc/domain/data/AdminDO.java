package com.jf.weidong.doc.domain.data;

public class AdminDO {
    private Integer id; // 编号
    private String username; // 用户名【用户登录】
    private String name; // 管理员姓名【可以理解为昵称】
    private String password; // 密码
    private String phone; // 联系方式
    private Integer state; // 删除状态 【0刪除】

    public AdminDO() {
    }

    public AdminDO(Integer id) {
        this.id = id;
    }

    public AdminDO(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}

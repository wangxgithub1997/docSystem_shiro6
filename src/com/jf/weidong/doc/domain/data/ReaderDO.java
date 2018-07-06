package com.jf.weidong.doc.domain.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 读者类
 */
public class ReaderDO implements Serializable {

	private Integer id;    //自动编号
	private String name;    //真实名称
	private String password;    //密码
	private String phone;    //联系方式
	private String email;    //邮箱
	private String paperNO;    //证件号码
	private Date createTime;    //创建时间
	private Integer fk_readerType;
	private Integer fk_admin;

	//alt + insert


	public ReaderDO() {
	}

	public ReaderDO(Integer id) {
		this.id = id;
	}

	public ReaderDO(String paperNO) {
		this.paperNO = paperNO;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPaperNO() {
		return paperNO;
	}

	public void setPaperNO(String paperNO) {
		this.paperNO = paperNO;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getFk_readerType() {
		return fk_readerType;
	}

	public void setFk_readerType(Integer fk_readerType) {
		this.fk_readerType = fk_readerType;
	}

	public Integer getFk_admin() {
		return fk_admin;
	}

	public void setFk_admin(Integer fk_admin) {
		this.fk_admin = fk_admin;
	}
}

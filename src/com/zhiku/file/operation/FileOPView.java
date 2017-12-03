package com.zhiku.file.operation;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "zhiku.file_op_view")
@IdClass(FileOPKey.class)
public class FileOPView {
	@Id
	private int fid;
	@Id
	private int uid;
	@Id
	private Date optime;
	private String opip;
	private int type;
	private String op_descs;
	private String usr;
	private String nick;
	private String mail;
	private String phone;
	private String qq;
	private String xname;
	private String mname;
	private int auth;
	private int user_status;
	private String name;
	private int module;
	private String cname;
	private int docformat;
	private Date uptime;
	private int dncnt;
	private String file_descs;
	private int origin;
	private int file_status;
	
	
	
	
	
	//getter and setter
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public Date getOptime() {
		return optime;
	}
	public void setOptime(Date optime) {
		this.optime = optime;
	}
	public String getOpip() {
		return opip;
	}
	public void setOpip(String opip) {
		this.opip = opip;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getOp_descs() {
		return op_descs;
	}
	public void setOp_descs(String op_descs) {
		this.op_descs = op_descs;
	}
	public String getUsr() {
		return usr;
	}
	public void setUsr(String usr) {
		this.usr = usr;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getXname() {
		return xname;
	}
	public void setXname(String xname) {
		this.xname = xname;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public int getAuth() {
		return auth;
	}
	public void setAuth(int auth) {
		this.auth = auth;
	}
	public int getUser_status() {
		return user_status;
	}
	public void setUser_status(int user_status) {
		this.user_status = user_status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getModule() {
		return module;
	}
	public void setModule(int module) {
		this.module = module;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public int getDocformat() {
		return docformat;
	}
	public void setDocformat(int docformat) {
		this.docformat = docformat;
	}
	public Date getUptime() {
		return uptime;
	}
	public void setUptime(Date uptime) {
		this.uptime = uptime;
	}
	public int getDncnt() {
		return dncnt;
	}
	public void setDncnt(int dncnt) {
		this.dncnt = dncnt;
	}
	public String getFile_descs() {
		return file_descs;
	}
	public void setFile_descs(String file_descs) {
		this.file_descs = file_descs;
	}
	public int getOrigin() {
		return origin;
	}
	public void setOrigin(int origin) {
		this.origin = origin;
	}
	public int getFile_status() {
		return file_status;
	}
	public void setFile_status(int file_status) {
		this.file_status = file_status;
	}
	
	
}

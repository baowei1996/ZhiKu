package com.zhiku.file;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "file_view")
public class FileView {
	@Id
	private int fid;
	private String name;
	
	private int uid;
	private String usr;
	private String nick;
	private String xname;
	private String mname;
	
	private Date uptime;
	private int module;
	private int cid;
	private String cname;
	private int docformat;
	private String fileformat;
	private String teacher;
	private int dncnt;
	private int colcnt;
	private int origin;
	private String desc;
	
	
	
	public static List<FileView> findByCid(int cid){
		return FileDAO.findByCid(cid);
	}
	
	public static List<FileView> findByCname(String cname){
		return FileDAO.findByCname(cname);
	}
	
	public static List<FileView> findByCids(List<Integer> cids){
		return FileDAO.findByCids(cids);
	}
	
	
	
	//getter and setter 方法
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
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
	public Date getUptime() {
		return uptime;
	}
	public void setUptime(Date uptime) {
		this.uptime = uptime;
	}
	public int getModule() {
		return module;
	}
	public void setModule(int module) {
		this.module = module;
	}
	public int getCid(){
		return cid;
	}
	public void setCid(int cid){
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCourse(String cname) {
		this.cname = cname;
	}
	public String getTeacher() {
		return teacher;
	}
	public int getDocformat() {
		return docformat;
	}

	public void setDocformat(int docformat) {
		this.docformat = docformat;
	}

	public String getFileformat() {
		return fileformat;
	}

	public void setFileformat(String fileformat) {
		this.fileformat = fileformat;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public int getDncnt() {
		return dncnt;
	}
	public void setDncnt(int dncnt) {
		this.dncnt = dncnt;
	}
	public int getColcnt() {
		return colcnt;
	}
	public void setColcnt(int colcnt) {
		this.colcnt = colcnt;
	}
	public int getOrigin() {
		return origin;
	}
	public void setOrigin(int origin) {
		this.origin = origin;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}

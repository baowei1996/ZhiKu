package com.zhiku.activity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "zhiku.activity")
public class Activity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int aid;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "img")
	private String img;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date pubtime;
	
	@Column(name = "linkweb")
	private String linkweb;
	
	@Column(name = "descs")
	private String descs;
	
	@Column(name = "scancnt")
	private int scancnt;
	
	@Column(name = "username")
	private String username;
	
	//默认的动态加载数
	public static int ACTIVITY_NUM = 2;
	
	//构造方法
	public Activity(){	}
	
	
	//getter and settter method
	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Date getPubtime() {
		return pubtime;
	}

	public void setPubtime(Date pubtime) {
		this.pubtime = pubtime;
	}

	public String getLinkweb() {
		return linkweb;
	}

	public void setLinkweb(String linkweb) {
		this.linkweb = linkweb;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public int getScancnt() {
		return scancnt;
	}

	public void setScancnt(int scancnt) {
		this.scancnt = scancnt;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	

}

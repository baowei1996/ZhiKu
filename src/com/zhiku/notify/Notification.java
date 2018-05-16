package com.zhiku.notify;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 
 * @author baowei
 * 用来描述用户通知的类
 *
 */
@Entity
@Table(name = "zhiku.notification")
public class Notification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int nid;
	
	@Column(name="title",nullable = false)
	private String title;
	
	@Column(name="fromer",nullable = false)
	private String from;
	
	@Column(name="toer" , nullable = false)
	private int to;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date ntime;
	
	@Column(name="content",nullable = false)
	private String content;
	
	@Column(name="isread" )
	private int read;
	
	
	//通知已读和未读标志
	public static int READ = 1;
	public static int UNREAD = 2;
	
	
	public Notification(){	}
	
	//Setter and Getter mothed
	public int getNid() {
		return nid;
	}
	public void setNid(int nid) {
		this.nid = nid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public Date getNtime() {
		return ntime;
	}
	public void setNtime(Date ntime) {
		this.ntime = ntime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
	
	
}

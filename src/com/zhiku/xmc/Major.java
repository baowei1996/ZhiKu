package com.zhiku.xmc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "zhiku.major")
public class Major {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int mid;
	private String mname;
	private int xid;
	
	public Major(){}
	public Major(String mname){
		this.mname = mname;
	}
	
	
	//getter and setter 方法
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public int getXid(){
		return xid;
	}
	public void setXid(int xid){
		this.setXid(xid);
	}
	
}

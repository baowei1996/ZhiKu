package com.zhiku.xmc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "college")
public class College {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int xid;
	private String xname;
	
	public College(){}
	public College(String xname){
		this.xname = xname;
	}
	
	//getter and setter 方法
	public int getXid() {
		return xid;
	}
	public void setXid(int xid) {
		this.xid = xid;
	}
	public String getXname() {
		return xname;
	}
	public void setXname(String xname) {
		this.xname = xname;
	}
	
	
}

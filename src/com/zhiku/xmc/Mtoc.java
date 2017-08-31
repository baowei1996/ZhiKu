package com.zhiku.xmc;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "mtoc")
@IdClass(MtocKey.class)
public class Mtoc {
	@Id
	private int xid;
	@Id
	private int mid;
	@Id
	private int cid;
	public int getXid() {
		return xid;
	}
	public void setXid(int xid) {
		this.xid = xid;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	
}


@SuppressWarnings("serial")
class MtocKey implements Serializable{
	private int xid;
	private int mid;
	private int cid;
	
	public MtocKey(){}

	public MtocKey(int xid, int mid, int cid) {
		super();
		this.xid = xid;
		this.mid = mid;
		this.cid = cid;
	}

	public int getXid() {
		return xid;
	}

	public void setXid(int xid) {
		this.xid = xid;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cid;
		result = prime * result + mid;
		result = prime * result + xid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MtocKey other = (MtocKey) obj;
		if (cid != other.cid)
			return false;
		if (mid != other.mid)
			return false;
		if (xid != other.xid)
			return false;
		return true;
	}
	
	
}

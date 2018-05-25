package com.zhiku.file.operation;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "zhiku.file_op")
@IdClass(FileOPKey.class)
public class FileOP {
	@Id
	private int fid;
	@Id
	private int uid;
	@Id
	@Temporal(TemporalType.TIMESTAMP)
	private Date optime;
	private String opip;
	private int type;
	private String descs;
	
	public static final int UPLOAD = 0;
	public static final int DOWNLOAD = 1;
	public static final int DELETE = 2;
	
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
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
	
	
	
}

@SuppressWarnings("serial")
class FileOPKey implements Serializable{
	private int fid;
	private int uid;
	private Date optime;
	
	public FileOPKey(){}

	public FileOPKey(int fid, int uid, Date optime) {
		super();
		this.fid = fid;
		this.uid = uid;
		this.optime = optime;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fid;
		result = prime * result + ((optime == null) ? 0 : optime.hashCode());
		result = prime * result + uid;
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
		FileOPKey other = (FileOPKey) obj;
		if (fid != other.fid)
			return false;
		if (optime == null) {
			if (other.optime != null)
				return false;
		} else if (!optime.equals(other.optime))
			return false;
		if (uid != other.uid)
			return false;
		return true;
	}
	
	
}
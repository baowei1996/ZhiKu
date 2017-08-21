package com.zhiku.user;

import java.util.Date;

import javax.swing.ImageIcon;

public class User {
	private int uid;		//用户编号
	private String usr;		//用户名
	private String nick;	//昵称
	private String pwd;		//用户密码
	private ImageIcon avator = null;	//头像
	private String sign;	//个性签名
	private int coin;		//积分
	private String mail;	//邮箱
	private String phone;	//电话
	private String qq;		//QQ
	private int xid;		//学院编号
	private int mid;		//专业编号
	private int auth;		//权限——0普通，1管理员
	private int status;		//状态——0未激活，1正常，2封禁
	
	private String regip;	//注册ip
	private Date regtime;	//注册时间
	private String lastip;	//最后登录ip
	private Date lasttime;	//最后登录时间
	private Date mailtime;	//激活到期日
	private Date unlktime;	//封禁到期日
	
	private int upcnt;
	private int dncnt;
	private int colcnt;
	
	private static UserDAO dao = new UserDAO();
	
	public User(){}

	/**
	 * 保存用户注册信息
	 * @return是否保存成功
	 */
	public boolean save(){
		return dao.save(this);
	}
	
	/**
	 * 检查登录信息
	 * @param usr 用户名
	 * @param pwd 登录密码
	 * @return是否登录成功
	 */
	public static boolean check(String usr , String pwd){
		return dao.check(usr , pwd);
	}
	
	public static boolean isExist(String col ,String value){
		return dao.isExist(col, value);
	}
	
	/**
	 * 修改用户信息
	 * @return是否修改成成功
	 */
	public boolean modify(){
		return dao.modify();
	}
	
	
	//setter和getter方法
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

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public ImageIcon getAvator() {
		return avator;
	}

	public void setAvator(ImageIcon avator) {
		this.avator = avator;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
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

	public int getAuth() {
		return auth;
	}

	public void setAuth(int auth) {
		this.auth = auth;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRegip() {
		return regip;
	}

	public void setRegip(String regip) {
		this.regip = regip;
	}

	public Date getRegtime() {
		return regtime;
	}

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	public String getLastip() {
		return lastip;
	}

	public void setLastip(String lastip) {
		this.lastip = lastip;
	}

	public Date getLasttime() {
		return lasttime;
	}

	public void setLasttime(Date lasttime) {
		this.lasttime = lasttime;
	}

	public Date getMailtime() {
		return mailtime;
	}

	public void setMailtime(Date mailtime) {
		this.mailtime = mailtime;
	}

	public Date getUnlktime() {
		return unlktime;
	}

	public void setUnlktime(Date unlktime) {
		this.unlktime = unlktime;
	}

	public int getUpcnt() {
		return upcnt;
	}

	public void setUpcnt(int upcnt) {
		this.upcnt = upcnt;
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
	
	
	
}

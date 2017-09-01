package com.zhiku.file;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "file_info")
public class JFile {	//为了不和java.io.File混淆，使用JFile

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int fid;
	
	@Column(nullable = false)
	private String path;
	
	@Column(nullable = false)
	private String name;
	
	private int module;
	private int course;
	private String teacher;
	private int docformat;
	private String fileformat;
	private String sha;
	
	@Column(nullable = false)
	private int upuid;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date uptime = null;
	
	private int dncnt;
	private int colcnt;
	private String desc;
	private int origin;
	private int status;
	
	
	//自定义常量
	//定义原创性类型
	public static final int NOT_ORIGIN = 0;
	public static final int JP_ORIGIN = 1;
	public static final int USER_ORIGIN = 2;
	
	//定义用户状态常量
	public static final int LOCKED = 0;
	public static final int NORMAL = 1;
	public static final int CHECKING = 2;
	
	//定义文件后缀的常量
	public static final int TYPE_DOC = 0;
	public static final int TYPE_XSL = 1;
	public static final int TYPE_PPT = 2;
	
	private FileDAO dao = null;
	
	public JFile(){
		this.dao = new FileDAO();
	}
	
	/**
	 * 将自身保存到数据库中
	 * @return 是否保存成功
	 */
	public boolean save(){
		return this.dao.save(this);
	}
	
	/**
	 * 依据文件编号找到对应文件
	 * @param fid 文件编号
	 * @return 对应的文件对象，没有则返回null
	 */
	public static JFile findByFid(int fid){
		return FileDAO.findByFid(fid);
	}
	
	/**
	 * 删除指定文件编号的文件
	 * @param fid 文件编号
	 * @return 是否删除成功
	 */
	public static boolean delete(int fid){
		return FileDAO.delete(fid);
	}
	
	/**
	 *修改文件信息并提交 
	 * @return 是否修改成功
	 */
	public boolean modify(){
		return dao.modify(this);
	}
	
	/**
	 * 依据关键字key模糊查询
	 * @param key
	 * @return
	 */
	public static List<JFile> search(String key){
		String sql = "from JFile where name like %" + key + "%";
		return FileDAO.search(sql);
	}
	
	/**
	 * 依据关键字key进行高级查询，即精确查询
	 * @param key
	 * @return
	 */
	public static List<JFile> advancedSearch(String key){
		String sql = "from JFile where name like " + key + "%";
		return FileDAO.search(sql);
	}
	
	
	 
	//setter getter 方法
	
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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
	public int getCourse() {
		return course;
	}
	public void setCourse(int course) {
		this.course = course;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
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
	public String getSha() {
		return sha;
	}
	public void setSha(String sha) {
		this.sha = sha;
	}
	public int getUpuid() {
		return upuid;
	}
	public void setUpuid(int upuid) {
		this.upuid = upuid;
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
	public int getColcnt() {
		return colcnt;
	}
	public void setColcnt(int colcnt) {
		this.colcnt = colcnt;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getOrigin() {
		return origin;
	}
	public void setOrigin(int origin) {
		this.origin = origin;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}

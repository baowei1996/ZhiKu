package com.zhiku.xmc;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.zhiku.hibernate.HibernateSessionFactory;

public class XMCService {
	
	@SuppressWarnings("unchecked")
	/**
	 * 根据学院和专业查找对应的课程
	 * @param xid 学院编号
	 * @param mid 专业编号
	 * @return 返回对应的课程的课号集合
	 */
	public static List<Integer> findCoursesInXM(int xid , int mid){
		Session session = null;
		List<Integer> courses = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "select cid from Mtoc where xid = " + xid + "and mid = " + mid;
			courses = session.createQuery(sql).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return courses;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 获得所有学院的信息
	 * @return 返回所有的学院
	 */
	public static List<College> getAllCollege(){
		Session session = null;
		List<College> colleges = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "from College";
			colleges = session.createQuery(sql).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return colleges;
	}
	
	
	@SuppressWarnings("unchecked")
	/**
	 * 获得所有专业的信息
	 * @return 返回所有的专业
	 */
	public static List<Major> getAllMajor(){
		Session session = null;
		List<Major> majors = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "from Major";
			majors = session.createQuery(sql).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return majors;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 提供搜索提示时使用的方法
	 * 根据输入的关键字查找对应的课程
	 * @param key 课程关键字
	 * @return 返回关键字相关的所有课程
	 */
	public static List<Course> searchByKey(String key){
		Session session = null;
		List<Course> courses = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "from Course where cname like \'%" + key + "%\'";
			courses = session.createQuery(sql).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return courses;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 依据学院编号找到对应学院下的所有专业
	 * @param xid 学院编号
	 * @return 专业集合
	 */
	public static List<Major> findMajorByXid(int xid){
		Session session = null;
		List<Major> majors = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			//因为一个学院一个专业下有多个课程，所以其中应该包含重复的专业数据，使用distinct去重
			String sql = "select distinct mid from Mtoc where xid = " + xid;
			List<Integer> mids = session.createQuery(sql).list();
			String searchMajor = "from Major where mid in " + mids.toString().replace('[', '(').replace(']', ')');
			majors = session.createQuery(searchMajor).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return majors;
	}
	
	/**
	 * 判断对应表中的字段是否包含某个值
	 * @param table 表名
	 * @param col 字段名
	 * @param value 值
	 * @return 如果存在返回true
	 */
	public static boolean isExist(String table , String col , int value){
		boolean exist = true;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "select count(*) from " + table + " where " + col + " = " + value;
			Query q = session.createQuery(sql);
			long result = (Long)q.uniqueResult();
			if(result != 0){
				exist = true;
			}else{
				exist = false;
			}
		}catch(Exception e){
			//如果执行SQL语句出错，则认为数据库中不已经存在这个数据
			exist = false;
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return exist;
	}
}

package com.zhiku.xmc;

import java.util.List;

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
			String sql = "select cid from mtoc where xid = " + xid + "and mid = " + mid;
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
			String sql = "from college";
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
			String sql = "from major";
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
			String sql = "from course where cname like %" + key + "%";
			courses = session.createQuery(sql).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return courses;
	}
}

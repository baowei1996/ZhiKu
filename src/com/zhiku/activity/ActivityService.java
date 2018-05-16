package com.zhiku.activity;


import java.util.List;

import org.hibernate.Session;

import com.zhiku.hibernate.HibernateSessionFactory;

public class ActivityService {
	
	@SuppressWarnings("unchecked")
	public static List<Activity> getNews(int num){
		List<Activity> acts = null;
		Session session = null;
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "from Activity order by aid desc limit " + num;
			acts = session.createQuery(sql).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return acts;
	}
	
	public static boolean addNews(Activity act){
		Session session = null;
		boolean isDone = true;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			session.persist(act);
			session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			isDone = false;
		}finally{
			
		}
		
		return isDone;
	}
}

package com.zhiku.notify;

import java.util.List;

import org.hibernate.Session;

import com.zhiku.hibernate.HibernateSessionFactory;

public class NotificationService {
	
	/**
	 * 根据编号查找对应通知
	 * @param nid 通知编号
	 * @return 编号对应的通知
	 */
	public static Notification getNotificationByNid(int nid){
		Session session = null;
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "from Notification where nid = " + nid ;
			Notification n = (Notification)session.createQuery(sql).uniqueResult();
			return n;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
	}
	
	/**
	 * 修改通知的状态
	 * @param nf 通知
	 * @return 是否修改成功
	 */
	public static boolean modify(Notification nf){
		boolean isDone = true;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			session.update(nf);
			session.getTransaction().commit();
			isDone = true;
		}catch(Exception e){
			session.getTransaction().rollback();
			isDone = false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		return isDone;
	}

	
	@SuppressWarnings("unchecked")
	/**
	 * 查找用户的通知
	 * @param uid 用户id
	 * @return 用户的所有通知
	 */
	public static List<Notification> getNotifications(String usr){
		List<Notification> notifications = null;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "from Notification where toer = \'" + usr + "\' order by ntime ";
			notifications = session.createQuery(sql).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return notifications;
	}
	
	/**
	 * 新增一个通知
	 * @param nf 通知
	 * @return 新增是否成功
	 */
	public static boolean addNotification(Notification nf){
		boolean isDone = true;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			session.persist(nf);
			session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
		}
		
		
		return isDone;
	}
}

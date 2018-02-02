package com.zhiku.DB;

import java.util.Date;

import org.hibernate.Session;

import com.zhiku.file.FileDAO;
import com.zhiku.file.JFile;
import com.zhiku.file.operation.FileOP;
import com.zhiku.file.operation.FileOPService;
import com.zhiku.hibernate.HibernateSessionFactory;
import com.zhiku.user.User;
import com.zhiku.user.UserDAO;

public class Transaction {
	/**
	 * 文件上传信息的事务
	 * @param file 上传文件的相关信息
	 * @param user 上传用户的相关信息
	 * @return 事务处理成功则返回true
	 */
	public static boolean saveUploadInfo(JFile file,User user,String opip){
		boolean isDone = true;
		Session session = null;

		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			FileDAO.save(file,session);
			//设置文件的上传者上传量加一！
			user.setUpcnt(user.getUpcnt() + 1);
			UserDAO.modify(user,session);
			
			//记录上传信息
			FileOP fp = new FileOP();
			file = JFile.findBySha(file.getSha());
			fp.setFid(file.getFid());
			fp.setUid(user.getUid());
			fp.setOptime(new Date());
			fp.setOpip(opip);
			fp.setType(FileOP.UPLOAD);
			FileOPService.save(fp,session);
			session.getTransaction().commit();

		} catch (Exception e) {
			isDone = false;
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			HibernateSessionFactory.closeSession();
		}
		return isDone;
	}
	
	
	/**
	 * 文件下载的事务
	 * @return	如果事务处理成功返回true
	 */
	public static boolean saveDownloadInfo(JFile f, User u, String opip){
		boolean isDone = true;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			f.setDncnt(f.getDncnt() + 1);
			FileDAO.modify(f,session);
			//记录下载操作
			FileOP fp = new FileOP();
			fp.setFid(f.getFid());
			fp.setUid(u.getUid());
			fp.setType(FileOP.DOWNLOAD);
			fp.setOptime(new Date());
			fp.setOpip(opip);
			FileOPService.save(fp, session);
			
			session.getTransaction().commit();
		}catch(Exception e){
			isDone = false;
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		
		return isDone;
	}
	
	
	public static boolean modifyUserInfo(User u){
		boolean isDone = true;
		Session session = null;

		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			UserDAO.modify(u, session);
			
			session.getTransaction().commit();

		} catch (Exception e) {
			isDone = false;
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			HibernateSessionFactory.closeSession();
		}
		
		
		return isDone;
	}
	
	public static boolean modifyFileInfo(JFile f){
		boolean isDone = true;
		Session session = null;

		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			FileDAO.modify(f, session);
			
			session.getTransaction().commit();

		} catch (Exception e) {
			isDone = false;
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			HibernateSessionFactory.closeSession();
		}
		
		
		return isDone;
	}
	
	public static boolean saveFile(JFile f){
		boolean isDone = true;
		Session session = null;

		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			FileDAO.save(f, session);
			
			session.getTransaction().commit();

		} catch (Exception e) {
			isDone = false;
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			HibernateSessionFactory.closeSession();
		}
		
		
		return isDone;
	}

	public static boolean saveFileOP(FileOP fp){
		boolean isDone = true;
		Session session = null;

		try {
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			FileOPService.save(fp, session);
			
			session.getTransaction().commit();

		} catch (Exception e) {
			isDone = false;
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			HibernateSessionFactory.closeSession();
		}
		
		
		return isDone;
	}
}

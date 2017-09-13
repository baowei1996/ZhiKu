package com.zhiku.file.operation;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.zhiku.hibernate.HibernateSessionFactory;

public class FileOPService {
	
	public static final int PAGE_SIZE = 5;
	
	public static boolean save(FileOP fp){
		boolean isDone = true;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			session.persist(fp);
			
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
	
	@SuppressWarnings("unchecked")
	/**
	 * 根据uid和分页信息，返回对应的上传信息操作！
	 * @param uid	用户编号
	 * @param page	第几页
	 * @param type 查询的类型
	 * @return	返回对应页数的fileOP信息
	 */
	public static List<FileOP> getOperation(int uid,int page,int type){
		List<FileOP> fileOpList = null;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "from FileOP where uid = " + uid + " and type = " + type + " order by fid";
			Query q = session.createQuery(sql);
			q.setFetchSize((page -1)*PAGE_SIZE);
			q.setMaxResults(PAGE_SIZE);
			fileOpList = q.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		return fileOpList;
	}
	
}

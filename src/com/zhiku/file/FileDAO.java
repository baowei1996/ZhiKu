package com.zhiku.file;

import java.util.List;

import org.hibernate.Session;

import com.zhiku.hibernate.HibernateSessionFactory;

public class FileDAO {
	
	/**
	 * 保存文件到数据库中
	 * @param f 文件对象
	 * @return 是否保存成功
	 */
	public boolean save(JFile f){
		boolean saved = true;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			session.persist(f);
			session.getTransaction().commit();
			
		}catch(Exception e){
			saved = false;
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return saved;
	}
	
	/**
	 * 依据文件编号找到相应文件
	 * @param fid 文件编号
	 * @return 返回对应文件，如果不存在放回null
	 */
	public static JFile findByFid(int fid){
		JFile f = null;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			f = (JFile)session.get(JFile.class, fid);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return f;
	}
	
	
	/**
	 * 删除指定文件编号的文件
	 * @param fid 文件编号
	 * @return 是否删除成功
	 */
	public static boolean delete(int fid){
		boolean isDone = true;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			JFile f = (JFile)session.get(JFile.class, fid);
			session.delete(f);
			session.getTransaction().commit();
			
		}catch(Exception e){
			isDone = false;
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		return isDone;
	}
	
	
	@SuppressWarnings("unchecked")
	/**
	 * 根据sql查找满足条件的文件集
	 * @param sql 查询语句
	 * @return 返回满足条件的JFile对象集
	 */
	public static List<JFile> search(String sql){
		Session session = null;
		List<JFile> filelist = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			filelist = session.createQuery(sql).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.getTransaction().commit();
			session.close();
		}
		
		return filelist;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 找出对应课程下的所有文件
	 * @param cid 课程
	 * @return 返回对应课程下的所有文件视图对象
	 */
	public static List<FileView> findByCid(int cid){
		Session session = null;
		List<FileView> filelist = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String sql = "from FileView where cid = " + cid;
			filelist = session.createQuery(sql).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.getTransaction().commit();
			session.close();
		}
		
		return filelist;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 依据课程名找到相应的文件视图对象
	 * @param cname 课程名
	 * @return 文件视图对象
	 */
	public static List<FileView> findByCname(String cname){
		Session session = null;
		List<FileView> filelist = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String sql = "from FileView where cname = \'" + cname + "\'";
			filelist = session.createQuery(sql).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.getTransaction().commit();
			session.close();
		}
		
		return filelist;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 重载方法，提供对多个课程对应文件的查找
	 * @param cids 多个课程的课号
	 * @return 课程对应的文件
	 */
	public static List<FileView> findByCids(List<Integer> cids){
		Session session = null;
		List<FileView> filelist = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			String sql = "from FileView where cid in" + cids.toString().replace('[', '(').replace(']', ')');
			filelist = session.createQuery(sql).list();
			
					
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.getTransaction().commit();
			session.close();
		}
		
		return filelist;
	}
	
}

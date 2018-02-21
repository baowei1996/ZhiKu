package com.zhiku.file;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.zhiku.hibernate.HibernateSessionFactory;

public class FileDAO {
	
	public static final int PAGE_SIZE = 5;
	
	/**
	 * 保存文件到数据库中
	 * @param f 文件对象
	 * @return 是否保存成功
	 */
	public static void save(JFile f,Session session){
		session.persist(f);
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
			HibernateSessionFactory.closeSession();
		}
		
		return f;
	}
	
	public static JFile findBySha(String sha){
		JFile f = null;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "from JFile where sha = \'" + sha + "\'";
			f = (JFile)session.createQuery(sql).uniqueResult();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeSession();
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
			HibernateSessionFactory.closeSession();
		}
		return isDone;
	}
	
	/**
	 * 修改文件f的信息并提交
	 * @param f 文件
	 * @return 是否修改成功
	 */
	public static void modify(JFile f,Session session){
		session.update(f);
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
			HibernateSessionFactory.closeSession();
		}
		
		return filelist;
	}
	
	public static FileView findFileViewByFid(int fid ){
		Session session = null;
		FileView file = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String sql = "from FileView where fid = " + fid ;
			file = (FileView) session.createQuery(sql).uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.getTransaction().commit();
			HibernateSessionFactory.closeSession();
		}
		
		return file;
	}
	
	
	@SuppressWarnings("unchecked")
	/**
	 * 找出对应课程下的所有文件
	 * @param cid 课程
	 * @return 返回对应课程下的所有文件视图对象
	 */
	public static List<FileView> findByCid(int cid ,int page){
		Session session = null;
		List<FileView> filelist = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String sql = "from FileView where status = 1 and cid = " + cid + " order by fid desc";
			Query q = session.createQuery(sql);
			q.setFirstResult((page-1)*PAGE_SIZE);	//偏移量
			q.setMaxResults(PAGE_SIZE);		//每页最大值
			filelist = q.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.getTransaction().commit();
			HibernateSessionFactory.closeSession();
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
			HibernateSessionFactory.closeSession();
		}
		
		return filelist;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 重载方法，提供对多个课程对应文件的查找
	 * @param cids 多个课程的课号
	 * @return 课程对应的文件
	 */
	public static List<FileView> findByCids(List<Integer> cids ,int page){
		Session session = null;
		List<FileView> filelist = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			
			String sql = "from FileView where status = 1 and cid in" + cids.toString().replace('[', '(').replace(']', ')') + " order by fid";
			Query q = session.createQuery(sql);
			q.setFirstResult((page-1)*PAGE_SIZE);
			q.setMaxResults(PAGE_SIZE);
			filelist = q.list();
			
					
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.getTransaction().commit();
			HibernateSessionFactory.closeSession();
		}
		
		return filelist;
	}
	
	/**
	 * 判断JFile中col中是否存在value属性的元组
	 * @param col 属性
	 * @param value 值
	 * @return 如果存在返回真，反之返回假
	 */
	public static boolean isExist(String col , String value){
		boolean exist = true;
		Session session = null;

		try {
			session = HibernateSessionFactory.getSession();
			String sql = "select count(*) from  JFile where " + col + " = \'"
					+ value + "\'";
			Query q = session.createQuery(sql);
			long result = (Long) q.uniqueResult();
			if (result != 0) {
				exist = true;
			} else {
				exist = false;
			}
		}catch (Exception e) {
			// 如果执行SQL语句出错，则认为数据库中已经存在这个数据
			exist = true;
			e.printStackTrace();
		} finally {
			HibernateSessionFactory.closeSession();
		}

		return exist;
	}
	
}

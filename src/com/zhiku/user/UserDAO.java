package com.zhiku.user;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.zhiku.hibernate.HibernateSessionFactory;

public class UserDAO {
	/**
	 * 保存用户的注册信息
	 * @param u 用户
	 * @return 是否注册成功
	 */
	public boolean save(User u){
		boolean isDone = true;
		
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			Transaction trans = session.beginTransaction();
			
			session.persist(u);
			trans.commit();
			
		}catch(Exception e){
			isDone = false;
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return isDone;
	}
	
	/**
	 * 通过uid找到用户
	 * @param uid 用户的uid
	 * @return 返回对应的用户对象
	 */
	public static User findByUid(int uid){
		User u = null;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			u = (User) session.get(User.class, uid);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return u;
	}
	
	/**
	 * 通过用户名找用户
	 * @param usr 用户名
	 * @return 返回对应的用户对象，没有则返回null
	 */
	public static User findByUsr(String usr){
		User u = null;
		Session session = null;
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "from user where usr = " + usr;	//省略了select * ,之前加上试过总是不能识别*，所以把省略了
			Query q = session.createQuery(sql);
			u = (User)q.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return u;
	}
	
	/**
	 * 判断USER表中是否存在col属性值为value的元素
	 * @param col 列名称
	 * @param value 列的属性值
	 * @return 如果有返回真
	 */
	public static boolean isExist(String col ,String value){
		boolean exist = true;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			String sql = "select count(*) from  user where " + col + "=" + value;
			Query q = session.createQuery(sql);
			long result = (Long)q.uniqueResult();
			if(result != 0){
				exist = true;
			}else{
				exist = false;
			}
		}catch(Exception e){
			//如果执行SQL语句出错，则认为数据库中已经存在这个数据
			exist = true;
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return exist;
	}
	
	/**
	 * 检查用户名和密码是否正确
	 * @param usr 用户名
	 * @param pwd 密码
	 * @return 是否正确
	 */
	public static boolean check(String usr , String pwd){
		
		boolean isCorrect = true;
		Session session = null;
		User u = null;
		try {
			session = HibernateSessionFactory.getSession();
			u = User.findByUsr(usr);
			
			if(u == null || pwd.equals(u.getPwd())){
				isCorrect = false;
			}
		} catch (Exception e) {
			isCorrect = false;
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return isCorrect;
	}
	
	/**
	 * 修改用户的信息并提交
	 * @param u 修改后的用户对象
	 * @return 是否修改成功
	 */
	public boolean modify(User u){
		boolean modified = true;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			session.update(u);
			session.getTransaction().commit();
		}catch(Exception e){
			modified = false;
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return modified;
	}
	
	/**
	 * 删除指定用户编号的用户信息
	 * @param uid 用户编号
	 * @return 如果删除返回true
	 */
	public static boolean delete(int uid){
		boolean isDone = true;
		Session session = null;
		
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			User u = (User)session.get(User.class, uid);
			session.delete(u);
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
	 * 依据sql返回一个用户列表对象
	 * @param sql 查询SQL
	 * @return 用户列表
	 */
	public static List<User> showList(String sql){
		Session session = null;
		List<User> userList = null;
		try{
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			userList = session.createQuery(sql).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.getTransaction().commit();
			session.close();
		}
		
		return userList;
	}
}

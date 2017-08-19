package com.zhiku.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zhiku.DB.DB;

public class UserDAO {
	public boolean save(User u){
		boolean isDone = true;
		Connection conn = DB.getConnection();
		
//		String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?,?,?)";
//		try {
//			PreparedStatement pstm = conn.prepareStatement(sql);
//		} catch (SQLException e) {
//			isDone = false;
//			e.printStackTrace();
//		}
//		do something
		return isDone;
	}
	
	public boolean check(String usr , String pwd){
		
		boolean isCorrect = true;
		Connection conn = DB.getConnection();
		
		String sql = "select usr , pwd from user where usr = \'" + usr +"\'";
		try {
			PreparedStatement pstm = conn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			
			String username = rs.getString("usr");
			String password = rs.getString("pwd");
			
			if(!username.equals(usr) || !password.equals(pwd)){
				isCorrect = false;
			}
		} catch (SQLException e) {
			isCorrect = false;
			e.printStackTrace();
		}
		
		return isCorrect;
	}
	
	public boolean modify(){
		//do something
		return true;
	}
}

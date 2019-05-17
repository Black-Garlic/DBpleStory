package test;

import java.sql.*;
import java.util.ArrayList;

public class Connect {
	String url = "jdbc:mysql://localhost:3306/DB?serverTimezone=UTC";
	String db_name = "root";
	String pass = "dbsgksmf1";
	
	public Connect() {}
	
	public void getData(ArrayList<User> user, String query) {
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, db_name, pass);
			
			Statement s = conn.createStatement ();
			s.executeQuery (query);
			ResultSet rs = s.getResultSet ();

			for (int i = 0 ; rs.next() ; i++) {
			  User temp = new User();
			
			  temp.user_id = rs.getString ("user_id");
			  temp.user_password = rs.getString ("user_password");
			  temp.birthday = rs.getString ("birthday");
			  temp.user_name = rs.getString ("user_name");
			  user.add(temp);
			  
			  System.out.println ("user_id=" + user.get(i).user_id + ", pass=" + user.get(i).user_password + ", birthday=" + user.get(i).birthday + ", user_name=" + user.get(i).user_name);
			}
			rs.close ();
			s.close ();
		} catch (Exception e) {
			System.out.println("에러\n"+e);
		}
	}

	public void setData(ArrayList<User> user, String query, String id, String pw, String birth, String name) {
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, db_name, pass);
			
			PreparedStatement s = conn.prepareStatement(query);
			
			s.setString(1, id);
			s.setString(2, pw);
			s.setString(3, birth);
			s.setString(4, name);
			
			int r = s.executeUpdate();
			
			s.close ();
		} catch (Exception e) {
			System.out.println("에러\n"+e);
		}
	}
}

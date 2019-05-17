package test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class main {

	static Scanner s = new Scanner(System.in);
	static Connect connect = new Connect();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String query;
		String choice;
		boolean valid = false;
		
		ArrayList<User> user = new ArrayList<User>();
		
		while(!valid) {
			query = "SELECT * FROM userlist";
			
			connect.getData(user, query);
			
			System.out.println("Please select menu");
			System.out.println("1. Login");
			System.out.println("2. Signup");
			
			choice = s.nextLine();
			
			switch(choice) {
			case "1" :
				login(user);
				valid = true;
				break;
			case "2" :
				signup(user);
				break;
			}
		}
	}
	
	public static void login(ArrayList<User> user) {
		String id;
		String pw;
		boolean valid = false;
		
		while (true) {
			System.out.print("Please enter ID : ");
			id = s.nextLine();
			System.out.print("Please enter pw : ");
			pw = s.nextLine();
			
			for (int i = 0; i < user.size(); i++) {
				if (user.get(i).user_id.equals(id) && user.get(i).user_password.equals(pw)) {
					valid = true;
					break;
				}
			}
			
			if (valid) {
				System.out.println("Login Successed\n");
				break;
			} else {
				System.out.println("Login Failed\n");
			}
		}
	}
	
	public static void signup(ArrayList<User> user) {
		String id;
		String pw;
		String birth;
		String name;
		String query;
		
		while (true) {
			System.out.print("Please enter ID : ");
			id = s.nextLine();
			System.out.print("Please enter pw : ");
			pw = s.nextLine();
			System.out.print("Please enter birthday : ");
			birth = s.nextLine();
			System.out.print("Please enter name : ");
			name = s.nextLine();
			
			if (!user.contains(id)) {
				System.out.println("Signup Successed\n");
				query = "insert into UserList (user_id, user_password, birthday, user_name)"
						+ "VALUES (?, ?, ?, ?)";
				connect.setData(user, query, id, pw, birth, name);
				break;
			} else {
				System.out.println("Signup Failed\n");
			}
		}
	}
}

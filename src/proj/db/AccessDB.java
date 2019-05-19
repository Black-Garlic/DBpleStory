package proj.db;

import java.sql.*;
import java.util.ArrayList;

public class AccessDB {
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String url = "jdbc:mysql://localhost:3306/db?serverTimezone=UTC";
	public static final String user = "root";
	public static final String password = "dbsgksmf1";
	
	Connection conn = null;
	
	public AccessDB() {}
	//db 연결
	public Connection connectDB() {
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(url, user, password);
			
			if(!conn.isClosed()) {
				System.out.println(".\n.\n.\nConnection succeed");
			}else {
				System.out.println(".\n.\n.\nConnection failed");
			}
		}catch(Exception e) {
			System.err.println("[Error]\n" + e.getStackTrace());
		}
		return conn;	
	}
	//db 연결 해제
	public void closeDB(Connection conn) {
		try {
			if(conn != null) {
				conn.close();
			}
		}catch(SQLException se) {
			System.err.println("[close error]\n" + se.getStackTrace());
		}
	}
	//로그인
	public boolean singIn(String id, String pwd) throws SQLException
	{
		boolean isSignIn = false;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		psmt = conn.prepareStatement("SELECT * FROM UserList WHERE user_id = ? and user_password = ?");
		
		psmt.setString(1, id);
		psmt.setString(2, pwd);
		rs = psmt.executeQuery();
		
		if(rs.next())
			isSignIn = true;
	
		return isSignIn;
	}
	//회원가입
	public boolean signUp(String id, String pwd, String birthday, String name) throws SQLException
	{
		boolean isSignUp = false;
		PreparedStatement psmt = null;
		
		String INSERT = "INSERT INTO UserList (user_id, user_password, birthday, user_name) VALUES (?, ?, ?, ?)";
		
		try {
			psmt = conn.prepareStatement(INSERT);
			psmt.setString(1, id);
			psmt.setString(2, pwd);
			psmt.setString(3, birthday);
			psmt.setString(4, name);
			
			int rep = psmt.executeUpdate();
			
			if(rep > 0) {
				isSignUp = true;
				System.out.println("...Sign Up Successed...");
			}else {
				System.out.println("...Sign up Failed...");
			}
			
		}catch(SQLException se) {
			System.err.println("[Sign Up Error]\n" + se.getStackTrace());
		}
		
		return isSignUp;
	} 
	//id 중복여부 확인(있으면 true, 없으면 false)
	public boolean checkIDDup(String id) {
		PreparedStatement psmt = null;
		ResultSet rs;
		boolean isIDDup = true;
		
		try {
			psmt = conn.prepareStatement("SELECT * FROM UserList WHERE user_id=?");
			psmt.setString(1, id);
			rs= psmt.executeQuery();
			
			if(rs.next()) 
				isIDDup = false;
		
		} catch (SQLException se) {
			System.err.println("[ID Check Error]\n" + se.getStackTrace());
		}
		return isIDDup;
	}
	//channel 중복여부 확인(있으면 true, 없으면 false)
	public boolean checkChannelDup(int channel_idx) {
		PreparedStatement psmt = null;
		ResultSet rs;
		boolean isChDup = true;
		
		try {
			psmt = conn.prepareStatement("SELECT * FROM ChannelList WHERE channel_index =?");
			psmt.setInt(1, channel_idx);
			rs= psmt.executeQuery();
			
			if(rs.next()) 
				isChDup = false;
		
		} catch (SQLException se) {
			System.err.println("[Channel Check Error]\n" + se.getStackTrace());
		}
		return isChDup;
	}
	//channel을 보여준다.
	public String showChannels() throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		int index;
		int user;
		String str = "";
		
		psmt = conn.prepareStatement("SELECT channel_index, present_user FROM ChannelList");
		rs = psmt.executeQuery();
		
		while(rs.next()) {
			index = rs.getInt("channel_index");
			user = rs.getInt("present_user");
			str += Integer.toString(index) + "\t\t" + Integer.toString(user) + "\n";
		}
		
		return str;
	}
	//channel을 추가한다.
	public boolean addChannel(int channel_index, int maxUser) {
		boolean isAdded = false;
		PreparedStatement psmt = null;
		
		String INSERT = "INSERT INTO ChannelList (channel_index, max_user) VALUES (?, ?)";
		
		try {
			psmt = conn.prepareStatement(INSERT);
			psmt.setInt(1, channel_index);
			psmt.setInt(2, maxUser);
			
			int rep = psmt.executeUpdate();
			
			if(rep > 0) {
				isAdded = true;
				System.out.println("…Adding Channel Successed…");
			}else {
				System.out.println("…Adding Channel Failed…");
			}
			
		}catch(SQLException se) {
			System.err.println("[Adding Channel Error]\n" + se.getStackTrace());
		}
		
		return isAdded;
	}
	
	public String showCharacter() throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		String character;
		String str = "";
		int count = 1;
		
		psmt = conn.prepareStatement("SELECT character_name FROM characterlist");
		rs = psmt.executeQuery();
		
		while(rs.next()) {
			character = rs.getString("character_name");
			str += count+". " + character + "\n";
			count++;
		}
		
		return str;
	}
	
	public void updateCharacter(int channel) throws SQLException {
		PreparedStatement psmt = null;
		
		String UPDATE = "UPDATE characterlist SET current_channel_index=?, current_map=?";
		
		try {
			psmt = conn.prepareStatement(UPDATE);
			psmt.setInt(1, channel);
			psmt.setString(2, "Henesis");
			
			int rep = psmt.executeUpdate();
			
			if(rep > 0) {
				System.out.println("...Update Channel Successed...");
			}else {
				System.out.println("...Update Channel Failed...");
			}
			
		}catch(SQLException se) {
			System.err.println("[Update Channel Error]\n" + se.getStackTrace());
		}
		
	}
	
	public String selectCharacter(int index) throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		String character;
		String str = "";
		int count = 1;
		
		psmt = conn.prepareStatement("SELECT character_name FROM characterlist");
		rs = psmt.executeQuery();
		
		while(rs.next()) {
			character = rs.getString("character_name");
			if (count == index)
				return character;
			count++;
		}
		
		return str;
	}
	
	public String currentMap(String character) throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		String str = "";
		String map;
		
		psmt = conn.prepareStatement("SELECT current_map FROM characterlist WHERE character_name = ?");
		psmt.setString(1, character);
		rs = psmt.executeQuery();
		
		while(rs.next()) {
			map = rs.getString("current_map");
			return map;
		}
		
		return str;
	}
	
	public void insertChat(String content, String character, String map) throws SQLException {
		PreparedStatement psmt = null;
		
		String INSERT = "INSERT INTO chatList (chat_content, character_name, character_location) VALUES (?, ?, ?)";
		
		try {
			psmt = conn.prepareStatement(INSERT);
			psmt.setString(1, content);
			psmt.setString(2, character);
			psmt.setString(3, map);
			
			int rep = psmt.executeUpdate();
			
			if(rep > 0) {
				System.out.println("...Chat Successed...");
			}else {
				System.out.println("...Chat Failed...");
			}
			
		}catch(SQLException se) {
			System.err.println("[Chat Error]\n" + se.getStackTrace());
		}
	}

	public String showChat(String map) throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		String chat;
		String character;
		String str = "";
		int count = 1;
		
		psmt = conn.prepareStatement("SELECT chat_content, character_name FROM chatlist WHERE character_location=?");
		psmt.setString(1, map);
		rs = psmt.executeQuery();
		
		while(rs.next()) {
			chat = rs.getString("chat_content");
			character = rs.getString("character_name");
			str = count + ". " + chat + "\t" + character + "\n";
			count++;
		}
		
		return str;
	}
	//
	public String showChatCharacter(String map) throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		String character;
		String str = "";
		
		psmt = conn.prepareStatement("SELECT DISTINCT character_name FROM chatlist WHERE character_location=?");
		psmt.setString(1, map);
		rs = psmt.executeQuery();
		int count = 1;
		
		while(rs.next()) {
			character = rs.getString("character_name");
			str = count + ". " + character + "\n";
			count++;
		}
		
		return str;
	}
	
	public String[] searchChat(int index, String map) throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		String chat[] = new String[2];
		String str[] = {""};
		int count = 1;
		
		psmt = conn.prepareStatement("SELECT chat_content, character_name FROM chatlist WHERE character_location=?");
		psmt.setString(1, map);
		rs = psmt.executeQuery();
		
		while(rs.next()) {
			if (count == index) {
				chat[0] = rs.getString("chat_content");
				chat[1] = rs.getString("character_name");
				return chat;
			}
			count++;
		}
		
		return str;
	}
	
	public void reportChat(String reportedCharacter, String reporterCharacter, String content, String reason) throws SQLException {
		PreparedStatement psmt = null;
		
		String INSERT = "INSERT INTO reportlist (reportedCharacter, reporterCharacter, reportedContents, reasonForReporting) VALUES (?, ?, ?, ?)";
		
		try {
			psmt = conn.prepareStatement(INSERT);
			psmt.setString(1, reportedCharacter);
			psmt.setString(2, reporterCharacter);
			psmt.setString(3, content);
			psmt.setString(4, reason);
			
			int rep = psmt.executeUpdate();
			
			if(rep > 0) {
				System.out.println("...Report Successed...");
			}else {
				System.out.println("...Report Failed...");
			}
			
		}catch(SQLException se) {
			System.err.println("[Report Error]\n" + se.getStackTrace());
		}
	}
	
	public ArrayList<String> showEquipInventory(String character) throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		String item;
		ArrayList<String> itemlist = new ArrayList<String>();
		ArrayList<String> inventory = new ArrayList<String>();
		String str = "";
		
		psmt = conn.prepareStatement("SELECT equip_item_index FROM userequipinventorylist WHERE character_name=?");
		psmt.setString(1, character);
		rs = psmt.executeQuery();
		int count = 1;
		
		while(rs.next()) {
			item = rs.getString("equip_item_index");
			itemlist.add(item);
			
			//inventory.add(count + ". " + item + "\n");
			//count++;
		}
		
		for(int i = 0 ; i < itemlist.size(); i++) {
			psmt = conn.prepareStatement("SELECT equip_item_name, equip_item_value FROM equiplist WHERE equip_item_index=?");
			psmt.setString(1, itemlist.get(i));
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				inventory.add(itemlist.get(i));
				inventory.add(rs.getString("equip_item_name"));
				inventory.add(rs.getString("equip_item_value"));
			}
		}
		
		return inventory;
	}
	
	public void sellItem(int index, String character, int meso) throws SQLException {
		PreparedStatement psmt = null;
		
		String DELETE = "DELETE from userequipinventorylist WHERE equip_item_index=?";
		
		try {
			psmt = conn.prepareStatement(DELETE);
			psmt.setInt(1, index);
			
			int rep = psmt.executeUpdate();
			
			if(rep > 0) {
				System.out.println("...Sell Successed...");
			}else {
				System.out.println("...Sell Failed...");
			}
			
		}catch(SQLException se) {
			System.err.println("[Sell Error]\n" + se.getStackTrace());
		}
		
		String UPDATE = "UPDATE usermoneyinventorylist SET meso = meso + ? WHERE character_name = ?";
		
		try {
			psmt = conn.prepareStatement(UPDATE);
			psmt.setInt(1, meso);
			psmt.setString(2, character);
			
			int rep = psmt.executeUpdate();
			
			if(rep > 0) {
				System.out.println("...Update meso Successed...");
			}else {
				System.out.println("...Update meso Failed...");
			}
			
		}catch(SQLException se) {
			System.err.println("[Update Channel Error]\n" + se.getStackTrace());
		}
	}
	
	public ArrayList<String> showEquipList() throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		ArrayList<String> itemlist = new ArrayList<String>();
		String str = "";
		
		psmt = conn.prepareStatement("SELECT equip_item_name, equip_item_value FROM equiplist");
		rs = psmt.executeQuery();
		
		while(rs.next()) {
			itemlist.add(rs.getString("equip_item_name"));
			itemlist.add(rs.getString("equip_item_value"));
		}
		
		return itemlist;
	}
	
	public void buyItem(int index, String character, int meso) throws SQLException {
		PreparedStatement psmt = null;
		
		System.out.println(index);
		
		boolean valid = false;

		String UPDATE = "UPDATE usermoneyinventorylist SET meso = meso - ? WHERE character_name = ?";
		
		try {
			psmt = conn.prepareStatement(UPDATE);
			psmt.setInt(1, meso);
			psmt.setString(2, character);
			
			int rep = psmt.executeUpdate();
			
			if(rep > 0) {
				System.out.println("...Update meso Successed...");
				valid = true;
			}else {
				System.out.println("...Update meso Failed...");
			}
			
		} catch(SQLException se) {
			System.err.println("[Update Channel Error]\n" + se.getStackTrace());
		}
		
		if (valid) {
			String INSERT = "INSERT INTO userequipinventorylist(character_name, equip_item_index) VALUES (?, ?)";
			
			try {
				psmt = conn.prepareStatement(INSERT);
				psmt.setString(1, character);
				psmt.setInt(2, index);
				
				int rep = psmt.executeUpdate();
				
				if(rep > 0) {
					System.out.println("...Buy Successed...");
				}else {
					System.out.println("...Buy Failed...");
				}
				
			}catch(SQLException se) {
				System.err.println("[Buy Error]\n" + se.getStackTrace());
			}
		}
	}
	
	public ArrayList<String> getRank(int index) throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		ArrayList<String> info = new ArrayList<String>();
		String str[] = {""};
		int count = 0;
		
		if (index == 1) {
			psmt = conn.prepareStatement("SELECT character_name, level FROM characterlist order by level desc");
		} else if (index == 2) {
			psmt = conn.prepareStatement("SELECT character_name, famous FROM characterlist order by famous desc");
		}

		rs = psmt.executeQuery();
		
		while(rs.next()) {
			if (index == 1) {
				info.add(rs.getString("character_name"));
				info.add(rs.getString("level"));
			} else if (index == 2) {
				info.add(rs.getString("character_name"));
				info.add(rs.getString("famous"));
			}
		}
		
		return info;
	}
}
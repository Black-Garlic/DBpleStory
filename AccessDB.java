package proj.db;

import java.sql.*;
import java.util.Scanner;

public class AccessDB {
	   public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	   public static final String url = "jdbc:mysql://localhost:3306/maplestory?serverTimezone=UTC";
	   public static final String user = "root";
	   public static final String password = "1234";
	   
	   Connection conn = null;
	   static Scanner s = new Scanner(System.in);
	   
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
	   
	   public void updateChannel(int channel) throws SQLException {
	      PreparedStatement psmt = null;
	      
	      String INSERT = "UPDATE 'characterlist' SET current_channel_index='" + channel + "'";
	      
	      try {
	         psmt = conn.prepareStatement(INSERT);
	         
	         int rep = psmt.executeUpdate();
	         
	         if(rep > 0) {
	            System.out.println("...Sign Up Successed...");
	         }else {
	            System.out.println("...Sign up Failed...");
	         }
	         
	      }catch(SQLException se) {
	         System.err.println("[Sign Up Error]\n" + se.getStackTrace());
	      }
	      
	   }
	   
	   public String selectCharacter(int index) throws SQLException {
	      PreparedStatement psmt = null;
	      ResultSet rs;
	      String character;
	      String str = "";
	      int count = 1;
	      
	      psmt = conn.prepareStatement("SELECT * FROM characterlist");
	      rs = psmt.executeQuery();
	      
	      while(rs.next()) {
	         character = rs.getString("character_name");
	         if (count == index)
	            return character;
	         count++;
	      }
	      
	      return str;
	   }
	   
	   public void showMonsters(String character) throws SQLException {
		      PreparedStatement psmt = null;
		      PreparedStatement psmt2 = null;
		      ResultSet rs;
		      ResultSet rs2;
		      String str = "";
		      String monster = "";
		      String current = null ;
		      String current2;
		      int count = 1;
		      
		      
		      psmt2 = conn.prepareStatement("SELECT current_map,character_name FROM Characterlist where character_name = ?");
		      psmt2.setString(1, character);
		      rs2 = psmt2.executeQuery();
		      
		      while(rs2.next())
		      {
		    	  current = rs2.getString("current_map");
		      }
		      
		      psmt = conn.prepareStatement("SELECT appear_map,monster_name FROM MonsterList");
		      rs = psmt.executeQuery();
		      while(rs.next()) {
		    	 current2 = rs.getString("appear_map");
		    	 if(current2.equals(current))
		    	 {
		         monster = rs.getString(2);
		         str += count+". " + monster + "\n";
		         count++;
		    	 }
		      }
		      
		      System.out.println("monster");
	          System.out.println(str);
	          	
	     
		   }
	   
	   public String selectedMonsters(String character, int index) throws SQLException
	   {
			 PreparedStatement psmt = null;
			 PreparedStatement psmt2 = null;
			 ResultSet rs;
			 ResultSet rs2;
			 String str = "";
			 String monster = "";
			 String current = null ;
			 String current2;
			 int count = 1;
			      
			      
			 psmt2 = conn.prepareStatement("SELECT current_map,character_name FROM Characterlist where character_name = ?");
			 psmt2.setString(1, character);
			 rs2 = psmt2.executeQuery();
			      
			 while(rs2.next())
			 {
			    current = rs2.getString("current_map");
			 }
			     
			 psmt = conn.prepareStatement("SELECT appear_map,monster_name FROM MonsterList");
			 rs = psmt.executeQuery();
			 while(rs.next()) {
			   current2 = rs.getString("appear_map");
			    	 if(current2.equals(current))
			    	 {
			         monster = rs.getString(2);
			         if(count==index)
			         {
			        	 return monster;
			         }
			         else 
			         {
			        	 count++;
			         }
			    	 }
			      }
			      
		          	
		          return str;
			   
	   }
	   /*
	   public String currentMap(String character) throws SQLException {
		   	 PreparedStatement psmt = null;
	         ResultSet rs;
	         String str = "";
	         String map = "";
	         
	         psmt = conn.prepareStatement("SELECT * FROM CharacterList WHERE character_name = ?");
	         psmt.setString(1, character);
	         rs = psmt.executeQuery();
	         
	         while(rs.next()) {
	            map = rs.getString("current_map");
	         }
	         
	         str = map;
	         return str;
		   }
	 
	   */
	 public void AttactMonster(String character,String monster) throws SQLException
	 {
		  PreparedStatement psmt = null;
		  PreparedStatement psmt2 = null;
	      ResultSet rs;
	      int rs2;
	      String query = "Update Monsterlist Set Hp = Hp - ? where monster_name = ?";
	      int damage = 0;
	      
	      psmt = conn.prepareStatement("Select damage from characterlist where character_name =? ");
	      psmt.setString(1, character);
	      rs = psmt.executeQuery();
	      
	      while(rs.next())
	      {
	    	  damage = rs.getInt("damage");
	      }
	      try
	      {
	      psmt2 = conn.prepareStatement(query);
	      psmt2.setString(2, monster);
	      psmt2.setInt(1, damage);
	      rs2 = psmt2.executeUpdate();
	      }
	      catch(SQLException e)
	      {
	    	  
	      }
	 }
	 
	 public void skillmonster(String character, String monster,int skill_index) throws SQLException
	 {
		 PreparedStatement psmt = null;
		 ResultSet rs;
		 int skill = 0;
		 int skill_damage = 0;
		 String query;
		 query = "Update monsterlist set Hp = Hp - ? where monster_name = ?";
		 psmt = conn.prepareStatement("Select skill"+skill_index+"_index from characterlist where character_name = ? and skill"+skill_index+"_index is not null");
		 psmt.setString(1, character);
		 rs = psmt.executeQuery();
		 
		 while(rs.next())
		 {
			 skill = rs.getInt("skill"+skill_index+"_index");
		 }
		 
		 PreparedStatement psmt2 = null;
		 ResultSet rs2;
		 psmt2 = conn.prepareStatement("Select skill_damage from skillList where skill_index = ?");
		 psmt2.setInt(1, skill);
		 rs2 = psmt2.executeQuery();
		 
		 while(rs2.next())
		 {
			 skill_damage = rs2.getInt("skill_damage");
		 }
		 
		 PreparedStatement psmt3 = null;
		 int rs3;
		 try
		 {
		 psmt3 = conn.prepareStatement(query);
		 psmt3.setInt(1, skill_damage);
		 psmt3.setString(2, monster);
		 rs3 = psmt3.executeUpdate();
		 
		 }
		 catch(SQLException e)
	     {
	    	  
	     }
		 
	 }
	 
	 public void showconsume(String character) throws SQLException
	 {
		 PreparedStatement psmt = null;
		 ResultSet rs;
		 PreparedStatement psmt2 = null;
		 ResultSet rs2;
		 String str = "";
		 int count = 1;
		 int indexs = 0;
		 String consume_item;
		 psmt = conn.prepareStatement("Select consume_item_index from UserConsumeInventoryList where character_name = ?");
		 psmt.setString(1, character);
		 rs = psmt.executeQuery();
		 while(rs.next()) 
		 {
			 indexs = rs.getInt("consume_item_index");
			 psmt2 = conn.prepareStatement("Select consume_item_name,consume_item_index from ConsumeList where consume_item_index = ? ");
			 psmt2.setInt(1, indexs);
			 rs2 = psmt2.executeQuery();
			 while(rs2.next())
			 {
				 consume_item = rs2.getString("consume_item_name");
				 count = rs2.getInt("consume_item_index");
				 str += count+". " + consume_item + "\n";
		         
			 }
	     }
		 
		 System.out.print(str);
	  }
	      
	  public void consumeitem(String character, int index) throws SQLException
	  {
		PreparedStatement psmt = null;
		ResultSet rs;
		int healhp = 0;
		int healmp = 0;
		psmt = conn.prepareStatement("Select heal_hp,heal_mp from consumelist where consume_item_index = ?");
		psmt.setInt(1, index);
		rs = psmt.executeQuery();
		while(rs.next())
		{
			healhp = rs.getInt("heal_hp");
			healmp = rs.getInt("heal_mp");
		}
		System.out.print(healhp);
		PreparedStatement psmt2 = null;
		int rs2;
		psmt2 = conn.prepareStatement("Update characterList set preHp = if(MaxHP > preHP +"+healhp+",preHP+"+healhp+",MaxHP) where character_name = ?");
		psmt2.setString(1, character);
		rs2 = psmt2.executeUpdate();
		
		PreparedStatement psmt3 = null;
		int rs3;
		psmt3 = conn.prepareStatement("Update characterList set preMp = if(MaxMP > preMP +"+healmp+",premP+"+healmp+",MaxmP) where character_name = ?");
		psmt3.setString(1, character);
		rs3 = psmt3.executeUpdate();
		
	  }
	 
	  public void makecharacter(String id,int channel) throws SQLException
	  {
		  String character_name;
		  while(true)
		  {
		  System.out.print("Enter the character name : ");
		  character_name = s.nextLine();
		  PreparedStatement psmt = null;
		  ResultSet rs;
		  psmt = conn.prepareStatement("Select character_name from characterList where character_name = ?");
		  psmt.setString(1, character_name);
		  rs = psmt.executeQuery();
		  
		  if(rs.next())
		  {
			  System.out.print("already exsited name. Please rewrite.");
		  }
		  else
		  	{
			  PreparedStatement psmt2 = null;
			  int rs2;
			  psmt2 = conn.prepareStatement("insert into characterlist (character_name,user_id,current_channel_index,current_map) values(?,?,?,?)");
			  psmt2.setString(1, character_name);
			  psmt2.setString(2, id);
			  psmt2.setInt(3, channel);
			  psmt2.setString(4, "HENESIS");
			  rs2 = psmt2.executeUpdate();
			  if(rs2>0)
			  {
				  System.out.print("Success! \n");
				  break;
			  }
			  else
			  {
				  System.out.print("Wait!");
			  }
			  
		  	}
		  break;
		  
		  }
		  
	  }
	   

}
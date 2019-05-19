package proj.db;
import java.util.ArrayList;
import java.sql.*;

public class AccessDB {
   public static final String DRIVER = "com.mysql.jdbc.Driver";
   public static final String url = "jdbc:mysql://localhost:3306/DBpleStory?serverTimezone=UTC";
   public static final String user = "root";
   public static final String password = "fa7940291!";
   
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
         ArrayList<String> inventory = null;
         String str = "";
         
         psmt = conn.prepareStatement("SELECT equip_item_index FROM userequipinventorylist WHERE character_name=?");
         psmt.setString(1, character);
         rs = psmt.executeQuery();
         int count = 1;
         
         while(rs.next()) {
            item = rs.getString("equip_item_index");
            inventory.add(count + ". " + character + "\n");
            count++;
         }
         
         return inventory;
      }


   public String showEquipInventory2(String character_name) throws SQLException {
         PreparedStatement psmt = null;
         ResultSet rs;
         String equipitem;
         String str = "";
         int count = 1;
         
         psmt = conn.prepareStatement("SELECT equip_item_index FROM UserEquipInventoryList WHERE character_name = ?");
         psmt.setString(1, character_name);
         rs = psmt.executeQuery();
         
         while(rs.next()) {
            equipitem = rs.getString("equip_item_index");
            str += count+". " + equipitem + "\n";
            count++;
         }
         
         return str;
      }
   public String showConsumeInventory(String character_name) throws SQLException {
         PreparedStatement psmt = null;
         ResultSet rs;
         String consumeitem;
         String str = "";
         int count = 1;
         
         psmt = conn.prepareStatement("SELECT consume_item_index FROM UserConsumeInventoryList WHERE character_name = ?");
         psmt.setString(1, character_name);
         rs = psmt.executeQuery();
         
         while(rs.next()) {
            consumeitem = rs.getString("consume_item_index");
            str += count+". " + consumeitem + "\n";
            count++;
         }
         
         return str;
      }
   public String showOtherInventory(String character_name) throws SQLException {
         PreparedStatement psmt = null;
         ResultSet rs;
         String otheritem;
         String str = "";
         int count = 1;
         
         psmt = conn.prepareStatement("SELECT other_item_index FROM UserOtherInventoryList WHERE character_name = ?");
         psmt.setString(1, character_name);
         rs = psmt.executeQuery();
         
         while(rs.next()) {
            otheritem = rs.getString("other_item_index");
            str += count+". " + otheritem + "\n";
            count++;
         }
         
         return str;
      }
   public String showFriendList(String character_name) throws SQLException {
         PreparedStatement psmt = null;
         ResultSet rs;
         String friend;
         String str = "";
         int count = 1;
         
         psmt = conn.prepareStatement("SELECT friend_name FROM FriendList WHERE character_name = ?");
         psmt.setString(1, character_name);
         rs = psmt.executeQuery();
         
         while(rs.next()) {
            friend = rs.getString("friend_name");
            str += count+". " + friend + "\n";
            count++;
         }
         
         return str;
      }
   
   public void addFriend(String character_name, String friend_name) throws SQLException
   {
      boolean isadded = false;
      PreparedStatement psmt = null;
      
      String INSERT = "INSERT INTO FriendList (character_name, friend_name, friend_channel_location, friend_map_location) VALUES (?, ?, null, null)";
      
      try {
         psmt = conn.prepareStatement(INSERT);
         psmt.setString(1, character_name);
         psmt.setString(2, friend_name);
         
         
         int rep = psmt.executeUpdate();
         
         if(rep > 0) {
            isadded = true;
            System.out.println("...Add friend Successed...");
         }else {
            System.out.println("...Add friend Failed...");
         }
         
      }catch(SQLException se) {
         System.err.println("[Add friend Error]\n" + se.getStackTrace());
      }
      
      
   }
   public String showPartyList() throws SQLException {
         PreparedStatement psmt = null;
         ResultSet rs;
         String Party;
         String str = "";
         int count = 1;
         
         psmt = conn.prepareStatement("SELECT party_index FROM PartyList");
         
         rs = psmt.executeQuery();
         
         while(rs.next()) {
            Party = rs.getString("party_index");
            str += count+". " + Party + "\n";
            count++;
         }
         
         return str;
      }
   public int partymemberNum(int party_index) throws SQLException {
         PreparedStatement psmt = null;
         ResultSet rs;
         
         int partynum=0;
         
         
         psmt = conn.prepareStatement("SELECT num_party_member FROM PartyList where party_index = ?");
         psmt.setInt(1, party_index);
         rs = psmt.executeQuery();
         
         while(rs.next()) {
            partynum = rs.getInt("num_party_member");
            }
         
         
         return partynum;
      }
   public int partyindexGet(String character_name) throws SQLException {
         PreparedStatement psmt = null;
         ResultSet rs;
         
         int partyindex=0;
         
         
         psmt = conn.prepareStatement("SELECT party_index FROM PartyList where party_leader = ?");
         psmt.setString(1, character_name);
         rs = psmt.executeQuery();
         
         while(rs.next()) {
            partyindex = rs.getInt("party_index");
            }
         
         
         return partyindex;
      }
   public int partyindexGet2(String character_name) throws SQLException {
         PreparedStatement psmt = null;
         ResultSet rs;
         
         int partyindex=0;
         
         
         psmt = conn.prepareStatement("SELECT party_index FROM CharacterList where character_name = ?");
         psmt.setString(1, character_name);
         rs = psmt.executeQuery();
         
         while(rs.next()) {
            partyindex = rs.getInt("party_index");
            }
         
         
         return partyindex;
      }
   public void addParty(String character_name) throws SQLException
   {
      boolean isadded = false;
      PreparedStatement psmt = null;
//      PreparedStatement psmt2 = null;
//      ResultSet rs;
//      String partyindex;
//      String str;
//      String SELECT = "SELECT party_index from characterlist where character_name = ?";
//      psmt2 = conn.prepareStatement(SELECT);
//      psmt2.setString(1, character_name);
//      rs = psmt2.executeQuery();
//      while(rs.next()) {
//         partyindex = rs.getString("party_index");
//         str = partyindex;
//         }
      
      
      String INSERT = "INSERT INTO PartyList (num_party_member,party_leader,party_member1,party_member2) VALUES (0,?,null,null)";
//      if(str.equals(null)) {
//         System.out.println("you already has party!");
//      }
//      else {
         try {
            psmt = conn.prepareStatement(INSERT);
            psmt.setString(1, character_name);
            
            int rep = psmt.executeUpdate();
            
            if(rep > 0) {
               isadded = true;
               System.out.println("...Make Party continue...");
            }else {
               System.out.println("...Make Party Failed...");
            }
            
         }catch(SQLException se) {
            System.err.println("[Make Party Error]\n" + se.getStackTrace());
         }
         
//      }
      
      
      
      //return isadded;
   }
   
   public void addParty2(String character_name) throws SQLException{
      boolean isadded2 = false;
      PreparedStatement psmt = null;
      PreparedStatement psmt2 = null;
      ResultSet rs;
      String SELECT = "SELECT party_index from partylist where party_leader = ?";
      String UPDATE = "UPDATE characterlist SET party_index = ? where character_name = ?";
      int partyindex = 0;
      psmt2 = conn.prepareStatement(SELECT);
      psmt2.setString(1, character_name);
      
      rs = psmt2.executeQuery();
      while(rs.next()) {
         partyindex = rs.getInt("party_index");
         }  
        
      
      
      try {
            psmt = conn.prepareStatement(UPDATE);
       
            psmt.setInt(1, partyindex);
            psmt.setString(2, character_name);
            int rep = psmt.executeUpdate();
            
            if(rep > 0) {
               System.out.println("You are PartyLeader!");
            }else {
               System.out.println("...Make Party Failed...");
            }
            
         }catch(SQLException se) {
            System.err.println("[Add Party Error]\n" + se.getStackTrace());
         }
      
      //return isadded2;
   }
   public void updateParty1(String party_member, int party_index) throws SQLException {
         PreparedStatement psmt = null;
         PreparedStatement psmt2 = null;
         String UPDATE = "UPDATE partylist SET num_party_member=?, party_member1=? where party_index = ?";
         String UPDATE2 = "UPDATE characterlist SET party_index=? where character_name = ?";
         try {
            psmt = conn.prepareStatement(UPDATE);
            psmt.setInt(1, 1);
            psmt.setString(2, party_member);
            psmt.setInt(3, party_index);
            int rep = psmt.executeUpdate();
            psmt2 = conn.prepareStatement(UPDATE2);
            psmt2.setInt(1, party_index);
            psmt2.setString(2,  party_member);
            int rep2 = psmt2.executeUpdate();
            if(rep > 0 && rep2>0) {
               System.out.println("...add Party Member Success...");
            }else {
               System.out.println("...add Party Member Failed...");
            }
            
         }catch(SQLException se) {
            System.err.println("[add Party Member Error]\n" + se.getStackTrace());
         }
         
      }
   public void updateParty2(String party_member, int party_index) throws SQLException {
         PreparedStatement psmt = null;
         PreparedStatement psmt2 = null;
         String UPDATE = "UPDATE partylist SET num_party_member=?, party_member2=? where party_index = ?";
         String UPDATE2 = "UPDATE characterlist SET party_index=? where character_name = ?";
         try {
            psmt = conn.prepareStatement(UPDATE);
            psmt.setInt(1, 2);
            psmt.setString(2, party_member);
            psmt.setInt(3, party_index);
            int rep = psmt.executeUpdate();
            psmt2 = conn.prepareStatement(UPDATE2);
            psmt2.setInt(1, party_index);
            psmt2.setString(2,  party_member);
            int rep2 = psmt2.executeUpdate();
            if(rep > 0 && rep2>0) {
               System.out.println("...add Party Member Successed...");
            }else {
               System.out.println("...add Party Member Failed...");
            }
            
         }catch(SQLException se) {
            System.err.println("[add Party Member Error]\n" + se.getStackTrace());
         }
         
      }
   public void DeleteParty(int party_index) throws SQLException { //파티장일때 사용
         
         PreparedStatement psmt = null;
         PreparedStatement psmt2 = null;
         PreparedStatement psmt3 = null;
         PreparedStatement psmt4 = null;
         PreparedStatement psmt5 = null;
         ResultSet rs;
         String party_leader="";
         String party_member1="";
         String party_member2="";
         
         String SELECT = "SELECT party_leader, party_member1, party_member2 from partylist where party_index = ?";
         psmt2 = conn.prepareStatement(SELECT);
         psmt2.setInt(1, party_index);
         rs = psmt2.executeQuery();
         while(rs.next()) {
            party_leader = rs.getString("party_leader");
            party_member1 = rs.getString("party_member1");
            party_member2 = rs.getString("party_member2");
            }
         
         
         String UPDATE = "UPDATE characterlist SET party_index=NULL where character_name = ?";
         String DELETE = "DELETE FROM partylist where party_leader = ?";
         try {
            psmt = conn.prepareStatement(UPDATE);
            psmt.setString(1, party_leader);
            int rep = psmt.executeUpdate();
            psmt3 = conn.prepareStatement(UPDATE);
            psmt3.setString(1, party_member1);
            int rep2 = psmt.executeUpdate();
            psmt4 = conn.prepareStatement(UPDATE);
            psmt4.setString(1, party_member2);
            int rep3 = psmt.executeUpdate();
            psmt5 = conn.prepareStatement(DELETE);
            psmt5.setString(1, party_leader);
            int rep4 = psmt5.executeUpdate();
            if(rep > 0 && rep2 > 0 && rep3 > 0 && rep4>0) {
               System.out.println("...Delete Party Successed...");
            }else {
               System.out.println("...Delete Party Failed...");
            }
            
         }catch(SQLException se) {
            System.err.println("[add Party Member Error]\n" + se.getStackTrace());
         }
         
      }
   public void ExitParty(String party_member, int party_index) throws SQLException {
        PreparedStatement psmt = null;
         PreparedStatement psmt2 = null;
         PreparedStatement psmt3 = null;
         PreparedStatement psmt4 = null;
         ResultSet rs;
         String getUser;
         String party_member1="";
         String party_member2="";
         
         
         String UPDATE1 = "UPDATE partylist SET num_party_member=0, party_member1=? where party_index = ?";
         String UPDATE2 = "UPDATE partylist SET num_party_member=1, party_member2=? where party_index = ?";
         String UPDATE3 = "UPDATE partylist SET party_member1 = ?, party_member2 = null where party_index = ?";
         String UPDATE4 = "UPDATE characterlist SET party_index = null where character_name = ?";
         String SELECT = "SELECT party_member1, party_member2 from partylist where party_index = ?";
         psmt2 = conn.prepareStatement(SELECT);
         psmt2.setInt(1, party_index);
         rs = psmt2.executeQuery();
         while(rs.next()) {
            party_member1 = rs.getString("party_member1");
            party_member2 = rs.getString("party_member2");
            }
         if(party_member1 == null) {
            party_member1 = "no";
         }
         if(party_member2 == null) {
            party_member2 = "no";
         }
         
         if(party_member1.equals(party_member)) {
            getUser = party_member1;  
            if(party_member2.equals(null)) {
               try {
                    psmt = conn.prepareStatement(UPDATE1);
                    psmt.setString(1, null);
                    psmt.setInt(2, party_index);
                    int rep = psmt.executeUpdate();
                    psmt4 = conn.prepareStatement(UPDATE4);
                    psmt4.setString(1, party_member);
                    
                    int rep4 = psmt4.executeUpdate();
                    if(rep > 0 && rep4 >0) {
                       System.out.println("...You Exit Party...");
                    }else {
                       System.out.println("...Exit Party Failed...");
                    }
                    
                 }catch(SQLException se) {
                    System.err.println("[Exit Party Error]\n" + se.getStackTrace());
                 }
            }
            else {
               try {
                    psmt = conn.prepareStatement(UPDATE1);
                    psmt.setString(1, null);
                    psmt.setInt(2, party_index);
                    int rep = psmt.executeUpdate();
                    
                    psmt3 = conn.prepareStatement(UPDATE3);
                    psmt3.setString(1, party_member2);
                    psmt3.setInt(2, party_index);
                    int rep2 = psmt.executeUpdate();
                    psmt4 = conn.prepareStatement(UPDATE4);
                    psmt4.setString(1, party_member);
                    int rep4 = psmt4.executeUpdate();
                    if(rep > 0 && rep2>0 && rep4 >0) {
                       System.out.println("...You Exit Party...");
                    }else {
                       System.out.println("...Exit Party Failed...");
                    }
                    
                 }catch(SQLException se) {
                    System.err.println("[Exit Party Error]\n" + se.getStackTrace());
                 }
            }
            
            
            
         }
         else if(party_member2.equals(party_member)) { // 파티 멤버일 때 사용
            getUser = party_member2;
            try {
                   psmt2 = conn.prepareStatement(UPDATE2);
                   psmt2.setString(1, null);
                   psmt2.setInt(2, party_index);
                   int rep3 = psmt2.executeUpdate();
                   psmt4 = conn.prepareStatement(UPDATE4);
                    psmt4.setString(1, party_member);
                    int rep4 = psmt4.executeUpdate();
                   if(rep3 > 0 && rep4>0) {
                      System.out.println("...You Exit Party...");
                   }else {
                      System.out.println("...Exit Party Failed...");
                   }
                   
                }catch(SQLException se) {
                   System.err.println("[Exit Party Error]\n" + se.getStackTrace());
                }
         }
         else {
            System.out.println("You don't have party!");
         }
      }
   public boolean addGuild(String character_name, String guild_name) throws SQLException
   {
      boolean isadded = false;
      PreparedStatement psmt = null;
   
      String INSERT = "INSERT INTO GuildList (guild_name,num_guild_member,guild_leader,guild_member1,guild_member2,guild_member3,guild_member4,guild_member5)"
            + " VALUES (?,0,?,null,null,null,null,null)";
      try {
         psmt = conn.prepareStatement(INSERT);
         psmt.setString(1, guild_name);
         psmt.setString(2, character_name);
         
         int rep = psmt.executeUpdate();
         
         if(rep > 0) {
            isadded = true;
            System.out.println("...Add Guild Successed...");
         }else {
            System.out.println("...Add Guild Failed...");
         }
         
      }catch(SQLException se) {
         System.err.println("[Add Guild Error]\n" + se.getStackTrace());
      }
      
      return isadded;
   }
   
   public boolean addGuild2(String character_name) throws SQLException{
      boolean isadded2 = false;
      PreparedStatement psmt = null;
      PreparedStatement psmt2 = null;
      ResultSet rs;
      String SELECT = "SELECT guild_name from partylist where party_leader = ?";
      String UPDATE = "UPDATE characterlist SET guild_name = ? where character_name = ?";
      String guild_name;
      psmt2 = conn.prepareStatement(SELECT);
      psmt2.setString(1, character_name);
      
      rs = psmt2.executeQuery();
         
        
       guild_name = rs.getString("guild_name");
      
      try {
            psmt = conn.prepareStatement(UPDATE);
       
            psmt.setString(1, guild_name);
            psmt.setString(2, character_name);
            
            int rep = psmt.executeUpdate();
            
            if(rep > 0) {
               System.out.println("You are Guild Leader!");
            }else {
               System.out.println("...Add Guild Failed...");
            }
            
         }catch(SQLException se) {
            System.err.println("[Add Guild Error]\n" + se.getStackTrace());
         }
      
      return isadded2;
   }
   public void updateGuild(String guild_member, String guild_name) throws SQLException { // 길드에 들어가고 싶은 character_name과 길드의 이름을 Argument로 전달
         PreparedStatement psmt = null;
         PreparedStatement psmt2 = null;
         ResultSet rs;
         int num;
         int inputnum;
         
         String SELECT = "SELECT num_guild_member from guildlist where guild_name = ?";
         
         psmt2 = conn.prepareStatement(SELECT);
         psmt2.setString(1, guild_name);
         
         rs = psmt2.executeQuery();
            
           
        num = rs.getInt("num_guild_member");
         
        if(num==0) {
           inputnum = 1;
           String UPDATE = "UPDATE partylist SET num_guild_member=?, party_member?=? where guild_name = ?";
            
            try {
               psmt = conn.prepareStatement(UPDATE);
               psmt.setInt(1, inputnum);
               psmt.setInt(2, inputnum);
               psmt.setString(3, guild_member);
               psmt.setString(4,guild_name);
               int rep = psmt.executeUpdate();
               
               if(rep > 0) {
                  System.out.println("...add Guild Member Successed...");
               }else {
                  System.out.println("...add Guild Member Failed...");
               }
               
            }catch(SQLException se) {
               System.err.println("[add Guilkd Member Error]\n" + se.getStackTrace());
            }
        }
        else if(num==1) {
           inputnum = 2;
           String UPDATE = "UPDATE partylist SET num_guild_member=?, party_member?=? where guild_name = ?";
            
            try {
               psmt = conn.prepareStatement(UPDATE);
               psmt.setInt(1, inputnum);
               psmt.setInt(2, inputnum);
               psmt.setString(3, guild_member);
               psmt.setString(4,guild_name);
               int rep = psmt.executeUpdate();
               
               if(rep > 0) {
                  System.out.println("...add Guild Member Successed...");
               }else {
                  System.out.println("...add Guild Member Failed...");
               }
               
            }catch(SQLException se) {
               System.err.println("[add Guilkd Member Error]\n" + se.getStackTrace());
            }
        }
        else if(num==2) {
           inputnum = 3;
           String UPDATE = "UPDATE partylist SET num_guild_member=?, party_member?=? where guild_name = ?";
            
            try {
               psmt = conn.prepareStatement(UPDATE);
               psmt.setInt(1, inputnum);
               psmt.setInt(2, inputnum);
               psmt.setString(3, guild_member);
               psmt.setString(4,guild_name);
               int rep = psmt.executeUpdate();
               
               if(rep > 0) {
                  System.out.println("...add Guild Member Successed...");
               }else {
                  System.out.println("...add Guild Member Failed...");
               }
               
            }catch(SQLException se) {
               System.err.println("[add Guilkd Member Error]\n" + se.getStackTrace());
            }
        }
        else if(num==3) {
           inputnum = 4;
           String UPDATE = "UPDATE partylist SET num_guild_member=?, party_member?=? where guild_name = ?";
            
            try {
               psmt = conn.prepareStatement(UPDATE);
               psmt.setInt(1, inputnum);
               psmt.setInt(2, inputnum);
               psmt.setString(3, guild_member);
               psmt.setString(4,guild_name);
               int rep = psmt.executeUpdate();
               
               if(rep > 0) {
                  System.out.println("...add Guild Member Successed...");
               }else {
                  System.out.println("...add Guild Member Failed...");
               }
               
            }catch(SQLException se) {
               System.err.println("[add Guilkd Member Error]\n" + se.getStackTrace());
            }
        }
        else {
           System.out.println("There are too many members in guild!");
        }
        
         
         
      }
   public void DeleteGuild(String guild_name) throws SQLException { //파티장일때 사용
         
         PreparedStatement psmt = null;
         PreparedStatement psmt2 = null;
         PreparedStatement psmt3 = null;
         PreparedStatement psmt4 = null;
         PreparedStatement psmt5 = null;
         PreparedStatement psmt6 = null;
         ResultSet rs;
         String guild_leader;
         String guild_member1;
         String guild_member2;
         String guild_member3;
         String guild_member4;
         
         String SELECT = "SELECT guild_leader, guild_member1, guild_member2, guild_member3, guild_member4 from partylist where guild_name = ?";
         psmt2 = conn.prepareStatement(SELECT);
         psmt2.setString(1, guild_name);
         rs = psmt2.executeQuery();
         guild_leader = rs.getString("guild_leader");
         guild_member1 = rs.getString("guild_member1");
         guild_member2 = rs.getString("guild_member2");
         guild_member3 = rs.getString("guild_member3");
         guild_member4 = rs.getString("guild_member4");
         String UPDATE = "UPDATE characterlist SET guild_index=null where character_name = ?";
         
         try {
            psmt = conn.prepareStatement(UPDATE);
            psmt.setString(1, guild_leader);
            int rep = psmt.executeUpdate();
            psmt3 = conn.prepareStatement(UPDATE);
            psmt3.setString(1, guild_member1);
            int rep2 = psmt3.executeUpdate();
            psmt4 = conn.prepareStatement(UPDATE);
            psmt4.setString(1, guild_member2);
            int rep3 = psmt4.executeUpdate();
            psmt5 = conn.prepareStatement(UPDATE);
            psmt5.setString(1, guild_member3);
            int rep4 = psmt5.executeUpdate();
            psmt6 = conn.prepareStatement(UPDATE);
            psmt6.setString(1, guild_member4);
            int rep5 = psmt6.executeUpdate();
            
            if(rep > 0 && rep2 > 0 && rep3 > 0 && rep4>0 && rep5>0) {
               System.out.println("...Delete Party Successed...");
            }else {
               System.out.println("...Delete Party Failed...");
            }
            
         }catch(SQLException se) {
            System.err.println("[add Party Member Error]\n" + se.getStackTrace());
         }
         
      }
//   public void ExitGuild(String guild_member, String guild_name) throws SQLException {
//      PreparedStatement psmt = null;
//         PreparedStatement psmt2 = null;
//         
//         ResultSet rs;
//         String getUser;
//         String party_member1;
//         String party_member2;
//         
//         
//         String UPDATE1 = "UPDATE partylist SET num_party_member=0, party_member1=? where party_index = ?";
//         String UPDATE2 = "UPDATE partylist SET num_party_member=1, party_member2=? where party_index = ?";
//         
//         String SELECT = "SELECT party_member1, party_member2 from partylist where partyindex = ?";
//         psmt2 = conn.prepareStatement(SELECT);
//         psmt2.setInt(1, party_index);
//         rs = psmt2.executeQuery();
//         
//         party_member1 = rs.getString("party_member1");
//         party_member2 = rs.getString("party_member2");
//         
//         if(party_member1.equals(party_member)) {
//            getUser = party_member1;  
//            
//            try {
//                psmt = conn.prepareStatement(UPDATE1);
//                psmt.setString(1, getUser);
//                psmt.setInt(2, party_index);
//                int rep = psmt.executeUpdate();
//                
//                if(rep > 0) {
//                   System.out.println("...You Exit Party...");
//                }else {
//                   System.out.println("...Exit Party Failed...");
//                }
//                
//             }catch(SQLException se) {
//                System.err.println("[Exit Party Error]\n" + se.getStackTrace());
//             }
//            
//            
//         }
//         else if(party_member2.equals(party_member)) { // 파티 멤버일 때 사용
//            getUser = party_member2;
//            try {
//                   psmt = conn.prepareStatement(UPDATE2);
//                   psmt.setString(1, getUser);
//                   psmt.setInt(2, party_index);
//                   int rep = psmt.executeUpdate();
//                   
//                   if(rep > 0) {
//                      System.out.println("...You Exit Party...");
//                   }else {
//                      System.out.println("...Exit Party Failed...");
//                   }
//                   
//                }catch(SQLException se) {
//                   System.err.println("[Exit Party Error]\n" + se.getStackTrace());
//                }
//         }
//         else {
//            System.out.println("You don't have party!");
//         }
//      }

}



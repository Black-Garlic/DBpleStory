package proj.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AccessDB {
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String url = "jdbc:mysql://localhost:3306/db?serverTimezone=UTC";
	public static final String user = "root";
	public static final String password = "dbsgksmf1";
	static Scanner s = new Scanner(System.in);

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
		boolean isChDup = false;

		try {
			psmt = conn.prepareStatement("SELECT * FROM ChannelList WHERE channel_index =?");
			psmt.setInt(1, channel_idx);
			rs= psmt.executeQuery();

			if(rs.next())
				isChDup = true;

		} catch (SQLException se) {
			System.err.println("[Channel Check Error]\n" + se.getStackTrace());
		}
		return isChDup;
	}
	//channel을 보여준다.
	public String showChanneles() throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		int index;
		int max_user;
		int present_user;
		String str = "";

		psmt = conn.prepareStatement("SELECT * FROM ChannelList");
		rs = psmt.executeQuery();

		while(rs.next()) {
			index = rs.getInt(1);
			str += "\n" + Integer.toString(index) +" ";
			present_user  = rs.getInt(3);
			str += "(" + Integer.toString(present_user) + " / ";
			max_user = rs.getInt(2);
			str += Integer.toString(max_user) + ")\n";
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
				System.out.println("...Adding Channel Successed...");
			}else {
				System.out.println("...Adding Channel Failed...");
			}

		}catch(SQLException se) {
			System.err.println("[Adding Channel Error]\n" + se.getStackTrace());
		}
		return isAdded;
	}
	//channel을 삭제한다.
	public boolean deleteChannel(int channel_index) {
		boolean isDeleted = false;
		PreparedStatement psmt = null;

		String DELETE = "DELETE FROM ChannelList WHERE channel_index=?";

		try {
			psmt = conn.prepareStatement(DELETE);
			psmt.setInt(1, channel_index);

			int rep = psmt.executeUpdate();

			if(rep > 0) {
				isDeleted = true;
				System.out.println("...Deleting Channel Successed...");
			}else {
				System.out.println("...Deleting Channel Failed...");
			}
		}catch(SQLException se) {
			System.err.println("[Deleting Channel Error]\n" + se.getStackTrace());
		}
		return isDeleted;
	}
	//channel을 수정한다.
	public boolean updateChannel(int channel_index, int maxUser) {
		boolean isUpdated = false;
		PreparedStatement psmt = null;

		String UPDATE = "UPDATE ChannelList SET max_user = ? WHERE channel_index = ?";

		try {
			psmt = conn.prepareStatement(UPDATE);
			psmt.setInt(1, maxUser);
			psmt.setInt(2, channel_index);

			int rep = psmt.executeUpdate();

			if(rep > 0) {
				isUpdated = true;
				System.out.println("...Updating Channel Successed...");
			}else {
				System.out.println("...Updating Channel Failed...");
			}
		}catch(SQLException se) {
			System.err.println("[Updating Channel Error]\n" + se.getStackTrace());
		}
		return isUpdated;
	}
	//맵을 보여준다.
	public String showMaps() throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		String mapName = "";
		String portals = "";

		String str = "";

		psmt = conn.prepareStatement("SELECT * FROM MapList");
		rs = psmt.executeQuery();

		while(rs.next()) {
			mapName = rs.getString(1);
			str += "\n" + mapName + " ";
			portals += rs.getString(2);
			str += portals + "\n";
		}

		return str;
	}
	//맵의 중복여부를 확인 (있으면 true, 없으면 false)
	public boolean checkMapDup(String mapName) {
		PreparedStatement psmt = null;
		ResultSet rs;
		boolean isMapDup = false;

		try {
			psmt = conn.prepareStatement("SELECT * FROM MapList WHERE map_name =?");
			psmt.setString(1, mapName);
			rs= psmt.executeQuery();

			if(rs.next())
				isMapDup = true;

		} catch (SQLException se) {
			System.err.println("[Map Check Error]\n" + se.getStackTrace());
		}
		return isMapDup;
	}
	//맵을 추가한다.
	public boolean addMap(String mapName, String portals) {
		boolean isAdded = false;
		PreparedStatement psmt = null;

		String INSERT = "INSERT INTO MapList (map_name, portal) VALUES (?, ?)";

		try {
			psmt = conn.prepareStatement(INSERT);
			psmt.setString(1, mapName);
			psmt.setString(2, portals);

			int rep = psmt.executeUpdate();

			if(rep > 0) {
				isAdded = true;
				System.out.println("...Adding Map Successed...");
			}else {
				System.out.println("...Adding Map Failed...");
			}

		}catch(SQLException se) {
			System.err.println("[Adding Map Error]\n" + se.getStackTrace());
		}
		return isAdded;
	}
	//맵 삭제
	public boolean deleteMap(String map_name) {
		boolean isDeleted = false;
		PreparedStatement psmt = null;

		String DELETE = "DELETE FROM MapList WHERE map_name=?";

		try {
			psmt = conn.prepareStatement(DELETE);
			psmt.setString(1, map_name);

			int rep = psmt.executeUpdate();

			if(rep > 0) {
				isDeleted = true;
				System.out.println("...Deleting Map Successed...");
			}else {
				System.out.println("...Deleting Map Failed...");
			}
		}catch(SQLException se) {
			System.err.println("[Deleting Map Error]\n" + se.getStackTrace());
		}
		return isDeleted;
	}
	//맵 수정
	public boolean updateMap(String map_name, String portals) {
		boolean isUpdated = false;
		PreparedStatement psmt = null;

		String UPDATE = "UPDATE MapList SET portal = ?  WHERE map_name = ?";

		try {
			psmt = conn.prepareStatement(UPDATE);
			psmt.setString(1, portals);
			psmt.setString(2, map_name);

			int rep = psmt.executeUpdate();

			if(rep > 0) {
				isUpdated = true;
				System.out.println("...Updating Map Successed...");
			}else {
				System.out.println("...Updating Map Failed...");
			}
		}catch(SQLException se) {
			System.err.println("[Updating Map Error]\n" + se.getStackTrace());
		}
		return isUpdated;
	}
	//아이템 중복확인
	public boolean checkEquipItemDup(int equipItemIndex) {
		PreparedStatement psmt = null;
		ResultSet rs;
		boolean isItemDup = false;

		try {
			psmt = conn.prepareStatement("SELECT * FROM EquipList WHERE equip_item_index =?");
			psmt.setInt(1, equipItemIndex);
			rs= psmt.executeQuery();

			if(rs.next())
				isItemDup = true;

		} catch (SQLException se) {
			System.err.println("[Equip Check Error]\n" + se.getStackTrace());
		}
		return isItemDup;
	}
	//아이템 추가
	public boolean addEquipItem(int equipItmeIndex, String equipName, int equipValue, int damage, int armor) {
		boolean isAdded = false;
		PreparedStatement psmt = null;

		String INSERT = "INSERT INTO EquipList (equip_item_index, equip_item_name, equip_item_value, damage, armor) VALUES (?, ?, ?, ?, ?)";

		try {
			psmt = conn.prepareStatement(INSERT);
			psmt.setInt(1, equipItmeIndex);
			psmt.setString(2, equipName);
			psmt.setInt(3, equipValue);
			psmt.setInt(4, damage);
			psmt.setInt(5, armor);

			int rep = psmt.executeUpdate();

			if(rep > 0) {
				isAdded = true;
				System.out.println("...Adding Equip Item Successed...");
			}else {
				System.out.println("...Adding Equip Item Failed...");
			}

		}catch(SQLException se) {
			System.err.println("[Adding Equip Item Error]\n" + se.getStackTrace());
		}
		return isAdded;
	}
	//아이템 삭제
	public boolean deleteEquipItem(int equipItemIndex) {
		boolean isDeleted = false;
		PreparedStatement psmt = null;

		String DELETE = "DELETE FROM EquipList WHERE equip_item_index=?";

		try {
			psmt = conn.prepareStatement(DELETE);
			psmt.setInt(1, equipItemIndex);

			int rep = psmt.executeUpdate();

			if(rep > 0) {
				isDeleted = true;
				System.out.println("...Deleting Equip Item Successed...");
			}else {
				System.out.println("...Deleting Equip Item Failed...");
			}
		}catch(SQLException se) {
			System.err.println("[Deleting Equip Item Error]\n" + se.getStackTrace());
		}
		return isDeleted;
	}
	//아이템 보여주기
	public String showEquipItems() throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		int equipItemIndex ;
		String equipItemName = "";
		int equipItemValue ;
		int equipItemDamage ;
		int equipItemArmor ;

		String str = "(id, name, value, damage, armor)";

		psmt = conn.prepareStatement("SELECT * FROM EquipList");
		rs = psmt.executeQuery();

		while(rs.next()) {
			equipItemIndex = rs.getInt(1);
			str += "\n" + equipItemIndex + " ";

			equipItemName = rs.getString(2);
			str += equipItemName + " ";

			equipItemValue = rs.getInt(3);
			str += equipItemValue + " ";

			equipItemDamage = rs.getInt(4);
			str += equipItemDamage + " ";

			equipItemArmor = rs.getInt(5);
			str += equipItemArmor + "\n";

		}

		return str;
	}
	//몬스터 보여주기
	public String showMonstersAdmin() throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		int monsterIndex ;
		String monsterName = "";
		int hp ;
		int exp ;
		String appearMap = "";
		int monsterDamage ;
		int dropEquip ;
		int dropConsume ;
		int dropOther ;

		String str = "(id, name, hp, exp, appearMap, damage, dropEquip, dropConsume, dropOther)";

		psmt = conn.prepareStatement("SELECT * FROM MonsterList");
		rs = psmt.executeQuery();

		while(rs.next()) {
			monsterIndex = rs.getInt(1);
			str += "\n" + monsterIndex + " ";

			monsterName = rs.getString(2);
			str += monsterName + " ";

			hp = rs.getInt(3);
			str += hp + " ";

			exp = rs.getInt(4);
			str += exp + " ";

			appearMap = rs.getString(5);
			str += appearMap + " ";

			monsterDamage = rs.getInt(6);
			str += monsterDamage + " ";

			dropEquip = rs.getInt(7);
			str += dropEquip + " ";

			dropConsume = rs.getInt(8);
			str += dropConsume + " ";

			dropOther = rs.getInt(9);
			str += dropOther + "\n";
		}

		return str;
	}
	//몬스터 중복 확인
	public boolean checkMonsterDup(int monsterIndex) {
		PreparedStatement psmt = null;
		ResultSet rs;
		boolean isMonsterDup = false;

		try {
			psmt = conn.prepareStatement("SELECT * FROM MonsterList WHERE monster_index =?");
			psmt.setInt(1, monsterIndex);
			rs= psmt.executeQuery();

			if(rs.next())
				isMonsterDup = true;

		} catch (SQLException se) {
			System.err.println("[Monster Check Error]\n" + se.getStackTrace());
		}
		return isMonsterDup;
	}
	//몬스터 삭제
	public boolean deleteMonster(int monsterIndex) {
		boolean isDeleted = false;
		PreparedStatement psmt = null;

		String DELETE = "DELETE FROM MonsterList WHERE monster_index=?";

		try {
			psmt = conn.prepareStatement(DELETE);
			psmt.setInt(1, monsterIndex);

			int rep = psmt.executeUpdate();

			if(rep > 0) {
				isDeleted = true;
				System.out.println("...Deleting Monster Successed...");
			}else {
				System.out.println("...Deleting Monster Failed...");
			}
		}catch(SQLException se) {
			System.err.println("[Deleting Monster Error]\n" + se.getStackTrace());
		}
		return isDeleted;
	}
	//몬스터 추가
	public boolean addMonster(int monsterIndex, String monsterName, int hp, int exp, String appearMap, int mosterDamage,
			int dropEquip, int dropConsume, int dropOthers) {
		boolean isAdded = false;
		PreparedStatement psmt = null;

		String INSERT = "INSERT INTO MonsterList (monster_index, monster_name, Hp, exp, appear_map, monster_damage, drop_equip, drop_consume, drop_other) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			psmt = conn.prepareStatement(INSERT);
			psmt.setInt(1, monsterIndex);
			psmt.setString(2, monsterName);
			psmt.setInt(3, hp);
			psmt.setInt(4, exp);
			psmt.setString(5, appearMap);
			psmt.setInt(6, mosterDamage);
			psmt.setInt(7, dropEquip);
			psmt.setInt(8, dropConsume);
			psmt.setInt(9, dropOthers);

			int rep = psmt.executeUpdate();

			if(rep > 0) {
				isAdded = true;
				System.out.println("...Adding Monster Successed...");
			}else {
				System.out.println("...Adding Monster Failed...");
			}

		}catch(SQLException se) {
			System.err.println("[Adding Monster Error]\n" + se.getStackTrace());
		}
		return isAdded;
	}

	public String showNPCs() throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		int NPCIndex ;
		String NPCName = "";
		String NPCLocation = "";

		String str = "(id, name, location)";

		psmt = conn.prepareStatement("SELECT * FROM NPCList");
		rs = psmt.executeQuery();

		while(rs.next()) {
			NPCIndex = rs.getInt(1);
			str += "\n" + NPCIndex + " ";

			NPCName = rs.getString(2);
			str += NPCName + " ";

			NPCLocation = rs.getString(3);
			str += NPCLocation + "\n";
		}

		return str;
	}

	public boolean checkNPCDup(int NPCIndex) {
		PreparedStatement psmt = null;
		ResultSet rs;
		boolean isNPCDup = false;

		try {
			psmt = conn.prepareStatement("SELECT * FROM NPCList WHERE npc_index =?");
			psmt.setInt(1, NPCIndex);
			rs= psmt.executeQuery();

			if(rs.next())
				isNPCDup = true;

		} catch (SQLException se) {
			System.err.println("[NPC Check Error]\n" + se.getStackTrace());
		}
		return isNPCDup;
	}

	public boolean addNPC(int NPCIndex, String NPCName, String NPCLocation) {
		boolean isAdded = false;
		PreparedStatement psmt = null;

		String INSERT = "INSERT INTO NPCList (npc_index, npc_name, npc_location) VALUES (?, ?, ?)";

		try {
			psmt = conn.prepareStatement(INSERT);
			psmt.setInt(1, NPCIndex);
			psmt.setString(2, NPCName);
			psmt.setString(3, NPCLocation);

			int rep = psmt.executeUpdate();

			if(rep > 0) {
				isAdded = true;
				System.out.println("...Adding NPC Successed...");
			}else {
				System.out.println("...Adding NPC Failed...");
			}

		}catch(SQLException se) {
			System.err.println("[Adding NPC Error]\n" + se.getStackTrace());
		}
		return isAdded;
	}

	public boolean deleteNPC(int NPCIndex) {
		boolean isDeleted = false;
		PreparedStatement psmt = null;

		String DELETE = "DELETE FROM NPCList WHERE npc_index=?";

		try {
			psmt = conn.prepareStatement(DELETE);
			psmt.setInt(1, NPCIndex);

			int rep = psmt.executeUpdate();

			if(rep > 0) {
				isDeleted = true;
				System.out.println("...Deleting NPC Successed...");
			}else {
				System.out.println("...Deleting NPC Failed...");
			}
		}catch(SQLException se) {
			System.err.println("[Deleting NPC Error]\n" + se.getStackTrace());
		}
		return isDeleted;
	}

	public boolean updateNPC(int NPCIndex, String NPCName, String NPCLocation) {
		boolean isUpdated = false;
		PreparedStatement psmt = null;

		String UPDATE = "UPDATE NPCList SET npc_name = ?, npc_location = ?  WHERE npc_index = ?";

		try {
			psmt = conn.prepareStatement(UPDATE);
			psmt.setString(1, NPCName);
			psmt.setString(2, NPCLocation);
			psmt.setInt(3, NPCIndex);

			int rep = psmt.executeUpdate();

			if(rep > 0) {
				isUpdated = true;
				System.out.println("...Updating NPC Successed...");
			}else {
				System.out.println("...Updating NPC Failed...");
			}
		}catch(SQLException se) {
			System.err.println("[Updating NPC Error]\n" + se.getStackTrace());
		}
		return isUpdated;
	}
	public String showReports() throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		int reportIndex ;
		String ReportedCharacter = "";
		String ReportingCharacter = "";
		String contents = "";
		String reason = "";

		String str = "(id, reported character, reporting character, contents, reason)";

		psmt = conn.prepareStatement("SELECT * FROM ReportList");
		rs = psmt.executeQuery();

		while(rs.next()) {
			reportIndex = rs.getInt(1);
			str += "\n" + reportIndex + " ";

			ReportedCharacter = rs.getString(2);
			str += ReportedCharacter + " ";

			ReportingCharacter = rs.getString(3);
			str += ReportingCharacter + " ";

			contents = rs.getString(4);
			str += contents + " ";

			reason = rs.getString(5);
			str += reason + "\n";
		}

		return str;
	}
	public boolean checkReportsDup(int indexOfReports) {
		PreparedStatement psmt = null;
		ResultSet rs;
		boolean isReportDup = false;

		try {
			psmt = conn.prepareStatement("SELECT * FROM ReportList WHERE Report_index =?");
			psmt.setInt(1, indexOfReports);
			rs= psmt.executeQuery();

			if(rs.next())
				isReportDup = true;

		} catch (SQLException se) {
			System.err.println("[Report Check Error]\n" + se.getStackTrace());
		}
		return isReportDup;
	}
//	public boolean punish(int indexOfReports) {
//		boolean isPunished = false;
//		PreparedStatement psmt = null;
//		ResultSet rs;
//		String punishedCharacter = "";
//
//		try {
//			psmt = conn.prepareStatement("SELECT * FROM ReportList WHERE Report_index =?");
//			psmt.setInt(1, indexOfReports);
//			rs= psmt.executeQuery();
//
//			if(rs.next())
//				punishedCharacter = rs.getString(2);
//
//			updateRoport(punishedCharacter);//신고당한 캐릭터가 신고한 캐릭터에 있을 경우 deleted로 수정
//
//			_punish(punishedCharacter);//캐릭터리스트에서 해당 캐릭터 삭제
//
//		}catch(SQLException se) {
//			System.err.println("[Punish Error]\n" + se.getStackTrace());
//		}
//		return isPunished;
//	}
//
//	public void updateRoport(String characterName) {
//		boolean isUpdated = false;
//		PreparedStatement psmt = null;
//
//		String UPDATE = "UPDATE ReportList SET ReporterCharacter = 'deleted' WHERE ReporterCharacter = ?";
//
//		try {
//			psmt = conn.prepareStatement(UPDATE);
//			psmt.setString(1, characterName);
//
//			int rep = psmt.executeUpdate();
//
//			if(rep > 0) {
//				isUpdated = true;
//				System.out.println("...Updating Report Successed...");
//			}else {
//				System.out.println("...Nothing to update...");
//			}
//		}catch(SQLException se) {
//			System.err.println("[Updating Report Error]\n" + se.getStackTrace());
//		}
//	}
//
//	public void _punish(String characterName) throws SQLException{
//		PreparedStatement psmt = null;
//
//		psmt = conn.prepareStatement("DELETE FROM CharacterList WHERE character_name= ?");
//		psmt.setString(1, characterName);
//
//		int rep = psmt.executeUpdate();
//
//		if(rep > 0) {
//			System.out.println("...Deleting character Successed...");
//		}else {
//			System.out.println("...Deleting character Failed...");
//		}
//	}

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
//     PreparedStatement psmt2 = null;
//     ResultSet rs;
//     String partyindex;
//     String str;
//     String SELECT = "SELECT party_index from characterlist where character_name = ?";
//     psmt2 = conn.prepareStatement(SELECT);
//     psmt2.setString(1, character_name);
//     rs = psmt2.executeQuery();
//     while(rs.next()) {
//        partyindex = rs.getString("party_index");
//        str = partyindex;
//        }
     
     
     String INSERT = "INSERT INTO PartyList (num_party_member,party_leader,party_member1,party_member2) VALUES (0,?,null,null)";
//     if(str.equals(null)) {
//        System.out.println("you already has party!");
//     }
//     else {
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
        
//     }
     
     
     
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

	public void makecharacter(String id,int channel) throws SQLException {
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

	public String showMapPortal(String character) throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs;
		String map = "";
		String portals = "";

		String str = "";

		psmt = conn.prepareStatement("SELECT current_map FROM characterlist WHERE character_name=?");
		psmt.setString(1, character);
		rs = psmt.executeQuery();

		while(rs.next()) {
			map += rs.getString("current_map");
		}
		
		psmt = conn.prepareStatement("SELECT portal FROM MapList WHERE map_name=?");
		psmt.setString(1, map);
		rs = psmt.executeQuery();

		while(rs.next()) {
			portals += rs.getString("portal");
			str += portals + "\n";
		}

		return str;
	}
	
	public void updateCurrentMap(String map, String character) {
		boolean isUpdated;
		PreparedStatement psmt = null;

		String UPDATE = "UPDATE characterlist SET current_map = ? WHERE character_name = ?";

		try {
			psmt = conn.prepareStatement(UPDATE);
			psmt.setString(1, map);
			psmt.setString(2, character);

			int rep = psmt.executeUpdate();

			if(rep > 0) {
				isUpdated = true;
				System.out.println("...Updating Map Successed...");
			}else {
				System.out.println("...Updating Map Failed...");
			}
		}catch(SQLException se) {
			System.err.println("[Updating Map Error]\n" + se.getStackTrace());
		}
	}
	
	public String showGuildList() throws SQLException {
        PreparedStatement psmt = null;
        ResultSet rs;
        String Guild;
        String str = "";
        int count = 1;
        
        psmt = conn.prepareStatement("SELECT guild_name FROM GuildList");
        
        rs = psmt.executeQuery();
        
        while(rs.next()) {
           Guild = rs.getString("guild_name");
           str += count+". " + Guild + "\n";
           count++;
        }
        
        return str;
     }
  
  public void addGuild(String character_name, String guild_name) throws SQLException
  {
     boolean isadded = false;
     PreparedStatement psmt = null;
  
     String INSERT = "INSERT INTO GuildList (guild_name,num_guild_member,guild_leader,guild_member1,guild_member2,guild_member3,guild_member4)"
           + " VALUES (?,0,?,null,null,null,null)";
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
     
     //return isadded;
  }
  
  public void addGuild2(String character_name) throws SQLException{
     boolean isadded2 = false;
     PreparedStatement psmt = null;
     PreparedStatement psmt2 = null;
     ResultSet rs;
     String SELECT = "SELECT guild_name from guildlist where guild_leader = ?";
     String UPDATE = "UPDATE characterlist SET guild_name = ? where character_name = ?";
     String guild_name = "";
     psmt2 = conn.prepareStatement(SELECT);
     psmt2.setString(1, character_name);
     
     rs = psmt2.executeQuery();
        
     while(rs.next()) {
        guild_name = rs.getString("guild_name");
           
     }
      
     
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
     
     //return isadded2;
  }
  
  
//  public void updateGuild(String guild_member, String guild_name) throws SQLException { // 길드에 들어가고 싶은 character_name과 길드의 이름을 Argument로 전달
//        PreparedStatement psmt = null;
//        ResultSet rs;
//        String guild_member_name;
//        int count = 1;
//        String str = "";
//        PreparedStatement psmt2 = null;
//        int rs2;
//        while(true)
//        {
//           str = "";
//           psmt = conn.prepareStatement("Select guild_member"+count+" from guildList where guild_name = ?");
//           psmt.setString(1,guild_name);
//           rs = psmt.executeQuery();
//           
//           if(!rs.next())
//           {
//              System.out.print(count);
//               psmt2 = conn.prepareStatement("Update guildlist set guild_member"+count+"=? where guild_name = ?");
//               psmt2.setString(1, guild_member);
//               psmt2.setString(2, guild_name);
//               rs2 = psmt2.executeUpdate();
//               count++;
//                  
//           }
//           else
//           {
//              count++;
//           }
//         
//              
//          if(count==4)
//          {
//             break;
//          }
//           
//        }
//        
//        
//        
//        
//        
//        /*
//        PreparedStatement psmt2 = null;
//        PreparedStatement psmt3 = null;
//        PreparedStatement psmt4 = null;
//        ResultSet rs;
//        ResultSet rs2;
//        ResultSet rs3;
//        ResultSet rs4;
//        int num = 0;
//        String str = "";
//        String originalguild ="";
//        String str2 = "";
//        String guildmember = "";
//        int inputnum = 0;
//        String UPDATE[] = {
//              "party_leader",
//              "UPDATE guildlist SET num_guild_member=?, guild_member1=? where guild_name = ?",
//              "UPDATE guildlist SET num_guild_member=?, guild_member2=? where guild_name = ?",
//              "UPDATE guildlist SET num_guild_member=?, guild_member3=? where guild_name = ?",
//              "UPDATE guildlist SET num_guild_member=?, guild_member4=? where guild_name = ?",
//        };
//        String SELECT[] = {
//              "party_leader",
//              "SELECT guild_member1 from guildlist where guild_name = ?",
//              "SELECT guild_member2 from guildlist where guild_name = ?",
//              "SELECT guild_member3 from guildlist where guild_name = ?",
//              "SELECT guild_member4 from guildlist where guild_name = ?",
//        };
//        
//        String UPDATE2 = "UPDATE characeterlist SET guild_name = ? where character_name = ?";
//              
//        boolean isguild = true;
//        String UPDATE1 = "UPDATE guildlist SET num_guild_member=?, guild_member?=? where guild_name = ?";
//        String SELECT1 = "SELECT num_guild_member from guildlist where guild_name = ?";
//        String SELECT2 = "SELECT guild_member? from guildlist where guild_name = ?";
//        String SELECT3 = "SELECT guild_name from characterlist where character_name = ?";
//        
//        psmt3 = conn.prepareStatement(SELECT3);
//        psmt3.setString(1, guild_member);
//        rs3 = psmt3.executeQuery();
//        while(rs3.next()) {
//           originalguild = rs3.getString("guild_name");
//           str2 = str2 + originalguild;
//        }
//        
//        if(str2.contentEquals(""))
//           isguild = true;
//        else
//           isguild = false;
//        
//        psmt2 = conn.prepareStatement(SELECT1);
//        psmt2.setString(1, guild_name);
//        rs = psmt2.executeQuery();
//        while(rs.next()) {
//           num = rs.getInt("num_guild_member");
//              
//        }
//       
//        if(isguild) {
//           if(num==4) {
//              System.out.println("There are too many guild member! Please join otehr guild");
//           }
//           else {
//              int i =0;
//              for(i=1;i<5;i++) {
//                 String itoS = Integer.toString(i);
//                 String input = "guild_member" + itoS;
//                 guildmember = "";
//                 str = "";
//                 psmt3 = conn.prepareStatement(SELECT[i]);
//                 
//                 psmt3.setString(1, guild_name);
//                 rs2 = psmt3.executeQuery();
//                 while(rs2.next()) {
//                    guildmember = rs2.getString(input);
//                    str = str + guildmember;
//                 }
//                 System.out.println("ing...");
//                 System.out.println(str);
//                 if(str.equals(null)) {
//                    try {
//                       //String itoS2 = Integer.toString(i);
//                       num = i;
//                       psmt = conn.prepareStatement(UPDATE[i]);
//                       psmt.setInt(1, i);
//                       psmt.setString(2, guild_member);
//                       psmt.setString(3, guild_name);
//                       psmt4 = conn.prepareStatement(UPDATE2);
//                       psmt4.setString(1, guild_name);
//                       psmt4.setString(2, guild_member);
//                       int rep4 = psmt4.executeUpdate();
//                           if(rep4>0) {
//                              System.out.println("...Join Guild Successed...");
//                           }else {
//                              System.out.println("...Join Guild Failed...");
//                           }
//                       break;  
//                    }catch(SQLException se) {
//                       System.err.println("[add Guild Member Error]\n" + se.getStackTrace());
//                     }
//                 }
//              }
//           }
//        }
//        else
//        {
//           System.out.println("You already have guild!");
//        }
//           
//         */  
//           
//           
//        
//        
//          
//       
//        
////       if(num==0) {
////          inputnum = 1;
////          String UPDATE = "UPDATE partylist SET num_guild_member=?, party_member?=? where guild_name = ?";
////           
////           try {
////              psmt = conn.prepareStatement(UPDATE);
////              psmt.setInt(1, inputnum);
////              psmt.setInt(2, inputnum);
////              psmt.setString(3, guild_member);
////              psmt.setString(4,guild_name);
////              int rep = psmt.executeUpdate();
////              
////              if(rep > 0) {
////                 System.out.println("...add Guild Member Successed...");
////              }else {
////                 System.out.println("...add Guild Member Failed...");
////              }
////              
////           }catch(SQLException se) {
////              System.err.println("[add Guilkd Member Error]\n" + se.getStackTrace());
////           }
////       }
////       else if(num==1) {
////          inputnum = 2;
////          String UPDATE = "UPDATE partylist SET num_guild_member=?, party_member?=? where guild_name = ?";
////           
////           try {
////              psmt = conn.prepareStatement(UPDATE);
////              psmt.setInt(1, inputnum);
////              psmt.setInt(2, inputnum);
////              psmt.setString(3, guild_member);
////              psmt.setString(4,guild_name);
////              int rep = psmt.executeUpdate();
////              
////              if(rep > 0) {
////                 System.out.println("...add Guild Member Successed...");
////              }else {
////                 System.out.println("...add Guild Member Failed...");
////              }
////              
////           }catch(SQLException se) {
////              System.err.println("[add Guilkd Member Error]\n" + se.getStackTrace());
////           }
////       }
////       else if(num==2) {
////          inputnum = 3;
////          String UPDATE = "UPDATE partylist SET num_guild_member=?, party_member?=? where guild_name = ?";
////           
////           try {
////              psmt = conn.prepareStatement(UPDATE);
////              psmt.setInt(1, inputnum);
////              psmt.setInt(2, inputnum);
////              psmt.setString(3, guild_member);
////              psmt.setString(4,guild_name);
////              int rep = psmt.executeUpdate();
////              
////              if(rep > 0) {
////                 System.out.println("...add Guild Member Successed...");
////              }else {
////                 System.out.println("...add Guild Member Failed...");
////              }
////              
////           }catch(SQLException se) {
////              System.err.println("[add Guilkd Member Error]\n" + se.getStackTrace());
////           }
////       }
////       else if(num==3) {
////          inputnum = 4;
////          String UPDATE = "UPDATE partylist SET num_guild_member=?, party_member?=? where guild_name = ?";
////           
////           try {
////              psmt = conn.prepareStatement(UPDATE);
////              psmt.setInt(1, inputnum);
////              psmt.setInt(2, inputnum);
////              psmt.setString(3, guild_member);
////              psmt.setString(4,guild_name);
////              int rep = psmt.executeUpdate();
////              
////              if(rep > 0) {
////                 System.out.println("...add Guild Member Successed...");
////              }else {
////                 System.out.println("...add Guild Member Failed...");
////              }
////              
////           }catch(SQLException se) {
////              System.err.println("[add Guilkd Member Error]\n" + se.getStackTrace());
////           }
////       }
////       else {
////          System.out.println("There are too many members in guild!");
////       }
//       
//        
//        
//     }
//  public String getguildIndex(String character_name) throws SQLException {
//     PreparedStatement psmt = null;
//     int
//     
//     
//     return 0;
//     
//     
//  }
  
  public void updateGuild2(String guild_name, String guild_member) throws SQLException {
        PreparedStatement psmt = null;
        PreparedStatement psmt2 = null;
        int gnumber=0;
        int guildmembernum=0;
        String UPDATE1 = "UPDATE guildlist SET num_guild_member=1, guild_member1=? where guild_name = ?";
        String UPDATE2 = "UPDATE guildlist SET num_guild_member=2, guild_member2=? where guild_name = ?";
        String UPDATE3 = "UPDATE guildlist SET num_guild_member=3, guild_member3=? where guild_name = ?";
        String UPDATE4 = "UPDATE guildlist SET num_guild_member=4, guild_member4=? where guild_name = ?";
        String SELECT = "SELECT num_guild_member from guildlist where guild_name = ?";
        ResultSet rs;
        
        
        psmt2 = conn.prepareStatement(SELECT);
       psmt2.setString(1, guild_name);
       rs = psmt2.executeQuery();
       
       while(rs.next()) {
              gnumber = rs.getInt("num_guild_member");
              guildmembernum = gnumber;
           }
        System.out.println(gnumber);
       if(guildmembernum == 0) {
          try {
                 psmt = conn.prepareStatement(UPDATE1);
//                 psmt.setInt(1, 1);
                 psmt.setString(1, guild_member);
                 psmt.setString(2, guild_name);
                 
                 int rep = psmt.executeUpdate();
                 
                 if(rep > 0) {
                    System.out.println("...Update Guild Member Successed...");
                 }else {
                    System.out.println("...Update Guild Member Failed1...");
                 }
                 
              }catch(SQLException se) {
                 System.err.println("[Update Channel Error]\n" + se.getStackTrace());
              }
       }
       else if(guildmembernum ==1) {
          try {
                 psmt = conn.prepareStatement(UPDATE2);
//                 psmt.setInt(1, 2);
                 psmt.setString(1, guild_member);
                 psmt.setString(2, guild_name);
                 
                 int rep = psmt.executeUpdate();
                 
                 if(rep > 0) {
                    System.out.println("...Update Guild Member Successed...");
                 }else {
                    System.out.println("...Update Guild Member Failed2...");
                 }
                 
              }catch(SQLException se) {
                 System.err.println("[Update Channel Error]\n" + se.getStackTrace());
              }
          
       }
       else if(guildmembernum ==2) {
          try {
                 psmt = conn.prepareStatement(UPDATE3);
//                 psmt.setInt(1, 3);
                 psmt.setString(1, guild_member);
                 psmt.setString(2, guild_name);
                 
                 int rep = psmt.executeUpdate();
                 
                 if(rep > 0) {
                    System.out.println("...Update Guild Member Successed...");
                 }else {
                    System.out.println("...Update Guild Member Failed3...");
                 }
                 
              }catch(SQLException se) {
                 System.err.println("[Update Channel Error]\n" + se.getStackTrace());
              }
       }
       else if(guildmembernum ==3) {
          try {
                 psmt = conn.prepareStatement(UPDATE4);
//                 psmt.setInt(1, 4);
                 psmt.setString(1, guild_member);
                 psmt.setString(2, guild_name);
                 
                 int rep = psmt.executeUpdate();
                 
                 if(rep > 0) {
                    System.out.println("...Update Guild Member Successed...");
                 }else {
                    System.out.println("...Update Guild Member Failed4...");
                 }
                 
              }catch(SQLException se) {
                 System.err.println("[Update Channel Error]\n" + se.getStackTrace());
              }
    
       }
       else if(guildmembernum ==4) {
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
           String guild_leader="";
           String guild_member1="";
           String guild_member2="";
           String guild_member3="";
           String guild_member4="";
           String str1="";
           String str2="";
           String str3="";
           String str4="";
           String str5="";
           
           
           String SELECT = "SELECT guild_leader, guild_member1, guild_member2, guild_member3, guild_member4 from guildlist where guild_name = ?";
           psmt2 = conn.prepareStatement(SELECT);
           psmt2.setString(1, guild_name);
           rs = psmt2.executeQuery();
           while(rs.next()) {
              guild_leader = rs.getString("guild_leader");
              str1=str1 + guild_leader;
              guild_member1 = rs.getString("guild_member1");
              str2=str2 + guild_member1;
              guild_member2 = rs.getString("guild_member2");
              str3=str3 + guild_member2;
              guild_member3 = rs.getString("guild_member3");
              str4=str4 + guild_member3;
              guild_member4 = rs.getString("guild_member4");
              str5=str5 + guild_member4;
              }
           
//           System.out.println(guild_leader);
//           System.out.println(guild_member1);
//           System.out.println(guild_member2);
//           System.out.println(guild_member3);
//           System.out.println(guild_member4);
           String UPDATE = "UPDATE characterlist SET guild_index=null where character_name = ?";
           String DELETE = "DELETE FROM guildlist where guild_leader = ?";
           
           
           try {
              psmt = conn.prepareStatement(DELETE);
              psmt.setString(1, guild_leader);
              int rep = psmt.executeUpdate();
//              psmt = conn.prepareStatement(UPDATE);
//              psmt.setString(1, str1);
//              int rep = psmt.executeUpdate();
//              psmt3 = conn.prepareStatement(UPDATE);
//              psmt3.setString(1, str2);
//              int rep2 = psmt3.executeUpdate();
//              psmt4 = conn.prepareStatement(UPDATE);
//              psmt4.setString(1, str3);
//              int rep3 = psmt4.executeUpdate();
//              psmt5 = conn.prepareStatement(UPDATE);
//              psmt5.setString(1, str4);
//              int rep4 = psmt5.executeUpdate();
//              psmt6 = conn.prepareStatement(UPDATE);
//              psmt6.setString(1, str5);
//              int rep5 = psmt6.executeUpdate();
              
              if(rep > 0) {
                 System.out.println("...Delete Guild Successed...");
              }else {
                 System.out.println("...Delete Guild Failed...");
              }
              
           }catch(SQLException se) {
              System.err.println("[Delete Guild Member Error]\n" + se.getStackTrace());
           }
           
        }
        
        
     }
	

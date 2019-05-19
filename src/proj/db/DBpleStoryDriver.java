package proj.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DBpleStoryDriver {
	public static AccessDB useDB;
//	public static User user;
	public static Connection conn;
	static Scanner s = new Scanner(System.in);
	static int num;
	static String id;

	public static void main(String[] args) throws SQLException
	{
		useDB = new AccessDB();
		conn = useDB.connectDB();
//		user = new User();
		boolean isAdmin = false; //사용자인지 아닌지 구분하는 flag

		System.out.println("***Welcome to DBpleStory***");

		Scanner sc = new Scanner(System.in);
		int choice_login;
		int choice_service;

		//login part
		while(true) {
			System.out.print("\nChoose what you want.\n(1: Sign-In, 2:Sign-Up, 3:Quit)\n> ");
			choice_login = Integer.parseInt(sc.nextLine());

			if(choice_login == 1) {//로그인
				//id랑 pwd 입력받기
				int count = 0;
				while(true) {
					System.out.print("Enter your id.\n>");
					Scanner s1 = new Scanner(System.in);
					String id = s1.nextLine();
					System.out.print("Enter your password.\n>");
					String pwd = s1.nextLine();
					if(useDB.singIn(id, pwd)) {
						System.out.println("\nWelcome, " + id);
						if(id.equals("admin")) {//관리자 확인. 관리자의 id는 admin이라고 가정
							isAdmin = true;
						}
//						user.setUser_id(id);
//						user.setUser_password(pwd);
						break;
					} else {
						if(count >= 3) break;
						System.out.println("\nTry("+ count +"/3)");
						System.out.println("ID and Password do not match. Try again.");
						count++;
					continue;
					}
				}
				break;
			} else if(choice_login == 2) {//회원가입
				while(true) {
					System.out.print("Enter your id.\n>");
					Scanner s2 = new Scanner(System.in);
					String id = s2.nextLine();
					//id 중복여부확인
					if(useDB.checkIDDup(id)) {
						System.out.println("...Valid ID...");
					} else {
						System.out.println("...ID already exists...");
						continue;
					}
					//비밀번호, 생일, 이름 받기
					System.out.print("Enter your password.\n>");
					String pwd = s2.nextLine();
					System.out.print("Enter your birthday.(e.x. 990109)\n>");
					String birthday = s2.nextLine();
					System.out.print("Enter your Name.\n>");
					String name = s2.nextLine();
					if(useDB.signUp(id, pwd, birthday, name)) {
						System.out.println("\nHi, " + id);
//						user.setUser_id(id);
//						user.setUser_password(pwd);
//						user.setBirthday(birthday);
//						user.setUser_name(name);
						break;
					}
				}
			} else {//나가기
				useDB.closeDB(conn);
				System.out.println("...Bye...");
				return;
			}
		}
		//service part
		while(true) {
			if(isAdmin) {//관리자 기능
				System.out.print("\nChoose what you want to manage.\n(1: Channel, 2:User, 3:Map, 4:Item, 5:Monster, 6: NPC, 7 : Exit)\n> ");
								choice_service = Integer.parseInt(sc.nextLine());

								switch(choice_service)
								{
								case 1://채널 관리
									int choice_channel;
									System.out.print("\nChoose what you want to manage.\n(1: Add Channel, 2: Delete Channel, 3: Update Channel 4: Exit)\n> ");
									choice_channel = Integer.parseInt(sc.nextLine());

									switch(choice_channel)
									{
									case 1://채널 추가(채널명만 받는다.)
										int countAddChannel = 0;
										while(true) {
											//지금 채널 뭐 있는지 보여주기(showChannels)
											System.out.println("...Below are the channels that currently exist...");
											System.out.println(useDB.showChanneles());
											//입력받는다.
											System.out.print("Enter channel index to add.\n>");
											int channelToAdd = Integer.parseInt(sc.nextLine());
											//있는 채널이면 true(다시 입력받기)
											if(useDB.checkChannelDup(channelToAdd)) {
												if(countAddChannel >= 3) break;
												System.out.println("\nTry("+ countAddChannel +"/3)");
												System.out.println("...Channel already exists...Try again...");
												countAddChannel++;
												continue;
											}
											else {//없는 채널이면 채널추가(addChannel)
												System.out.print("Enter # of max user.\n>");
												int maxUser = Integer.parseInt(sc.nextLine());
												if(useDB.addChannel(channelToAdd, maxUser)) {
													System.out.println("Ch. " + channelToAdd + " has just added.");
												}
											}
											break;
										}

										break;
									case 2://채널 삭제
										int countDeleteChannel = 0;
										while(true) {
											//채널 보여주기
											System.out.println("...Below are the channels that currently exist...");
											System.out.println(useDB.showChanneles());
											//삭제할 채널 입력받기
											System.out.print("Enter channel index to delete.\n>");
											int channelToDelete = Integer.parseInt(sc.nextLine());
											//있으면 삭제
											if(useDB.checkChannelDup(channelToDelete)) {
												if(useDB.deleteChannel(channelToDelete)) {
													System.out.println("Ch. " + channelToDelete + " has just deleted.");
												}
											}else {//없으면 다시 입력받기
												if(countDeleteChannel >= 3) break;
												System.out.println("\nTry("+ countDeleteChannel +"/3)");
												System.out.println("...Channel does not exists...Try again...");
												countDeleteChannel++;
												continue;
											}
										break;
										}

										break;
									case 3://채널 수정
										int countUpdateChannel = 0;
										while(true) {
											//채널 보여주기
											System.out.println("...Below are the channels that currently exist...");
											System.out.println(useDB.showChanneles());
											//수정할 채널 입력받기
											System.out.print("Enter channel index to update.\n>");
											int channelToUpdate = Integer.parseInt(sc.nextLine());
											//있으면 수정
											if(useDB.checkChannelDup(channelToUpdate)) {
												System.out.print("Enter # of max user.\n>");
												int maxUser = Integer.parseInt(sc.nextLine());
												if(useDB.updateChannel(channelToUpdate, maxUser)) {
													System.out.println("Ch. " + channelToUpdate + " has just updated.");
												}
											}else {//없으면 다시 입력받기
												if(countUpdateChannel >= 3) break;
												System.out.println("\nTry("+ countUpdateChannel +"/3)");
												System.out.println("...Channel does not exists...Try again...");
												countUpdateChannel++;
												continue;
											}
										break;
										}

										break;
									case 4://나가기
										System.out.println("\n...Back to the menu...");
										break;
									default://다른 것들(나가기로 처리)
										System.out.println("\n...Back to the menu...");
										break;
									}

									break;
								case 2://유저 관리
									int choice_user;
									System.out.print("\nChoose what you want to manage.\n(1: Show Reports, 2: Exit)\n> ");
									choice_user = Integer.parseInt(sc.nextLine());

									switch(choice_user)
									{
									case 1:
				//						int countPunishCharacter = 0;
				//						while(true) {
											System.out.println("...Below are reports...");
											System.out.println(useDB.showReports());

				//							System.out.print("Enter the index of report to punish.\n>");
				//							int indexOfReports = Integer.parseInt(sc.nextLine());
				//
				//							if(useDB.checkReportsDup(indexOfReports)) {
				//								if(useDB.punish(indexOfReports)) {
				//									System.out.println("...punished...");
				//								}
				//							}else {
				//								if(countPunishCharacter >= 3) break;
				//								System.out.println("\nTry("+ countPunishCharacter +"/3)");
				//								System.out.println("...Character does not exists...Try again...");
				//								countPunishCharacter++;
				//								continue;
				//							}
				//						}

										break;
									case 2:
										System.out.println("\n...Back to the menu...");
										break;
									default:
										System.out.println("\n...Back to the menu...");
										break;
									}

									break;
								case 3://맵 관리
									int choice_map;
									System.out.print("\nChoose what you want to manage.\n(1: Add Map, 2: Delete Map, 3: Update Map 4: Exit)\n> ");
									choice_map = Integer.parseInt(sc.nextLine());

									switch(choice_map) {
									case 1://맵 추가
										int countAddMap = 0;
										while(true) {

											System.out.println("...Below are the maps that currently exist...");
											System.out.println(useDB.showMaps());

											System.out.print("Enter the name of map to add.\n>");
											String mapToAdd = sc.nextLine();

											if(useDB.checkMapDup(mapToAdd)) {
												if(countAddMap >= 3) break;
												System.out.println("\nTry("+ countAddMap +"/3)");
												System.out.println("...Map already exists...Try again...");
												countAddMap++;
												continue;
											}
											else {
												System.out.print("Enter portals in map (NOTICE: delimiter is , )\n>");
												String portals = sc.nextLine();
												if(useDB.addMap(mapToAdd, portals)) {
													System.out.println("Map [" + mapToAdd + "] has just added.");
												}
											}
											break;
										}

										break;
									case 2://맵 삭제
										int countDeleteMap = 0;
										while(true) {
											//맵 보여주기
											System.out.println("...Below are the Maps that currently exist...");
											System.out.println(useDB.showMaps());
											//삭제할 맵 입력받기
											System.out.print("Enter name of map to delete.\n>");
											String mapToDelete = sc.nextLine();
											//있으면 삭제
											if(useDB.checkMapDup(mapToDelete)) {
												if(useDB.deleteMap(mapToDelete)) {
													System.out.println("map [" + mapToDelete + "] has just deleted.");
												}
											}else {//없으면 다시 입력받기
												if(countDeleteMap >= 3) break;
												System.out.println("\nTry("+ countDeleteMap +"/3)");
												System.out.println("...Channel does not exists...Try again...");
												countDeleteMap++;
												continue;
											}
										break;
										}

										break;
									case 3://맵 수정
										int countUpdateMap = 0;
										while(true) {

											System.out.println("...Below are the maps that currently exist...");
											System.out.println(useDB.showMaps());

											System.out.print("Enter name of map to update.\n>");
											String mapToUpdate = sc.nextLine();
											//있으면 수정
											if(useDB.checkMapDup(mapToUpdate)) {
												System.out.print("Enter portals in map (NOTICE: delimiter is , )\n>");
												String portals = sc.nextLine();
												if(useDB.updateMap(mapToUpdate, portals)) {
													System.out.println("Ch. " + mapToUpdate + " has just updated.");
												}
											}else {//없으면 다시 입력받기
												if(countUpdateMap >= 3) break;
												System.out.println("\nTry("+ countUpdateMap +"/3)");
												System.out.println("...Channel does not exists...Try again...");
												countUpdateMap++;
												continue;
											}
										break;
										}

										break;
									case 4:
										System.out.println("\n...Back to the menu...");
										break;
									default:
										System.out.println("\n...Back to the menu...");
										break;
									}

									break;
								case 4://아이템 관리
									int choice_item;
									System.out.print("\nChoose what you want to manage.\n(1: Add Item, 2: Delete Item, 3: Exit)\n> ");
									choice_item = Integer.parseInt(sc.nextLine());
									switch(choice_item)
									{
									case 1://아이템 추가
										int countAddItem = 0;
										while(true) {
											//show Items
											System.out.println("...Below are the Equip items that currently exist...");
											System.out.println(useDB.showEquipItems());
											//Enter Item
											System.out.print("Enter Equip item index to add.\n>");
											int itemToAdd = Integer.parseInt(sc.nextLine());

											if(useDB.checkEquipItemDup(itemToAdd)) {
												if(countAddItem >= 3) break;
												System.out.println("\nTry("+ countAddItem +"/3)");
												System.out.println("...That item already exists...Try again...");
												countAddItem++;
												continue;
											}
											else {
												System.out.print("Enter the equip item name.\n>");
												String equipName = sc.nextLine();
												System.out.print("Enter the value.\n>");
												int equipValue = Integer.parseInt(sc.nextLine());
												System.out.print("Enter the damage.\n>");
												int equipDamage = Integer.parseInt(sc.nextLine());
												System.out.print("Enter the armor.\n>");
												int equipArmor = Integer.parseInt(sc.nextLine());

												if(useDB.addEquipItem(itemToAdd, equipName, equipValue, equipDamage, equipArmor)) {
													System.out.println("# " + itemToAdd + " " + equipName +" has just added.");
												}
											}
											break;
										}

										break;
									case 2://아이템 삭제
										int countDeleteItem = 0;
										while(true) {
											//show Items
											System.out.println("...Below are the Equip items that currently exist...");
											System.out.println(useDB.showEquipItems());
											//Enter Item
											System.out.print("Enter Equip item index to delete.\n>");
											int itemToDelete = Integer.parseInt(sc.nextLine());

											//있으면 삭제
											if(useDB.checkEquipItemDup(itemToDelete)) {
												if(useDB.deleteEquipItem(itemToDelete)) {
													System.out.println("# " + itemToDelete + " has just deleted.");
												}
											}else {//없으면 다시 입력받기
												if(countDeleteItem >= 3) break;
												System.out.println("\nTry("+ countDeleteItem +"/3)");
												System.out.println("...Item does not exists...Try again...");
												countDeleteItem++;
												continue;
											}
										break;
										}

										break;
									case 3://나가기
										System.out.println("\n...Back to the menu...");
										break;
									default://다른 것들(나가기로 처리)
										System.out.println("\n...Back to the menu...");
										break;
									}

									break;
								case 5://몬스터 관리
									int choice_monster;
									System.out.print("\nChoose what you want to manage.\n(1: Add Monster, 2: Delete Monster, 3: Exit)\n> ");
									choice_monster = Integer.parseInt(sc.nextLine());

									switch(choice_monster)
									{
									case 1://몬스터 추가
										int countAddMonster = 0;
										while(true) {
											//show Monsters
											System.out.println("...Below are the monsters that currently exist...");
											System.out.println(useDB.showMonstersAdmin());
											//Enter Monster
											System.out.print("Enter Monseter index to add.\n>");
											int monsterToAdd = Integer.parseInt(sc.nextLine());

											if(useDB.checkMonsterDup(monsterToAdd)) {
												if(countAddMonster >= 3) break;
												System.out.println("\nTry("+ countAddMonster +"/3)");
												System.out.println("...That monster already exists...Try again...");
												countAddMonster++;
												continue;
											}
											else {
												System.out.print("Enter the monster name.\n>");
												String monsterName = sc.nextLine();
												System.out.print("Enter the HP.\n>");
												int hp = Integer.parseInt(sc.nextLine());
												System.out.print("Enter the EXP.\n>");
												int exp = Integer.parseInt(sc.nextLine());
												System.out.print("Enter the appearing map.\n>");
												String appearMap = sc.nextLine();
												System.out.print("Enter the monster damage.\n>");
												int mosterDamage = Integer.parseInt(sc.nextLine());
												System.out.print("Enter the index of equip item.\n>");
												int dropEquip = Integer.parseInt(sc.nextLine());
												System.out.print("Enter the index of consume item.\n>");
												int dropConsume = Integer.parseInt(sc.nextLine());
												System.out.print("Enter the index of other item.\n>");
												int dropOthers = Integer.parseInt(sc.nextLine());

												if(useDB.addMonster(monsterToAdd, monsterName, hp, exp, appearMap, mosterDamage, dropEquip, dropConsume, dropOthers)) {
													System.out.println("# " + monsterToAdd + " " + monsterName +" has just added.");
												}
											}
											break;
										}

										break;
									case 2://몬스터 삭제
										int countDeleteMonster = 0;
										while(true) {
											//show Monsters
											System.out.println("...Below are the monsters that currently exist...");
											System.out.println(useDB.showMonstersAdmin());
											//Enter Monster
											System.out.print("Enter Monseter index to delete.\n>");
											int monsterToDelete = Integer.parseInt(sc.nextLine());

											//있으면 삭제
											if(useDB.checkMonsterDup(monsterToDelete)) {
												if(useDB.deleteMonster(monsterToDelete)) {
													System.out.println("# " + monsterToDelete + " has just deleted.");
												}
											}else {//없으면 다시 입력받기
												if(countDeleteMonster >= 3) break;
												System.out.println("\nTry("+ countDeleteMonster +"/3)");
												System.out.println("...Monster does not exists...Try again...");
												countDeleteMonster++;
												continue;
											}
										break;
										}

										break;
									case 3://나가기
										System.out.println("\n...Back to the menu...");
										break;
									default://다른 것들(나가기로 처리)
										System.out.println("\n...Back to the menu...");
										break;
									}

									break;
								case 6://npc 관리
									int choice_npc;
									System.out.print("\nChoose what you want to manage.\n(1: Add NPC, 2: Delete NPC, 3: Update NPC 4: Exit)\n> ");
									choice_npc = Integer.parseInt(sc.nextLine());

									switch(choice_npc)
									{
									case 1://npc 추가
										int countAddNPC = 0;
										while(true) {
											//show npc
											System.out.println("...Below are NPCs that currently exist...");
											System.out.println(useDB.showNPCs());
											//enter npc
											System.out.print("Enter NPC index to add.\n>");
											int NPCToAdd = Integer.parseInt(sc.nextLine());

											if(useDB.checkNPCDup(NPCToAdd)) {
												if(countAddNPC >= 3) break;
												System.out.println("\nTry("+ countAddNPC +"/3)");
												System.out.println("...NPC already exists...Try again...");
												countAddNPC++;
												continue;
											}
											else {
												System.out.print("Enter NPC name.\n>");
												String NPCName = sc.nextLine();
												System.out.print("Enter NPC location.\n>");
												String NPCLocation = sc.nextLine();

												if(useDB.addNPC(NPCToAdd, NPCName, NPCLocation)) {
													System.out.println("# " + NPCToAdd + " " + NPCName +" has just added.");
												}
											}
											break;
										}

										break;
									case 2://npc 삭제
										int countDeleteNPC = 0;
										while(true) {
											System.out.println("...Below are NPCs that currently exist...");
											System.out.println(useDB.showNPCs());

											System.out.print("Enter NPC index to delete.\n>");
											int NPCToDelete = Integer.parseInt(sc.nextLine());

											if(useDB.checkNPCDup(NPCToDelete)) {
												if(useDB.deleteNPC(NPCToDelete)) {
													System.out.println("# " + NPCToDelete + " has just deleted.");
												}
											}else {
												if(countDeleteNPC >= 3) break;
												System.out.println("\nTry("+ countDeleteNPC +"/3)");
												System.out.println("...NPC does not exists...Try again...");
												countDeleteNPC++;
												continue;
											}
										break;
										}

										break;
									case 3://npc 수정
										int countUpdateNPC = 0;
										while(true) {
											System.out.println("...Below are NPCs that currently exist...");
											System.out.println(useDB.showNPCs());

											System.out.print("Enter NPC index to delete.\n>");
											int NPCToUpdate = Integer.parseInt(sc.nextLine());

											if(useDB.checkNPCDup(NPCToUpdate)) {
												System.out.print("Enter the NPC name.\n>");
												String NPCName = sc.nextLine();

												System.out.print("Enter the NPC location.\n>");
												String NPCLocation = sc.nextLine();

												if(useDB.updateNPC(NPCToUpdate, NPCName, NPCLocation)) {
													System.out.println("# " + NPCToUpdate + " has just updated.");
												}
											}else {
												if(countUpdateNPC >= 3) break;
												System.out.println("\nTry("+ countUpdateNPC +"/3)");
												System.out.println("...NPC does not exists...Try again...");
												countUpdateNPC++;
												continue;
											}
										break;
										}

										break;
									case 4://나가기
										System.out.println("\n...Back to the menu...");
										break;
									default://다른 것들(나가기로 처리)
										System.out.println("\n...Back to the menu...");
										break;
									}

									break;

								case 7://종료
									useDB.closeDB(conn);
									System.out.println("...System OFF...");
									return ;
								default:
									useDB.closeDB(conn);
									System.out.println("...System OFF...");
									return ;
								}
							}
			else {//일반 사용자 기능
				//형님들 여기부터 부탁드립니다!
				selectChannel();
				break;
			}
		}
	}

	public static void selectChannel() throws SQLException {
		int num;
		String channel = useDB.showChanneles();

		System.out.println("Channels\tpresent_user");
		System.out.println(channel);

		System.out.print("Select Channnel : ");
		num = s.nextInt();

		channelCharacter(num);
	}

	public static void channelCharacter(int channel) throws SQLException {
		int num;

		System.out.println("1. Character Select");
		System.out.println("2. Channel Select");
//		System.out.println("3. Quit");

		num = s.nextInt();

		switch(num) {
		case 1:
			selectCharacter(channel);
			break;
		case 2:
			selectChannel();
			break;
		default:
			break;
		}
	}

	public static void selectCharacter(int channel) throws SQLException {
		int index;
		String characters;
		String character;

		characters = useDB.showCharacter();

		System.out.println("Character");
		System.out.println(characters);

		System.out.println("Select Character");

		index = s.nextInt();
		useDB.updateCharacter(channel);

		character = useDB.selectCharacter(index);
		characterMenu(character);
	}

	public static void characterMenu(String character) throws SQLException {
		int index;
		int monster_select;
        int monster_behave=5;
        int skill_index;
        int consume_index;

		while(true) {
			System.out.println("Character Menu");
			System.out.println("1. My Information");
			System.out.println("2. Friend");
			System.out.println("3. Party");
			System.out.println("4. Guild");
			System.out.println("5. Map");
			System.out.println("6. Move Map");
			System.out.println("7. Hunting");
			System.out.println("8. Chat");
			System.out.println("9. Shop");
			System.out.println("10. Ranking\n");

			System.out.print("Select menu\n>");
			index = s.nextInt();

			switch(index) {
			case 1:
	            characterInfo(character);
	            break;
	         case 2:
	            characterFriend(character);
	            break;
	         case 3:
	            characterParty(character);
	            break;
			case 4:
				characterGuild(character);
				break;
			case 5:
				map();
				break;
			case 6:
				mapMove(character);
				break;
			case 7:
				String monster = null;
		   		
		   		useDB.showMonsters(character);
		   		System.out.print("What do you hunt monster?:(back:0) ");
		   		monster_select = s.nextInt();
		   		
		   		if(monster_select==0) {
		   		 break;
		   		}
		   		System.out.print("What do you do?:(1.attack, 2.skill_attack, 3.consume, 4.run )");
		   		monster_behave = s.nextInt();
		   		switch(monster_behave) {
		   	 	case 1 : useDB.AttactMonster(character,useDB.selectedMonsters(character, monster_select));
		   	 		break;
		   	 	case 2 : System.out.print("What skill do you use?:(1 or 2) ");
		   	 			 skill_index = s.nextInt();
		   	 			 System.out.println(useDB.selectedMonsters(character, monster_select));
		   	 			 useDB.skillmonster(character, useDB.selectedMonsters(character, monster_select), skill_index);
		   	 		break;
		   	 	case 3 : useDB.showconsume(character);
		   	 			 System.out.print("Which do you consume? ");
		   	 			 consume_index = s.nextInt();
		   	 			useDB.consumeitem(character, consume_index);
		   	 		break;
		   	 }
		   	 if(monster_behave==4)
		   	 {
		   		 
		   		 break;
		   	 }
				break;
			case 8:
				chat(character);
				break;
			case 9:
				shop(character);
				break;
			case 10:
				rank(character);
				break;
			default :
				break;
			}
		}
	}
	
	public static void characterInfo(String character) throws SQLException{
      int index1;
       System.out.println("Select menu");
       System.out.println("1. Show equip item");
       System.out.println("2. Show consume item");
       System.out.println("3. Show other item");
       index1 = s.nextInt();
       
       switch(index1) {
       case 1:
          ArrayList<String> equip;
          int count = 0;
          
          equip=useDB.showEquipInventory(character);
          for (int i = 0; i < equip.size() / 3; i++) {
        	  System.out.println(equip.get(count * 3 + 1));
        	  count++;
          }
          System.out.println();
        	  
          break;
       
       case 2: 
          String consume;
          consume=useDB.showConsumeInventory(character); 
          System.out.println(consume);
          break;
       
       case 3:
          String other;
          other = useDB.showOtherInventory(character);
          System.out.println(other);
          break;
       }
	}
	
   public static void characterFriend(String character) throws SQLException{
      int index2;
       System.out.println("Select menu");
       System.out.println("1. Show Friend List");
       System.out.println("2. Add Friend");
       index2 = s.nextInt();
       
       switch(index2) {
       case 1:
          String friend;
          
          friend=useDB.showFriendList(character);
          System.out.println(friend);
          break;
       
       case 2:
       boolean friendadd;
       String skip;
       String friendname;
       System.out.println("input friend name");
       skip = s.nextLine();
       friendname = s.nextLine();
          useDB.addFriend(character, friendname);
          break;
       }
   }
	   
   public static void characterParty(String character) throws SQLException{
      int index3;
      System.out.println("Select menu");
      System.out.println("1. Show Party List");
      System.out.println("2. Make Party");
      System.out.println("3. Join Party");
      System.out.println("4. Delete Party (leader only)");
      System.out.println("5. Exit Party (member only)");
      index3 = s.nextInt();
      
      switch(index3) {
      case 1:
    	  String partyList;
    	  partyList=useDB.showPartyList();
    	  System.out.println(partyList);
    	  break;
       
       case 2:
    	  useDB.addParty(character);   
    	  useDB.addParty2(character);
    	  break;
       
       case 3:
    	  String skip;
	      int partyindex;
	      int membernum;
	       
	      System.out.println("input party index");
	      skip = s.nextLine();
	      partyindex = s.nextInt();
	       
	      membernum = useDB.partymemberNum(partyindex);
	       
	      if(membernum == 0)
	    	  useDB.updateParty1(character, partyindex);
	          if(membernum == 1)
	             useDB.updateParty2(character, partyindex);
	          
	      break;
	          
       case 4:
    	   int pindex;
	       pindex = useDB.partyindexGet(character);
	       useDB.DeleteParty(pindex);
	          
	       break;
	   case 5:
	       int ptindex;
	       
	       ptindex = useDB.partyindexGet2(character);
	       System.out.println(ptindex);
	       useDB.ExitParty(character, ptindex);
	       break;
	   }
	}
   
   public static void characterGuild(String character) throws SQLException{
	      int index4;
	      System.out.println("Select menu");
	      System.out.println("1. Show Guild List");
	       System.out.println("2. Make Guild");
	       System.out.println("3. Join Guild");
	       System.out.println("4. Delete Guild (leader only)");
	       index4 = s.nextInt();
	       String skip;
	       skip = s.nextLine();
	       switch(index4) {
	       case 1:
	          String guildList;
	          guildList=useDB.showGuildList();
	          System.out.println(guildList);
	          break;
	       
	       case 2:
	       System.out.println("Input guild name");
	       String gname;
	       gname = s.nextLine();
	       useDB.addGuild(character, gname);   
	       useDB.addGuild2(character);
	          break;
	       
	       case 3:
	       
	       String wantguild;
	       int membernum;
	       
	       System.out.println("input guild name");
	       wantguild = s.nextLine();
	       
	       //membernum = useDB.partymemberNum(partyindex);
	       
	       useDB.updateGuild2(wantguild, character);
	          
	          break;
	          
	       case 4:
	          String wantdelete;
	          
	          System.out.println("input delete guild name");
	          wantdelete = s.nextLine();
	          
	          useDB.DeleteGuild(wantdelete);
	          
	          break;
	       case 5:
	          int ptindex;
	       
	          ptindex = useDB.partyindexGet2(character);
	          System.out.println(ptindex);
	          useDB.ExitParty(character, ptindex);
	          break;
	          
	          
	          
	       }
	   }
   
   	public static void map() throws SQLException {
   		System.out.println("...Below are the maps that currently exist...");
		System.out.println(useDB.showMaps());
   	}
   	
   	public static void mapMove(String character) throws SQLException {
   		String map;
   		
   		System.out.println("...Below are the maps that currently exist...");
		System.out.println(useDB.showMapPortal(character));
		
		map = s.nextLine();
		System.out.println("Select Portal");
		map = s.nextLine();
		
		useDB.updateCurrentMap(map, character);
   	}
   	
   	public static void hunt(String character) throws SQLException {
   		
   		
   	}

	public static void chat(String character) throws SQLException {
		int index;

		System.out.println("Select Mode");
		System.out.println("1. Chat");
		System.out.println("2. Show Chat");

		index = s.nextInt();

		switch(index) {
		case 1:
			doChat(character);
			break;
		case 2:
			showChat(character);
			break;
		default :
			break;
		}
	}

	public static void doChat(String character) throws SQLException {
		String content;
		String map;

		System.out.print(">");
		content = s.nextLine();
		content = s.nextLine();
		map = useDB.currentMap(character);

		useDB.insertChat(content, character, map);
	}

	public static void showChat(String character) throws SQLException {
		int index;

		System.out.println("\nChat List");
		System.out.println(useDB.showChat(useDB.currentMap(character)));

		System.out.println("Select Mode");
		System.out.println("1. Go to Menu");
		System.out.println("2. Report");

		index = s.nextInt();

		switch(index) {
		case 1:
			break;
		case 2:
			report(character);
			break;
		}
	}

	public static void report(String character) throws SQLException {
		int index;
		String reason;
		String map = useDB.currentMap(character);

		System.out.println("\nChoose user to report");
		System.out.println("0. to Quit");
		System.out.println(useDB.showChat(map));

		index = s.nextInt();

		String chat[] = useDB.searchChat(index, map);

		reason = s.nextLine();
		System.out.print("Please write reason\n>");
		reason = s.nextLine();

		if (index != 0) {
			useDB.reportChat(chat[1], character, chat[0], reason);
		}
	}

	public static void shop(String character) throws SQLException {
		int index;

		System.out.print("1. Sell Item 2. Buy Item : ");
		index = s.nextInt();

		switch(index) {
		case 1:
			sell(character);
			break;
		case 2:
			buy(character);
			break;
		}
	}

	public static void sell(String character) throws SQLException {
		ArrayList<String> inventory = useDB.showEquipInventory(character);
		int index;
		int count = 0;

		System.out.println("Equip Inventory List");
		for(int i = 0; i < inventory.size(); i++) {
			System.out.println(inventory.get(i) + ". " + inventory.get(i+1) + " - " + inventory.get(i+2) + " meso");
			i++;
			i++;
			count++;
		}

		System.out.println("Select Item number to sell (0 to Quit)");

		index = s.nextInt();
		index--;

		if(index != -1) {
			useDB.sellItem(index + 1, character, Integer.parseInt(inventory.get((count-1) * 3 + 2)));
		}
	}

	public static void buy(String character) throws SQLException {
		ArrayList<String> itemlist = useDB.showEquipList();
		int index;
		int count = 1;

		System.out.println("Selling Equip List");
		for(int i = 0; i < itemlist.size(); i++) {
			System.out.println(count++ + ". " + itemlist.get(i) + " - " + itemlist.get(i+1) + " meso");
			i++;
		}

		System.out.println("Select Item number to buy (0 to Quit)");

		index = s.nextInt();
		index--;

		if(index != -1) {
			useDB.buyItem(index + 1, character, Integer.parseInt(itemlist.get(index * 2 + 1)));
		}
	}

	public static void rank(String character) throws SQLException {
		int index;

		System.out.println("Select Option");
		System.out.println("1. Level");
		System.out.println("2. Famous");

		index = s.nextInt();

		ArrayList<String> info = useDB.getRank(index);

		for(int i = 0; i < info.size() / 2; i++) {
			System.out.println(info.get(i) + " -> " + info.get(i + 1) + "\n");
		}

	}
}

package proj.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DBpleStoryDriver {
	public static AccessDB useDB;
//	public static User user;
	public static Connection conn;
	static Scanner s = new Scanner(System.in);
	
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
			System.out.print("\nChoose what you want. (1: Sign-In, 2:Sign-Up, 3:Quit)\n> ");
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
				System.out.print("\nChoose what you want to manage. (1: Channel, 2:User, 3:Map, 4:Item, 5:Monster, 6: NPC, 7 : exit)\n> ");
				choice_service = Integer.parseInt(sc.nextLine());
				
				switch(choice_service) {
				case 1:
					int choice_channel;
					System.out.print("\nChoose what you want to manage. (1: Add Channel, 2: Delete Channel, 3: Revise Channel 4: exit)\n> ");
					choice_channel = Integer.parseInt(sc.nextLine());
					
					switch(choice_channel) {
					case 1://채널 추가(채널명만 받는다.)
						while(true) {
							//지금 채널 뭐 있는지 보여주기(showChannels)
							System.out.println("...Below are the channels that currently exist...");
							System.out.println(useDB.showChannels());
							//입력받는다.
							System.out.print("Enter channel index to add.\n>");
							int channelToAdd = Integer.parseInt(sc.nextLine());
							
							//있는 채널이면 false(다시 입력받게 하기)
							if(useDB.checkChannelDup(channelToAdd)) {
								System.out.println("...Channel already exists...");
								continue;
							}
							else {//없는 채널이면 채널추가(addChannel)
								System.out.print("Enter # of max user.\n>");
								int maxUser = Integer.parseInt(sc.nextLine());
								if(useDB.addChannel(channelToAdd, maxUser)) {
									System.out.println("Ch. " + channelToAdd + "has just added.");
								}
							}
							break;
						}
						break;
					case 2://채널 삭제
						break;
					case 3://채널 수정
						break;
					case 4://나가기
						break;
					default://다른 것들(나가기로 처리)
						break;
					}
					
					break;
				case 2:
					
					break;
				case 3:
					
					break;
				case 4:
					
					break;
				case 5:
					
					break;
					
				case 6:
					
					break;
				
				case 7:
				
					break;
				default:
					
					break;
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
		String channel = useDB.showChannels();
		
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
				
				break;
			case 2:
				
				break;
			case 3:
				
				break;
			case 4:
				
				break;
			case 5:
				
				break;
			case 6:
				
				break;
			case 7:
				
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
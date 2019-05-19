package proj.db;

import java.sql.*;
import java.util.Scanner;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DBpleStoryDriver {
   public static AccessDB useDB;
//   public static User user;
   public static Connection conn;
   static Scanner s = new Scanner(System.in);
   static int num;
   static String id;
   
   public static void main(String[] args) throws SQLException
   {
      useDB = new AccessDB();
      conn = useDB.connectDB();
//      user = new User();
      boolean isAdmin = false; //��������� �ƴ��� �����ϴ� flag
     
      
      System.out.println("***Welcome to DBpleStory***");
      
      Scanner sc = new Scanner(System.in);
      int choice_login;
      int choice_service;
      
      //login part
      while(true) {
         System.out.print("\nChoose what you want. (1: Sign-In, 2:Sign-Up, 3:Quit)\n> ");
         choice_login = Integer.parseInt(sc.nextLine());
         
         if(choice_login == 1) {//�α���
            //id�� pwd �Է¹ޱ�
            int count = 0;
            while(true) {
               System.out.print("Enter your id.\n>");
               Scanner s1 = new Scanner(System.in);
               id = s1.nextLine();
               System.out.print("Enter your password.\n>");
               String pwd = s1.nextLine();
               if(useDB.singIn(id, pwd)) {
                  System.out.println("\nWelcome, " + id);
                  if(id.equals("admin")) {//������ Ȯ��. �������� id�� admin�̶�� ����
                     isAdmin = true;
                  }
//                  user.setUser_id(id);
//                  user.setUser_password(pwd);
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
         } else if(choice_login == 2) {//ȸ������
            while(true) {
               System.out.print("Enter your id.\n>");
               Scanner s2 = new Scanner(System.in);
               id = s2.nextLine();
               //id �ߺ�����Ȯ��
               if(useDB.checkIDDup(id)) {
                  System.out.println("...Valid ID...");
               } else {
                  System.out.println("...ID already exists...");
                  continue;
               }
               //��й�ȣ, ����, �̸� �ޱ�
               System.out.print("Enter your password.\n>");
               String pwd = s2.nextLine();
               System.out.print("Enter your birthday.(e.x. 990109)\n>");
               String birthday = s2.nextLine();
               System.out.print("Enter your Name.\n>");
               String name = s2.nextLine();
               if(useDB.signUp(id, pwd, birthday, name)) {
                  System.out.println("\nHi, " + id);
//                  user.setUser_id(id);
//                  user.setUser_password(pwd);
//                  user.setBirthday(birthday);
//                  user.setUser_name(name);
                  break;
               }
            }
         } else {//������
            useDB.closeDB(conn);
            System.out.println("...Bye...");
            return;
         }   
      }
      //service part
      while(true) {
         if(isAdmin) {//������ ���
            System.out.print("\nChoose what you want to manage. (1: Channel, 2:User, 3:Map, 4:Item, 5:Monster, 6: NPC, 7 : exit)\n> ");
            choice_service = Integer.parseInt(sc.nextLine());
            
            switch(choice_service) {
            case 1:
               int choice_channel;
               System.out.print("\nChoose what you want to manage. (1: Add Channel, 2: Delete Channel, 3: Revise Channel 4: exit)\n> ");
               choice_channel = Integer.parseInt(sc.nextLine());
               
               switch(choice_channel) {
               case 1://ä�� �߰�(ä�θ� �޴´�.)
                  while(true) {
                     //���� ä�� �� �ִ��� �����ֱ�(showChannels)
                     System.out.println("...Below are the channels that currently exist...");
                     System.out.println(useDB.showChannels());
                     //�Է¹޴´�.
                     System.out.print("Enter channel index to add.\n>");
                     int channelToAdd = Integer.parseInt(sc.nextLine());
                     
                     //�ִ� ä���̸� false(�ٽ� �Է¹ް� �ϱ�)
                     if(useDB.checkChannelDup(channelToAdd)) {
                        System.out.println("...Channel already exists...");
                        continue;
                     }
                     else {//���� ä���̸� ä���߰�(addChannel)
                        System.out.print("Enter # of max user.\n>");
                        int maxUser = Integer.parseInt(sc.nextLine());
                        if(useDB.addChannel(channelToAdd, maxUser)) {
                           System.out.println("Ch. " + channelToAdd + "has just added.");
                        }
                     }
                     break;
                  }
                  break;
               case 2://ä�� ����
                  break;
               case 3://ä�� ����
                  break;
               case 4://������
                  break;
               default://�ٸ� �͵�(������� ó��)
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
         else {//�Ϲ� ����� ���
            //���Ե� ������� ��Ź�帳�ϴ�!
            selectChannel();
            break;
         }
      }
   }
   
   public static void selectChannel() throws SQLException {
     
      
      String channel = useDB.showChannels();
      
      System.out.println("Channels\tpresent_user");
      System.out.println(channel);
      
      System.out.print("Select Channnel : ");
      num = s.nextInt();
      
      channelCharacter(num);
   }
   
   public static void channelCharacter(int channel) throws SQLException {
      int num1;
      int select = 0;
      
      System.out.println("1. Character Select");
      System.out.println("2. Channel Select");
//      System.out.println("3. Quit");
      
      num1 = s.nextInt();
      
      switch(num1) {
      case 1:
    	 while(true)
    	 {
    	 System.out.println("1. Select");
    	 System.out.println("2. make");
    	 select = s.nextInt();
    	 if(select==1)
    	 {
    		 selectCharacter(channel);
    		 break;
    	 }
    	 else if(select==2)
    	 {
    		 useDB.makecharacter(id, channel);
    	 }
    	 }
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
      //useDB.updateCharacter(channel);
      
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
        	 String monster = null;
        	 useDB.showMonsters(character);
        	 System.out.print("What do you hunt monster?:(back:0) ");
        	 monster_select = s.nextInt();
        	 if(monster_select==0)
        	 {
        		 break;
        	 }
        	 System.out.print("What do you do?:(1.attack, 2.skill_attack, 3.consume, 4.run )");
        	 monster_behave = s.nextInt();
        	 switch(monster_behave)
        	 {
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
            
            break;
         case 10:
            
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
         //doChat(character);
         break;
      case 2:
        // showChat(character);
         break;
      default :
         break;
      }
   }
   /*
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
         
         break;
      case 2:
         
         break;
      }
   }
   
   public static void sell(String character) throws SQLException {
      ArrayList<String> inventory = useDB.showEquipInventory(character);
   }
*/
}
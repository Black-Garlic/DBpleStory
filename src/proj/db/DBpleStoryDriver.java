package proj.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
public class DBpleStoryDriver {
   public static AccessDB useDB;
//   public static User user;
   public static Connection conn;
   static Scanner s = new Scanner(System.in);
   
   public static void main(String[] args) throws SQLException
   {
      useDB = new AccessDB();
      conn = useDB.connectDB();
//      user = new User();
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
//                  user.setUser_id(id);
//                  user.setUser_password(pwd);
//                  user.setBirthday(birthday);
//                  user.setUser_name(name);
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
//      System.out.println("3. Quit");
      
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
   
   public static void characterInfo(String character) throws SQLException{
      int index1;
       System.out.println("Select menu");
       System.out.println("1. Show equip item");
       System.out.println("2. Show consume item");
       System.out.println("3. Show other item");
       index1 = s.nextInt();
       
       switch(index1) {
       case 1:
          String equip;
          
          equip=useDB.showEquipInventory2(character);
          System.out.println(equip);
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
           characterInfo(character);
        
            break;
         case 2:
            characterFriend(character);
            break;
         case 3:
            characterParty(character);
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
         
         break;
      case 2:
         
         break;
      }
   }
   
   public static void sell(String character) throws SQLException {
      ArrayList<String> inventory = useDB.showEquipInventory(character);
   }
   
   
   
}









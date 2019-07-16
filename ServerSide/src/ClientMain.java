import connection.ClientSideImp.ClientSideImp;
import connection.ServerSideIF;
import dao.daoExc.GetUserex;
import models.Channel;
import models.Group;
import models.ProfileInfo;
import models.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        Scanner scanner = new Scanner(System.in);
        String url = "rmi://localhost/Test";
        ServerSideIF chatServerIF = (ServerSideIF) Naming.lookup(url);
        ClientSideImp csi = ClientSideImp.getInstance(chatServerIF,"1234567890qwerty");
        String s;
        String t;
        String o;
        String u;
        outterline:
        while (true){
            switch (s = scanner.nextLine()){
                case "login":
                    t = scanner.nextLine();
                    o = scanner.nextLine();
                    System.out.println(csi.login(t,o));
                    break;
                case "getuserstatus":
                    try {
                        t = scanner.nextLine();
                        System.out.println(csi.get_Status(t));
                    } catch (GetUserex getUserex) {
                        getUserex.printStackTrace();
                    }
                    break;
                case "createChannel":
                    t = scanner.nextLine();
                    Channel channel = new Channel(t,null,"hiii",new Date());
                    ClientSideImp.getInstance().createChannel(channel);
                    break;
                case "joinChannel":
                    t = scanner.nextLine();
                    ClientSideImp.getInstance().joinChannel(t);
                    break ;
                case "createGroup":
                    t = scanner.nextLine();
                    Group group = new Group(t,"hi",null,"zz",new Date());
                    ClientSideImp.getInstance().createGroup(group,new ArrayList<>());
                    break;
                case "joinGroup":
                    t = scanner.nextLine();
                    ClientSideImp.getInstance().joinGroup(t);
                    break ;
                case "getlastseen":
                    t = scanner.nextLine();
                    System.out.println(csi.get_lastseen(t));
                    break;
                case "getallmsg":
                    t = scanner.nextLine();
                    HashMap<Integer, ArrayList> msg = csi.getmsg_between_2person(t);
                    for (int i = 0; i < msg.size(); i++) {
                        msg.get(i).stream().forEach(x -> System.out.print(x + " "));
                        System.out.println();
                    }
                    break;
                case "getalluser":
                    ArrayList<String> users = csi.get_All_User();
                    for (int i = 0; i < users.size(); i++) {
                        System.out.print(users.get(i) + " ");
                    }
                    break;
                case "sendmsg":
                    t = scanner.nextLine();
                    o = scanner.nextLine();
                    csi.sendmsg(t,o);
                    break;
                case "seeprof":
                    t = scanner.nextLine();
                    ProfileInfo pi = csi.get_User_Profile(t);
                    System.out.println(pi.getFirstname());
                    System.out.println(pi.getLastname());
                    System.out.println(pi.getUsername());
                    break;
                case "uploadfile":
                    t = scanner.nextLine();
                    o = scanner.nextLine();
                    u= scanner.nextLine();
                    File file = new File(t);
                    csi.upload_File(file,o,u);
                    break;
                case "downloadfile":
                    t = scanner.nextLine();
                    o = scanner.nextLine();
                    u= scanner.nextLine();
                    csi.download_File(t,o,u);
                    break;
                case "editprof":
                    File file1 = new File("C:\\Users\\ASuS\\Downloads\\2016-03-25-14-15-28--1047324899.jpg");
                    byte[] ss = new byte[(int)file1.length()];
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file1);
                        fileInputStream.read(ss);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    User user = new User("ddd3","skdfj","sdlfj","ddd3","sdfsf",new Date(),new Date(),ss);
                    ClientSideImp.getInstance().edit_profile(user);
                case "exit":
                    break outterline;
            }
        }
        /*new Thread(() -> {
            try {
                new ClientSideImp(chatServerIF,"1234567890qwerty");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();*/
    }
}

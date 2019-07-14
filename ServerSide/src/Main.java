import JpaConfig.JPAUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import connection.ServerSideIF;
import connection.ServerSideImp.ServerSideImp;
import connection.SocketConnection.ServerSocket;
import dao.ChannelDao;
import dao.ChannelDaoImp.ChannelDaoImp;
import dao.GroupDao;
import dao.GroupDaoImp.GroupDaoImp;
import dao.MessageQuery;
import dao.MessageQueryImp.MessageQueryImp;
import dao.UserDaoImp.UserDaoImp;
import dao.daoExc.GetUserex;
import models.Channel;
import models.Group;
import models.GroupMessage;
import models.User;
import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import protections.RSA;

import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.metamodel.EntityType;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(final String[] args){
        //RSA rsa = new RSA("ss");
        UserDaoImp userDaoImp = new UserDaoImp();
        String s = "slam";
        byte[] o = s.getBytes();
        /*User user = new User("ahmad1","mohmadi1","sdkjf@djfk.com",
                "ddddddd1","sdfsf",new Date(),new Date(),o);
        User user1 = new User("ahmad2","mohmadi2","sdkjf@djdsfk.com",
                "ddddddd2","sdfsf",new Date(),new Date(),o);
        User user2 = new User("ahmad3","mohmadi3","sdkjf@djfdsk.com",
                "ddddddd3","sdfsf",new Date(),new Date(),o);
        userDaoImp.addUser(user);
        userDaoImp.addUser(user1);
        userDaoImp.addUser(user2);*/
        Group group = new Group("sdfs","sfsdf","sfsf","sdfsf",new Date());
        GroupMessage groupMessage = new GroupMessage("ss","sss",new Date());
        ChannelDao channelDao = new ChannelDaoImp();
        Channel channel = new Channel("sjfk","fdsf","dfjj",new Date());
        GroupDao groupDao = new GroupDaoImp();
        group.getGroupMessages().add(groupMessage);
        groupDao.updateGroup(group);
        channelDao.addChannel(channel);
        //MessageQuery msg = new MessageQueryImp();
        try {
            User u1 = userDaoImp.getUser("ddddddd1");
            User u2 = userDaoImp.getUser("ddddddd2");
            Channel c = channelDao.getChannel("sjfk");
            c.getUsers().add(u1);
            c.getUsers().add(u2);
            u1.getChannels().add(c);
            u2.getChannels().add(c);
            channelDao.updateChannel(c);
            /*userDaoImp.updateUser(u1);
            userDaoImp.updateUser(u2);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Group u = groupDao.getGroup("sdfs");
            System.out.println(u.getUsers().iterator().next().getUserName());
            System.out.println(u.getGroupMessages().get(0).getMessage());
            User uu = userDaoImp.getUser("ddddddd2");
            System.out.println(uu.getGroups().iterator().next().getName());
        } catch (GetUserex getUserex) {
            getUserex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*EntityManager em=null;
        EntityTransaction et=null;
        try {
            em = JPAUtil.getEntitManager();
            et = em.getTransaction();
            et.begin();
            em.persist(groupMessage);
            et.commit();
        }catch (Exception ex){
            et.rollback();
            ex.printStackTrace();
            System.out.println("-----------------------------------------------------" +
                    "------------------------------------------------");
            System.out.println("-----------------------------------------------------" +
                    "-------------------------------------------------");
            System.out.println(ex.getCause());
        }finally {
            em.close();
        }*/
        //msg.createTable();
        //msg.createTable();
        /*msg.addChat("ddddddd1","ddddddd2");
        msg.addChat("sdfsksdfsjf","slamsf");
        msg.addMessage("slam khobi halet chetore","ddddddd1","ddddddd2",0);
        msg.addMessage("man khobam to khobi?","ddddddd2","ddddddd1",0);
        msg.addMessage("are che khabar?","ddddddd1","ddddddd2",0);*/
        MessageQuery msg = new MessageQueryImp();
        msg.updateUser("ddddddd2","ddd2");
        msg.updateUser("ddddddd1","ddd1");
        //msg.addMessage("salamati","slam","sdfskjf",0);
        //msg.addMessage("salamati","sdfsksdfsjf","slamsf");
        /*HashMap<Integer, ArrayList> mess = new Gson().fromJson(msg.getChatBetweenTwoPerson("sdfskjf","slam"),
                new TypeToken<HashMap<Integer, ArrayList>>() {}.getType());
        System.out.println(new TypeToken<HashMap<Integer, ArrayList>>() {}.getType());
        for (int i = 0; i < mess.size(); i++) {
            System.out.println(mess.get(i).get(0)+" : "+mess.get(i).get(1)+" : " + mess.get(i).get(2) +" : " + mess.get(i).get(3));
        }*/
        /*HashMap<Integer, ArrayList> mes = msg.getAllChat("sdfsksdfsjf");
        for (int i = 0; i < mes.size(); i++) {
            System.out.println(mes.get(i).get(0) +" : "+mes.get(i).get(1) +" to "+mes.get(i).get(2));
        }*/
        /*try {
            msg.isChatExist("slam","sdfsksdfsjf");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            //Thread thread = new Thread(() -> ServerSocket.start());
            //thread.start();
            InetAddress inetAddress = InetAddress.getLocalHost();
            ServerSideIF chatServer = new ServerSideImp();
            /*ArrayList<String> user = new Gson().fromJson(chatServer.getAllUser(),ArrayList.class);
            for (int i = 0; i < user.size(); i++) {
                System.out.println(user.get(i));
            }*/
            System.setProperty("java.rmi.server.hostname",inetAddress.getHostAddress());
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Test",chatServer);
            System.out.println("serer ready");
            //Naming.rebind("Test",chatServer);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
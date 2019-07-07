import JpaConfig.JPAUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import connection.ServerSideIF;
import connection.ServerSideImp.ServerSideImp;
import connection.SocketConnection.ServerSocket;
import dao.MessageQuery;
import dao.MessageQueryImp.MessageQueryImp;
import dao.UserDaoImp.UserDaoImp;
import dao.daoExc.GetUserex;
import models.User;
import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.metamodel.EntityType;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(final String[] args){
        /*UserDaoImp userDaoImp = new UserDaoImp();
        User user = new User("ahmad1","mohmadi1","sdkjf@djfk.com",
                "ddddddd1","sdfsf",new Date(),new Date());
        User user1 = new User("ahmad2","mohmadi2","sdkjf@djdsfk.com",
                "ddddddd2","sdfsf",new Date(),new Date());
        User user2 = new User("ahmad3","mohmadi3","sdkjf@djfdsk.com",
                "ddddddd3","sdfsf",new Date(),new Date());
        userDaoImp.addUser(user);
        userDaoImp.addUser(user1);
        userDaoImp.addUser(user2);*/
        MessageQuery msg = new MessageQueryImp();
        //msg.createTable();
        //msg.createTable();
        /*msg.addChat("sdfskjf","slam");
        msg.addChat("sdfsksdfsjf","slamsf");
        msg.addMessage("slam khobi halet chetore","sdfskjf","slam",0);
        msg.addMessage("man khobam to khobi?","slam","sdfskjf",0);
        msg.addMessage("are che khabar?","sdfskjf","slam",0);
        msg.addMessage("salamati","slam","sdfskjf",0);*/
        //msg.addMessage("salamati","sdfsksdfsjf","slamsf");
        HashMap<Integer, ArrayList> mess = new Gson().fromJson(msg.getAllChat("ddddddd1"),
                new TypeToken<HashMap<Integer, ArrayList>>() {}.getType());
        System.out.println(new TypeToken<HashMap<Integer, ArrayList>>() {}.getType());
        for (int i = 0; i < mess.size(); i++) {
            System.out.println(mess.get(i).get(0)+" : "+mess.get(i).get(1)+" : " + mess.get(i).get(2) +" : " + mess.get(i).get(3));
        }
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
            /*Thread thread = new Thread(() -> ServerSocket.start());
            thread.start();*/
            ServerSideIF chatServer = new ServerSideImp();
            Naming.rebind("Test",chatServer);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
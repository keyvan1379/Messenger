import JpaConfig.JPAUtil;
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

import java.util.Date;
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
        /*msg.addChat("sdfskjf","slam");
        msg.addChat("sdfsksdfsjf","slamsf");
        msg.addMessage("slam khobi halet chetore","sdfskjf","slam");
        msg.addMessage("man khobam to khobi?","slam","sdfskjf");
        msg.addMessage("are che khabar?","sdfskjf","slam");
        msg.addMessage("salamati","slam","sdfskjf");*/
        msg.addMessage("salamati","sdfsksdfsjf","slamsf");
    }
}
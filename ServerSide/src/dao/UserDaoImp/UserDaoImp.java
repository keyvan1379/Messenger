package dao.UserDaoImp;

import JpaConfig.JPAUtil;
import dao.UserDao;
import dao.daoExc.GetUserex;
import dao.daoExc.Passex;
import dao.daoExc.UsernameEx;
import hibernateconfig.HibernateUtil;
import models.User;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDaoImp implements UserDao {
    @Override
    public void addUser(User user) {
        EntityManager em=null;
        EntityTransaction et=null;
        try {
            em = JPAUtil.getEntitManager();
            et = em.getTransaction();
            et.begin();
            em.persist(user);
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
        }
    }

    @Override
    public void deleteUser(String username) {
        EntityManager em=null;
        EntityTransaction et=null;
        try {
            em = JPAUtil.getEntitManager();
            et = em.getTransaction();
            et.begin();
            User user = (User) em.find(User.class,username);
            em.remove(user);
            et.commit();
        }catch (Exception ex){
            et.rollback();
            ex.printStackTrace();
            System.out.println("-----------------------------------------------------" +
                    "------------------------------------------------");
            System.out.println("-----------------------------------------------------" +
                    "------------------------------------------------");
            System.out.println(ex.getCause());
        }finally {
            em.close();
        }
    }

    @Override
    public void updateUser(User user) {
        EntityManager em=null;
        EntityTransaction et=null;
        try {
            em = JPAUtil.getEntitManager();
            et = em.getTransaction();
            et.begin();
            User oldUser = (User) em.find(User.class,user.getUserName());
            oldUser = user;
            em.merge(oldUser);
            et.commit();
        }catch (Exception ex){
            et.rollback();
            ex.printStackTrace();
            System.out.println("-----------------------------------------------------" +
                    "------------------------------------------------");
            System.out.println("-----------------------------------------------------" +
                    "------------------------------------------------");
            System.out.println(ex.getCause());
        }finally {
            em.close();
        }
    }

    @Override
    public ArrayList<String> getUsers() throws Exception {
        ArrayList<String> allUser = new ArrayList<>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection
                    ("jdbc:oracle:thin:@127.0.0.1:1521:XE","ADMIN","admin");
            PreparedStatement statement = connection.prepareStatement("SELECT USERNAME from USERS");
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                allUser.add(rs.getString(1));
            }
            return allUser;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("wrong with query");
        }
    }

    @Override
    public User getUser(String username) throws GetUserex {
        EntityManager em = null;
        EntityTransaction et = null;
        User user = null;
        try {
            em = JPAUtil.getEntitManager();
            et = em.getTransaction();
            et.begin();
            user = (User) em.find(User.class, username);
            et.commit();
        } catch (Exception ex){
            et.rollback();
            ex.printStackTrace();
            System.out.println("-----------------------------------------------------" +
                    "------------------------------------------------");
            System.out.println("-----------------------------------------------------" +
                    "------------------------------------------------");
            System.out.println(ex.getCause());
        }finally {
            em.close();
            if(user==null){
                throw new GetUserex("This User does not exist");
            }
            return user;
        }
    }

    @Override
    public void login(String username, String password) {
        User user = null;
        try {
            user = getUser(username);
            if(!user.getPassWord().equals(password)){
                throw new Passex("Wrong password");
            }
            user.setLastSeen(new Date());
            user.setIsActive("-1");
            updateUser(user);
        } catch (GetUserex getUserex) {
            throw new UsernameEx("this username does not exist");
        }
    }

    @Override
    public String isActive(String username) throws GetUserex {
        User user;
        try {
            user = getUser(username);
            return user.getIsActive();
        } catch (GetUserex getUserex) {
            throw new GetUserex("this user does not exist");
        }
    }

    @Override
    public Date lastSeen(String username) throws GetUserex{
        User user;
        try {
            user = getUser(username);
            return user.getLastSeen();
        }catch (GetUserex ex){
            throw new GetUserex("this user does not exist");
        }
    }


}














/*Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(user);
        }catch (Exception ex){
            System.out.println(ex.getCause());
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }finally {
            session.getTransaction().commit();
            session.close();
        }*/
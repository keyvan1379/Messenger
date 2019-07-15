package dao.GroupDaoImp;

import JpaConfig.JPAUtil;
import dao.GroupDao;
import models.Group;
import models.GroupMessage;
import models.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Set;

public class GroupDaoImp implements GroupDao {
    @Override
    public Group getGroup(String username) throws Exception {
        EntityManager em = null;
        EntityTransaction et = null;
        Group group = null;
        try {
            em = JPAUtil.getEntitManager();
            et = em.getTransaction();
            et.begin();
            group = (Group) em.find(Group.class, username);
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
            if(group==null){
                throw new Exception("This User does not exist");
            }
            return group;
        }
    }

    @Override
    public void addGroup(Group group) {
        EntityManager em=null;
        EntityTransaction et=null;
        try {
            em = JPAUtil.getEntitManager();
            et = em.getTransaction();
            et.begin();
            em.persist(group);
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
    public void updateGroup(Group group) {
        EntityManager em=null;
        EntityTransaction et=null;
        try {
            em = JPAUtil.getEntitManager();
            et = em.getTransaction();
            et.begin();
            Group oldGroup = (Group) em.find(Group.class,group.getUserName());
            oldGroup = group;
            em.merge(oldGroup);
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
    public void deleteGroup(String username) {
        EntityManager em=null;
        EntityTransaction et=null;
        try {
            em = JPAUtil.getEntitManager();
            et = em.getTransaction();
            et.begin();
            Group group = (Group) em.find(Group.class,username);
            em.remove(group);
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
    public Set<User> getGroupUsers(String username) throws Exception {
        Group group = getGroup(username);
        return group.getUsers();
    }

    @Override
    public ArrayList<GroupMessage> getGroupMessage(String username) throws Exception {
        Group group = getGroup(username);
        return (ArrayList)group.getGroupMessages();
    }
}

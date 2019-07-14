package dao.ChannelDaoImp;

import JpaConfig.JPAUtil;
import dao.ChannelDao;
import models.Channel;
import models.ChannelMessage;
import models.Group;
import models.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Set;

public class ChannelDaoImp implements ChannelDao {
    @Override
    public Channel getChannel(String username) throws Exception {
        EntityManager em = null;
        EntityTransaction et = null;
        Channel channel = null;
        try {
            em = JPAUtil.getEntitManager();
            et = em.getTransaction();
            et.begin();
            channel = (Channel) em.find(Channel.class, username);
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
            if(channel==null){
                throw new Exception("This User does not exist");
            }
            return channel;
        }
    }

    @Override
    public void addChannel(Channel channel) {
        EntityManager em=null;
        EntityTransaction et=null;
        try {
            em = JPAUtil.getEntitManager();
            et = em.getTransaction();
            et.begin();
            em.persist(channel);
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
    public void deleteChannel(String username) {
        EntityManager em=null;
        EntityTransaction et=null;
        try {
            em = JPAUtil.getEntitManager();
            et = em.getTransaction();
            et.begin();
            Channel channel = (Channel) em.find(Channel.class,username);
            em.remove(channel);
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
    public void updateChannel(Channel channel) {
        EntityManager em=null;
        EntityTransaction et=null;
        try {
            em = JPAUtil.getEntitManager();
            et = em.getTransaction();
            et.begin();
            Channel oldChannel = (Channel) em.find(Channel.class,channel.getUsername());
            oldChannel = channel;
            em.merge(oldChannel);
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
    public Set<User> getChannelUsers(String username) throws Exception {
        Channel channel = getChannel(username);
        return channel.getUsers();
    }

    @Override
    public ArrayList<ChannelMessage> getChannelMessage(String username) throws Exception {
        Channel channel = getChannel(username);
        return (ArrayList)channel.getChannelMessages();
    }
}

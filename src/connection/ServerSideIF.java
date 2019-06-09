package connection;

import dao.daoExc.GetUserex;

import java.rmi.Remote;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface ServerSideIF extends Remote {
    void sendMsg(String username,String msg);
    ArrayList<String> getAllOnlineUser();
    String login(String username,String password);
    boolean getUser(String username) throws GetUserex;
    boolean isActive(String username) throws GetUserex;
    Date lastSeen(String username) throws GetUserex;
    HashMap<Integer, ArrayList> getAllMessages(String username1);
    HashMap<Integer, ArrayList> getMessageBetween2Person(String username1,String username2);
}

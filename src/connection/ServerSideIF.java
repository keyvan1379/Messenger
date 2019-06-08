package connection;

import dao.daoExc.GetUserex;

import java.rmi.Remote;
import java.util.ArrayList;
import java.util.Date;

public interface ServerSideIF extends Remote {
    void sendMsg(String username,String msg);
    ArrayList<String> getAllOnlineUser();
    String login(String username,String password);
    boolean getUser(String username) throws GetUserex;
    boolean isActive(String username) throws GetUserex;
    Date lastSeen(String username) throws GetUserex;
}

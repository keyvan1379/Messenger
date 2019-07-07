package connection;

import dao.daoExc.GetUserex;
import models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface ServerSideIF extends Remote {
    void sendMsg(String FromUsername,String ToUsername,String msg) throws Exception, RemoteException;
    ArrayList<String> getAllOnlineUser()throws RemoteException;
    String login(String username,String password,ClientSideIF clientSideIF) throws RemoteException;
    String signUp(User user) throws RemoteException;
    boolean getUser(String username) throws GetUserex,RemoteException;
    boolean isActive(String username) throws GetUserex,RemoteException;
    Date lastSeen(String username) throws GetUserex,RemoteException;
    String getAllMessages(String username1) throws RemoteException;
    String getMessageBetween2Person(String username1,String username2) throws Exception,RemoteException;
    void uploadFile(String fromUser,String filename,String toUser)throws RemoteException;
    void downloadFileAgain(String fromUsername, String fileName,
                           String username,ClientSideIF clientSideIF) throws RemoteException;
}

package connection;

import dao.daoExc.GetUserex;
import models.ProfileInfo;
import models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface ServerSideIF extends Remote {
    PublicKey getKey() throws RemoteException;
    void createCon(String username,String key) throws RemoteException;
    void sendMsg(String FromUsername,String ToUsername,String msg) throws Exception, RemoteException;
    String getAllUser()throws RemoteException;
    String login(String username,String password,ClientSideIF clientSideIF) throws RemoteException;
    String signUp(User user) throws RemoteException;
    String editProfile(String username,ClientSideIF clientSideIF,User user)throws RemoteException;
    String deleteProfile(String username,ClientSideIF clientSideIF)throws RemoteException;
    boolean getUser(String username) throws GetUserex,RemoteException;
    ProfileInfo getUserInfo(String username) throws GetUserex,RemoteException;
    String isActive(String sourceUser,String username) throws GetUserex,RemoteException;
    void setStatus(String username,String status) throws RemoteException, GetUserex;
    Date lastSeen(String username) throws GetUserex,RemoteException;
    String getAllMessages(String username1) throws RemoteException;
    String getMessageBetween2Person(String username1,String username2) throws Exception,RemoteException;
    void uploadFile(String fromUser,String filename,String toUser)throws RemoteException;
    void downloadFileAgain(String fromUsername, String fileName,
                           String username,ClientSideIF clientSideIF,String path) throws RemoteException;
}

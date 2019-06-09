package connection.ServerSideImp;

import connection.ClientSideIF;
import connection.ServerSideIF;
import dao.MessageQuery;
import dao.MessageQueryImp.MessageQueryImp;
import dao.UserDao;
import dao.UserDaoImp.UserDaoImp;
import dao.daoExc.GetUserex;
import dao.daoExc.Passex;
import dao.daoExc.UsernameEx;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ServerSideImp extends UnicastRemoteObject implements ServerSideIF {
    public ServerSideImp() throws RemoteException {
    }

    private UserDao userDao = new UserDaoImp();
    private MessageQuery messageQuery = new MessageQueryImp();
    private HashMap<String, ClientSideIF> clients = new HashMap<>();

    @Override
    public void sendMsg(String FromUsername,String ToUsername, String msg) throws Exception {
        if(!messageQuery.isChatExist(FromUsername,ToUsername)) messageQuery.addChat(FromUsername,ToUsername);
        try {//need some security
            if(clients.get(ToUsername)==null){
                try {
                    userDao.getUser(ToUsername);
                    messageQuery.addMessage(msg,FromUsername,ToUsername);
                }catch (GetUserex ex){
                    System.out.println(ex.getMessage());
                    return;
                }
            }
            else {
                try {
                    userDao.getUser(ToUsername);
                    clients.get(ToUsername).getMessage(msg);
                    messageQuery.addMessage(msg,FromUsername,ToUsername);
                }catch (GetUserex ex){
                    System.out.println(ex.getMessage());
                    return;
                }
            }

            /*this block for send msg to database*/
        }catch (NullPointerException ex){
            /*

           this block is for check if username does exist then send msg
             */
            ex.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> getAllOnlineUser() {
        return null;
    }

    @Override
    public String login(String username, String password,ClientSideIF clientSideIF) {
        try {
            userDao.login(username,password);
            clients.put(username,clientSideIF);
            return "wait for server";
        }catch (UsernameEx ex){
            return ex.getMessage();
        }catch (Passex ex){
            return ex.getMessage();
        }
    }

    @Override
    public boolean getUser(String username) throws GetUserex {
        return false;
    }

    @Override
    public boolean isActive(String username) throws GetUserex {
        return userDao.isActive(username);
    }

    @Override
    public Date lastSeen(String username) throws GetUserex {
        return userDao.lastSeen(username);
    }

    @Override
    public HashMap<Integer, ArrayList> getAllMessages(String username1) {
        return messageQuery.getAllChat(username1);
    }

    @Override
    public HashMap<Integer, ArrayList> getMessageBetween2Person(String username1, String username2) throws Exception {
        if(messageQuery.isChatExist(username1,username2))
            return messageQuery.getChatBetweenTwoPerson(username1,username2);
        else
            throw new IllegalArgumentException("chat dose not exist between this 2 username");
    }
}

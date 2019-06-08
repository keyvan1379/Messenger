package connection.ServerSideImp;

import connection.ServerSideIF;
import dao.UserDao;
import dao.UserDaoImp.UserDaoImp;
import dao.daoExc.GetUserex;
import dao.daoExc.Passex;
import dao.daoExc.UsernameEx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ServerSideImp implements ServerSideIF {
    private UserDao userDao = new UserDaoImp();
    private HashMap<String,String> clients = new HashMap<>(); //this is wrong bcs HashMap value must be consist of clientif not String but we are in dev
    @Override
    public void sendMsg(String username, String msg) {
        try {
            clients.get(username);   // first we need to Implement clients interface then we send msg :))))
            /*



            this block for send msg to database*/
        }catch (NullPointerException ex){
            /*


           this block is for check if username does exist then send msg
             */
        }
    }

    @Override
    public ArrayList<String> getAllOnlineUser() {
        return null;
    }

    @Override
    public String login(String username, String password) {
        try {
            userDao.login(username,password);
            clients.put(username,password);
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
}

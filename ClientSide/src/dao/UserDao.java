package dao;

import dao.daoExc.GetUserex;
import models.User;

import java.util.ArrayList;
import java.util.Date;

public interface UserDao {
    void addUser(User user);
    void deleteUser(String user);
    void updateUser(User user);
    void login(String username, String password);
    String isActive(String username) throws GetUserex;
    Date lastSeen(String username) throws GetUserex;
    ArrayList<String> getUsers() throws Exception;
    User getUser(String username) throws GetUserex;
    ArrayList<String> getChatUsersList(String username) throws Exception;
}

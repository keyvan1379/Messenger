package dao;

import dao.daoExc.GetUserex;
import models.User;

import java.util.ArrayList;
import java.util.Date;

public interface UserDao {
    void addUser(User user);
    void deleteUser(String user);
    void updateUser(User user);
    void login(String username,String password);
    boolean isActive(String username) throws GetUserex;
    Date lastSeen(String username) throws GetUserex;
    ArrayList<User> getUsers();
    User getUser(String username) throws GetUserex;
}
package dao;

import java.util.ArrayList;
import java.util.HashMap;

public interface MessageQuery {
    void createTable();
    void addChat(String username1, String username2);
    boolean isChatExist(String username1, String username2) throws Exception;
    String getAllChat(String username1);
    String getChatBetweenTwoPerson(String username1, String username2);
    void addMessage(String message, String fromUsername, String toUsername, int isFile);
    void updateUser(String OldUserName, String newUserName);
    //some function to get message
}

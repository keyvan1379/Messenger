package dao;

import java.util.ArrayList;
import java.util.HashMap;

public interface MessageQuery {
    void createTable();
    void addChat(String username1,String username2);
    boolean isChatExist(String username1,String username2) throws Exception;
    HashMap<Integer, ArrayList> getAllChat(String username1);
    HashMap<Integer, ArrayList> getChatBetweenTwoPerson(String username1, String username2);
    void addMessage(String message,String fromUsername,String toUsername);
    //some function to get message
}

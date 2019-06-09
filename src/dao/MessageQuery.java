package dao;

import java.util.ArrayList;
import java.util.HashMap;

public interface MessageQuery {
    void createTable();
    void addChat(String username1,String username2);
    void getAllChat(String username1);
    HashMap<Integer, ArrayList> getCharBetweenTwoPerson(String username1, String username2);
    void addMessage(String message,String fromUsername,String toUsername);
    //some function to get message
}

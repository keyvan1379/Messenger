package dao;

public interface MessageQuery {
    void createTable();
    void addChat(String username1,String username2);
    void getChat(String username1,String username2);
    void addMessage(String message,String fromUsername,String toUsername);
    //some function to get message
}

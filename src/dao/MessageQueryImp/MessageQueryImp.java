package dao.MessageQueryImp;

import dao.MessageQuery;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MessageQueryImp implements MessageQuery {
    @Override
    public void createTable() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection
                    ("jdbc:oracle:thin:@127.0.0.1:1521:XE","ADMIN","admin");

            PreparedStatement statement = connection.prepareStatement("CREATE TABLE Conversation(" +
                    "C_ID int NOT NULL PRIMARY KEY," +
                    "USER1 varchar(255) NOT NULL, " +
                    "USER2 varchar(255) NOT NULL," +
                    "CREATED_TIME DATE " +
                    ")"+
                    "");
            System.out.println(statement.execute());
            PreparedStatement statement1 = connection.prepareStatement("CREATE TABLE Message(" +
                    "M_ID int NOT NULL PRIMARY KEY," +
                    "MESSAGE ClOB NOT NULL, " +
                    "USER1 varchar(255)," +
                    "C_ID int NOT NULL," +
                    "SEND_TIME DATE," +
                    "FOREIGN KEY (C_ID)" +
                    "REFERENCES Conversation(C_ID)" +
                    ")");
            System.out.println(statement1.execute());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addChat(String username1, String username2) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection
                    ("jdbc:oracle:thin:@127.0.0.1:1521:XE","ADMIN","admin");
            PreparedStatement statement = connection.prepareStatement("INSERT INTO CONVERSATION (USER1, USER2, CREATED_TIME) " +
                    "values (?,?,?)");
            statement.setString(1,username1);
            statement.setString(2,username2);
            Date date = new Date();
            statement.setDate(3,new java.sql.Date(date.getTime()));
            System.out.println(statement.execute());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<Integer, ArrayList> getAllChat(String username1) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection
                    ("jdbc:oracle:thin:@127.0.0.1:1521:XE","ADMIN","admin");
            PreparedStatement statement = connection.prepareStatement("(SELECT MESSAGE.M_ID,MESSAGE.USER1 As" +
                    "    FromUser,MESSAGE.MESSAGE,? AS" +
                    "    toUser FROM MESSAGE WHERE MESSAGE.C_ID IN (" +
                    "    SELECT C_ID FROM CONVERSATION" +
                    "    WHERE (USER1 = ? Or USER2=?)" +
                    ") AND NOT MESSAGE.USER1=?" +
                    " UNION ALL" +
                    " SELECT MESSAGE.M_ID,MESSAGE.USER1 AS User1,MESSAGE.MESSAGE,CASE" +
                    "        WHEN CONVERSATION.USER1=? THEN CONVERSATION.USER2" +
                    "        WHEN CONVERSATION.USER2=? THEN CONVERSATION.USER1" +
                    "     END AS ToUser FROM MESSAGE" +
                    "        INNER JOIN CONVERSATION ON Message.C_ID=CONVERSATION.C_ID" +
                    " WHERE MESSAGE.USER1=?) ORDER BY M_ID");
            statement.setString(1,username1);
            statement.setString(2,username1);
            statement.setString(3,username1);
            statement.setString(4,username1);
            statement.setString(5,username1);
            statement.setString(6,username1);
            statement.setString(7,username1);
            ResultSet rs = statement.executeQuery();
            HashMap<Integer,ArrayList> allMessages= new HashMap<>();
            int i=0;
            while (rs.next()){
                allMessages.put(i,new ArrayList<>());
                allMessages.get(i).add(rs.getString(2));
                allMessages.get(i).add(rs.getString(3));
                allMessages.get(i++).add(rs.getString(4));
            }
            return allMessages;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public HashMap<Integer, ArrayList> getChatBetweenTwoPerson(String username1, String username2) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection
                    ("jdbc:oracle:thin:@127.0.0.1:1521:XE","ADMIN","admin");
            PreparedStatement statement = connection.prepareStatement("(SELECT MESSAGE.M_ID,MESSAGE.USER1 As" +
                    "    FromUser,MESSAGE.MESSAGE,? AS" +
                    "    toUser FROM MESSAGE WHERE MESSAGE.C_ID IN (" +
                    "    SELECT C_ID FROM CONVERSATION" +
                    "    WHERE ((USER1 = ? AND USER2 = ?)Or" +
                    "           (USER2 = ? AND CONVERSATION.USER1= ?))" +
                    ") AND NOT MESSAGE.USER1=?" +
                    " UNION ALL" +
                    " SELECT MESSAGE.M_ID,MESSAGE.USER1 AS User1,MESSAGE.MESSAGE,CASE" +
                    "        WHEN (CONVERSATION.USER1=?" +
                    "          )THEN CONVERSATION.USER2" +
                    "        WHEN (CONVERSATION.USER2=?" +
                    "          )THEN CONVERSATION.USER1" +
                    "     END AS ToUser FROM MESSAGE" +
                    "        INNER JOIN CONVERSATION ON Message.C_ID=CONVERSATION.C_ID" +
                    " WHERE MESSAGE.USER1=? AND MESSAGE.C_ID IN (" +
                    "     SELECT C_ID FROM CONVERSATION" +
                    "     WHERE ((USER1 = ? AND USER2 = ?)Or" +
                    "            (USER2 = ? AND CONVERSATION.USER1= ?))" +
                    "     )) ORDER BY M_ID");
            statement.setString(1,username1);
            statement.setString(2,username1);
            statement.setString(3,username2);
            statement.setString(4,username1);
            statement.setString(5,username2);
            statement.setString(6,username1);
            statement.setString(7,username1);
            statement.setString(8,username1);
            statement.setString(9,username1);
            statement.setString(10,username1);
            statement.setString(11,username2);
            statement.setString(12,username1);
            statement.setString(13,username2);
            ResultSet rs = statement.executeQuery();
            HashMap<Integer,ArrayList> messages= new HashMap<>();
            int i=0;
            while (rs.next()){
                messages.put(i,new ArrayList<>());
                messages.get(i).add(rs.getString(2));
                messages.get(i++).add(rs.getString(3));
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addMessage(String message, String fromUsername, String toUsername) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection
                    ("jdbc:oracle:thin:@127.0.0.1:1521:XE","ADMIN","admin");
            PreparedStatement statement = connection.prepareStatement("INSERT INTO MESSAGE (MESSAGE, USER1, C_ID, SEND_TIME) " +
                    "values (? ,?," +
                    "(SELECT C_ID FROM Conversation WHERE ((USER1 IN (?,?)) AND (USER2 IN (?,?))))," +
                    "?)");
            statement.setString(1,message);
            statement.setString(2,fromUsername);
            statement.setString(3,toUsername);
            statement.setString(4,fromUsername);
            statement.setString(5,toUsername);
            statement.setString(6,fromUsername);
            Date date = new Date();
            statement.setDate(7,new java.sql.Date(date.getTime()));
            System.out.println(statement.execute());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


    /*CREATE SEQUENCE conver_seq;*/
    /*CREATE OR REPLACE TRIGGER conver_tig
        BEFORE INSERT ON Conversation
        FOR EACH ROW
        BEGIN
        SELECT conver_seq.nextval
        INTO :new.C_ID
        FROM dual;
        END;*/

/*CREATE SEQUENCE mess_seq;*/
    /*CREATE OR REPLACE TRIGGER mess_tig
        BEFORE INSERT ON Message
        FOR EACH ROW
        BEGIN
        SELECT mess_seq.nextval
        INTO :new.M_ID
        FROM dual;
        END;*/


    /*System.out.println("querying SELECT * FROM XXX");
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " ");
                }
                System.out.println("");
            }*/
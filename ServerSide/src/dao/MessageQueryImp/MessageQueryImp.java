package dao.MessageQueryImp;

import com.google.gson.Gson;
import dao.MessageQuery;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

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
                    ("jdbc:oracle:thin:@127.0.0.1:1521:XE", "ADMIN", "admin");

            PreparedStatement statement = connection.prepareStatement("CREATE TABLE Conversation(" +
                    "C_ID int NOT NULL PRIMARY KEY," +
                    "USER1 varchar(255) NOT NULL, " +
                    "USER2 varchar(255) NOT NULL," +
                    "CREATED_TIME varchar(255)" +
                    ")" +
                    "");
            System.out.println(statement.execute());
            PreparedStatement statement1 = connection.prepareStatement("CREATE TABLE Message(" +
                    "M_ID int NOT NULL PRIMARY KEY," +
                    "MESSAGE ClOB NOT NULL, " +
                    "USER1 varchar(255)," +
                    "C_ID int NOT NULL," +
                    "IS_FILE int," +
                    "SEND_TIME varchar(255)," +
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
            try {
                if (isChatExist(username1, username2)) return;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Connection connection = DriverManager.getConnection
                    ("jdbc:oracle:thin:@127.0.0.1:1521:XE", "ADMIN", "admin");
            PreparedStatement statement = connection.prepareStatement("INSERT INTO CONVERSATION (USER1, USER2, CREATED_TIME) " +
                    "values (?,?,?)");
            statement.setString(1, username1);
            statement.setString(2, username2);
            java.util.Date dt = new java.util.Date();

            java.text.SimpleDateFormat sdf =
                    new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String currentTime = sdf.format(dt);
            statement.setString(3, currentTime);
            System.out.println(statement.execute());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isChatExist(String username1, String username2) throws Exception {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection
                    ("jdbc:oracle:thin:@127.0.0.1:1521:XE", "ADMIN", "admin");
            PreparedStatement statement = connection.prepareStatement("SELECT USER1,USER2 FROM CONVERSATION" +
                    "  WHERE (USER2 = ? AND USER1 = ?) OR" +
                    "    (USER1 = ? AND USER2 = ?)");
            statement.setString(1, username1);
            statement.setString(2, username2);
            statement.setString(3, username1);
            statement.setString(4, username2);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new Exception("some error in database");
        }
    }

    @Override
    public String getAllChat(String username1) {
        String separator = "\\";
        String[] replace;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection
                    ("jdbc:oracle:thin:@127.0.0.1:1521:XE", "ADMIN", "admin");
            PreparedStatement statement = connection.prepareStatement("(SELECT MESSAGE.M_ID,MESSAGE.USER1 As" +
                    "    FromUser,MESSAGE.MESSAGE,MESSAGE.IS_FILE,? AS" +
                    "    toUser FROM MESSAGE WHERE MESSAGE.C_ID IN (" +
                    "    SELECT C_ID FROM CONVERSATION" +
                    "    WHERE (USER1 = ? Or USER2=?)" +
                    ") AND NOT MESSAGE.USER1=?" +
                    " UNION ALL" +
                    " SELECT MESSAGE.M_ID,MESSAGE.USER1 AS User1,MESSAGE.MESSAGE,MESSAGE.IS_FILE,CASE" +
                    "        WHEN CONVERSATION.USER1=? THEN CONVERSATION.USER2" +
                    "        WHEN CONVERSATION.USER2=? THEN CONVERSATION.USER1" +
                    "     END AS ToUser FROM MESSAGE" +
                    "        INNER JOIN CONVERSATION ON Message.C_ID=CONVERSATION.C_ID" +
                    " WHERE MESSAGE.USER1=?) ORDER BY M_ID");
            statement.setString(1, username1);
            statement.setString(2, username1);
            statement.setString(3, username1);
            statement.setString(4, username1);
            statement.setString(5, username1);
            statement.setString(6, username1);
            statement.setString(7, username1);
            ResultSet rs = statement.executeQuery();
            HashMap<Integer, ArrayList> allMessages = new HashMap<>();
            int i = 0;
            while (rs.next()) {
                allMessages.put(i, new ArrayList<>());
                allMessages.get(i).add(rs.getString(2));
                if (Integer.parseInt(rs.getString(4)) == 1) {
                    replace = rs.getString(3).replaceAll(Pattern.quote(separator), "\\\\")
                            .split("\\\\");
                    allMessages.get(i).add(replace[replace.length - 1].substring(16));
                } else {
                    allMessages.get(i).add(rs.getString(3));
                }
                allMessages.get(i).add(rs.getString(4));
                allMessages.get(i++).add(rs.getString(5));
            }
            Gson gson = new Gson();
            String json = gson.toJson(allMessages);
            return json;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getChatBetweenTwoPerson(String username1, String username2) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection
                    ("jdbc:oracle:thin:@127.0.0.1:1521:XE", "ADMIN", "admin");
            PreparedStatement statement = connection.prepareStatement("(SELECT MESSAGE.M_ID,MESSAGE.USER1 As" +
                    "    FromUser,MESSAGE.MESSAGE,MESSAGE.IS_FILE,SEND_TIME,? AS" +
                    "    toUser FROM MESSAGE WHERE MESSAGE.C_ID IN (" +
                    "    SELECT C_ID FROM CONVERSATION" +
                    "    WHERE ((USER1 = ? AND USER2 = ?)Or" +
                    "           (USER2 = ? AND CONVERSATION.USER1= ?))" +
                    ") AND NOT MESSAGE.USER1=?" +
                    " UNION ALL" +
                    " SELECT MESSAGE.M_ID,MESSAGE.USER1 AS User1,MESSAGE.MESSAGE,MESSAGE.IS_FILE,SEND_TIME," +
                    "CASE" +
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
            statement.setString(1, username1);
            statement.setString(2, username1);
            statement.setString(3, username2);
            statement.setString(4, username1);
            statement.setString(5, username2);
            statement.setString(6, username1);
            statement.setString(7, username1);
            statement.setString(8, username1);
            statement.setString(9, username1);
            statement.setString(10, username1);
            statement.setString(11, username2);
            statement.setString(12, username1);
            statement.setString(13, username2);
            ResultSet rs = statement.executeQuery();
            HashMap<Integer, ArrayList> messages = new HashMap<>();
            int i = 0;
            while (rs.next()) {
                messages.put(i, new ArrayList<>());
                messages.get(i).add(rs.getString(2));
                if(Integer.parseInt(rs.getString(4))!=0){
                    messages.get(i).add(rs.getString(3).split("\\\\")
                            [rs.getString(3).split("\\\\").length-1]);
                }else
                    messages.get(i).add(rs.getString(3));
                messages.get(i).add(rs.getString(4));
                messages.get(i++).add(rs.getString(5));
            }
            Gson gson = new Gson();
            String json = gson.toJson(messages);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addMessage(String message, String fromUsername, String toUsername, int isFile) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection
                    ("jdbc:oracle:thin:@127.0.0.1:1521:XE", "ADMIN", "admin");
            PreparedStatement statement = connection.prepareStatement("INSERT INTO MESSAGE (MESSAGE, USER1, C_ID, SEND_TIME,IS_FILE) " +
                    "values (? ,?," +
                    "(SELECT C_ID FROM Conversation WHERE ((USER1 IN (?,?)) AND (USER2 IN (?,?))))," +
                    "?,?)");
            statement.setString(1, message);
            statement.setString(2, fromUsername);
            statement.setString(3, toUsername);
            statement.setString(4, fromUsername);
            statement.setString(5, toUsername);
            statement.setString(6, fromUsername);
            java.util.Date dt = new java.util.Date();

            java.text.SimpleDateFormat sdf =
                    new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String currentTime = sdf.format(dt);
            statement.setString(7, currentTime);
            statement.setInt(8, isFile);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(String oldUserName, String newUserName) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection
                    ("jdbc:oracle:thin:@127.0.0.1:1521:XE", "ADMIN", "admin");
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE CONVERSATION set USER2 = ? WHERE USER2 = ?");
            preparedStatement.setString(1,newUserName);
            preparedStatement.setString(2,oldUserName);
            PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE CONVERSATION set USER1 = ? WHERE USER1 = ?");
            preparedStatement1.setString(1,newUserName);
            preparedStatement1.setString(2,oldUserName);
            PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE MESSAGE set USER1= ? WHERE USER1=?");
            preparedStatement2.setString(1,newUserName);
            preparedStatement2.setString(2,oldUserName);
            PreparedStatement preparedStatement3 = connection.prepareStatement("commit");
            preparedStatement.execute();
            preparedStatement1.execute();
            preparedStatement2.execute();
            preparedStatement3.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMessage(String username) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection
                    ("jdbc:oracle:thin:@127.0.0.1:1521:XE", "ADMIN", "admin");
            PreparedStatement statement2 = connection.prepareStatement("DELETE FROM MESSAGE WHERE C_ID IN (SELECT C_ID FROM " +
                    "CONVERSATION WHERE (CONVERSATION.USER1 = ? OR CONVERSATION.USER2 = ?))");
            statement2.setString(1,username);
            statement2.setString(2,username);
            statement2.execute();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM CONVERSATION WHERE USER1 = ?");
            statement.setString(1,username);
            statement.execute();
            PreparedStatement statement1 = connection.prepareStatement("DELETE FROM CONVERSATION WHERE USER2 = ?");
            statement1.setString(1,username);
            statement1.execute();
            PreparedStatement statement4 = connection.prepareStatement("COMMIT");
            statement4.execute();
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
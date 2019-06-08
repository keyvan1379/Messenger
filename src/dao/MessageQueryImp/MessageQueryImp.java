package dao.MessageQueryImp;

import dao.MessageQuery;

import java.sql.*;
import java.util.Date;

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
    public void getChat(String username1, String username2) {

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
package ui.controller;

import java.util.Date;

public class Message
{
    String user;
    String message;
    String time;
    long isFile;
    public Message(String user, String message, long isFile, String time)
    {
        this.user = user;
        this.message = message;
        this.isFile = isFile;
        this.time = time;
    }

    public static String dateToString(Date date){
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public long getIsFile() {
        return isFile;
    }

    public String getTime() {
        return time;
    }
}

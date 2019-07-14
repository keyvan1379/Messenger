package models;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "GROUP_MESSAGE")
public class GroupMessage implements Serializable {

    @Id
    @Column(name = "MESSAGE_ID")
    @GeneratedValue
    private long id;

    @Column(name = "FROM_USER")
    private String fromUser;

    @Column(name = "MSG")
    private String message;

    @Column(name = "DATE_OF_MSG")
    private Date sendDate;

    public GroupMessage(String fromUser, String message, Date sendDate) {
        this.fromUser = fromUser;
        this.message = message;
        this.sendDate = sendDate;
    }

    public GroupMessage(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
}

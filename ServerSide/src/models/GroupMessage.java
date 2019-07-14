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
}

package models;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "GroupMessage")
public class GroupMessage implements Serializable {

    @Id
    @Column(name = "Id")
    @GeneratedValue
    private long id;

    @Column(name = "FROM_USER")
    private String fromUser;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "DATE")
    private Date sendDate;

    public GroupMessage(String fromUser, String message, Date sendDate) {
        this.fromUser = fromUser;
        this.message = message;
        this.sendDate = sendDate;
    }

    public GroupMessage(){}
}

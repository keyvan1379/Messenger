package models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CHANNEL_MESSAGE")
public class ChannelMessage implements Serializable {
    public ChannelMessage() {
    }

    public ChannelMessage(String admin, String msg, Date date) {
        this.admin = admin;
        this.msg = msg;
        this.date = date;
    }

    @Id
    @Column(name = "CHANNEL_MESSAGE_ID")
    @GeneratedValue
    private long id;

    @Column(name = "CHANNEL_ADMIN")
    private String admin;

    @Column(name = "MSG")
    private String msg;

    @Column(name = "MSG_DATE")
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

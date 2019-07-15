package models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "CHANNELS")
public class Channel implements Serializable {

    @Id
    @Column(name = "USERNAME")
    private String username;

    @Column(name = "CHANNEL_ADMIN")
    private String admin;

    @Column(name = "BIO")
    private String bio;

    @Column(name = "CREATE_TIME")
    private Date date;

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "channels")
    private Set<User> users = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "USERNAME")
    private List<ChannelMessage> channelMessages = new ArrayList<>();

    public Channel(String username, String admin, String bio, Date date) {
        this.username = username;
        this.admin = admin;
        this.bio = bio;
        this.date = date;
    }
    public Channel(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public List<ChannelMessage> getChannelMessages() {
        return channelMessages;
    }

    public void setChannelMessages(List<ChannelMessage> channelMessages) {
        this.channelMessages = channelMessages;
    }
}

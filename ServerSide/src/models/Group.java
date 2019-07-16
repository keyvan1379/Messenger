package models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "GROUPS")
public class Group implements Serializable {

    @Id
    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADMIN")
    private String admin;

    @Column(name = "BIO")
    private String bio;

    @Column(name = "CREATETIME")
    private Date createTime;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "GROUPS_USERS",joinColumns = @JoinColumn(name = "USER_NAME"),inverseJoinColumns = @JoinColumn(name = "GROUP_USERNAME"))
    private Set<User> users = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "USERNAME")
    private List<GroupMessage> groupMessages = new ArrayList<>();

    public Group(){
    }

    public Group(String userName, String name, String admin, String bio, Date createTime) {
        this.userName = userName;
        this.name = name;
        this.admin = admin;
        this.bio = bio;
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public List<GroupMessage> getGroupMessages() {
        return groupMessages;
    }

    public void setGroupMessages(List<GroupMessage> groupMessages) {
        this.groupMessages = groupMessages;
    }

}

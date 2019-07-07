package models;

import com.sun.javafx.beans.IDProperty;
import protections.MD5;

import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Date;
import java.util.InputMismatchException;
@Entity
@Table(name = "USERS")
public class User implements Serializable {

    @Id
    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "FIRST_NAME")
    private String fistName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String passWord;

    @Column(name = "JOIN_TIME")
    private Date joinTime;

    @Column(name = "LAST_SEEN")
    private Date lastSeen;

    @Column(name = "STATUS")
    private boolean isActive = false;

    @Transient
    private BufferedImage profileImage;

    public User(String fistName, String lastName, String email,
                String userName, String passWord,Date joinTime,Date lastSeen) {
        setFistName(fistName);
        setLastName(lastName);
        setEmail(email);
        setUserName(userName);
        setPassWord(MD5.getMd5(passWord));
        setJoinTime(joinTime);
        setLastSeen(lastSeen);
        //setProfileImage(profileImage);
    }

    public User() {
    }

    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        /*if(!email.matches("[a-zA-z]{6,}[@][a-zA-z]+[.][a-zA-z]+")){
            throw new InputMismatchException();
        }*/
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPassWord() {
        return passWord;
    }

    public BufferedImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(BufferedImage profileImage) {
        this.profileImage = profileImage;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}



//@Id
//@GeneratedValue(strategy = GenerationType.AUTO)
    /*@Column(name = "ID",nullable = false,updatable = false)
    private Long userID;*/
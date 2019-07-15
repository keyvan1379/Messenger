package models;

import java.io.Serializable;

public class ProfileInfo implements Serializable {
    private String username;
    private String firstname;
    private String lastname;
    private byte[] profile;


    public ProfileInfo(String username, String firstname, String lastname, byte[] profile) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public byte[] getProfile() {
        return profile;
    }

    public void setProfile(byte[] profile) {
        this.profile = profile;
    }
}

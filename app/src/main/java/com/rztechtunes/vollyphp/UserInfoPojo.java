package com.rztechtunes.vollyphp;

public class UserInfoPojo {
    String id;
    String email;
    String pass;

    public UserInfoPojo(String id, String email, String pass) {
        this.id = id;
        this.email = email;
        this.pass = pass;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
}

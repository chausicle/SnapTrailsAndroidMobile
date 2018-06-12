package com.justinchau.snaptrailsandroidmobile;

public class User {

    private String username;
    private String user_image;
    private String email;

    public User(String username, String user_image) {
        this.username = username;
        this.user_image = user_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImage() {
        return user_image;
    }

    public void setUserImage(String user_image) {
        this.user_image = user_image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


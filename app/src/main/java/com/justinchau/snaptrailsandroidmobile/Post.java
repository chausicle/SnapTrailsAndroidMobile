package com.justinchau.snaptrailsandroidmobile;

public class Post {
    private String location;
    private String description;
    private String image_url;
    private String created_at;
    private User user;

    public Post(
            String location,
            String description,
            String image_url,
            String created_at,
            User user) {
        this.location = location;
        this.description = description;
        this.image_url = image_url;
        this.created_at = created_at;
        this.user = user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}



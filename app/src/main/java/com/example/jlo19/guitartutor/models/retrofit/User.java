package com.example.jlo19.guitartutor.models.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Object that stores details of a User
 */
public class User {
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("level")
    private int level;
    @SerializedName("achievements")
    private int achievements;

    public User(String name, String email, int level, int achievements) {
        this.name = name;
        this.email = email;
        this.level = level;
        this.achievements = achievements;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getLevel() {
        return level;
    }

    public int getAchievements() {
        return achievements;
    }
}

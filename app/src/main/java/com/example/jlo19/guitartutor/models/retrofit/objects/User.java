package com.example.jlo19.guitartutor.models.retrofit.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Object that stores details of a User
 */
public class User implements Parcelable {
    @SerializedName("USER_ID")
    private int id;
    @SerializedName("NAME")
    private final String name;
    @SerializedName("EMAIL")
    private final String email;
    @SerializedName("LEVEL")
    private final int level;
    @SerializedName("ACHIEVEMENTS")
    private final int achievements;
    @SerializedName("API_KEY")
    private String apiKey;

    public User(String name, String email, int level, int achievements) {
        this.name = name;
        this.email = email;
        this.level = level;
        this.achievements = achievements;
    }

    public User(int id, String name, String email, int level, int achievements, String apiKey) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.level = level;
        this.achievements = achievements;
        this.apiKey = apiKey;
    }

    private User(Parcel in) {
        name = in.readString();
        email = in.readString();
        level = in.readInt();
        achievements = in.readInt();
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

    public int getId() {
        return id;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeInt(level);
        dest.writeInt(achievements);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

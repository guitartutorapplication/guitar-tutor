package com.example.jlo19.guitartutor.models.retrofit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Object that stores details of a User
 */
public class User implements Parcelable {
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

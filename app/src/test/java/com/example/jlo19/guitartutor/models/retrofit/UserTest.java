package com.example.jlo19.guitartutor.models.retrofit;

import android.os.Parcel;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

/**
 * Testing User
 */
public class UserTest {

    private User user;
    private String name;
    private String email;
    private int level;
    private int achievements;

    @Before
    public void setUp() {
        name = "Kate";
        email = "katesmith@gmail.com";
        level = 2;
        achievements = 400;
        user = new User(name, email, level, achievements);
    }

    @Test
    public void getName_ReturnsName() {
        // act
        String actualName = user.getName();

        // assert
        Assert.assertEquals(name, actualName);
    }

    @Test
    public void getEmail_ReturnsEmail() {
        // act
        String actualEmail = user.getEmail();

        // assert
        Assert.assertEquals(email, actualEmail);
    }

    @Test
    public void getLevel_ReturnsLevel() {
        // act
        int actualLevel = user.getLevel();

        // assert
        Assert.assertEquals(level, actualLevel);
    }

    @Test
    public void getAchievements_ReturnsAchievements() {
        // act
        int actualAchievements = user.getAchievements();

        // assert
        Assert.assertEquals(achievements, actualAchievements);
    }


    @Test
    public void describeContents_ReturnsZero() {
        // act
        int actual = user.describeContents();

        // assert
        Assert.assertEquals(0, actual);
    }

    @Test
    public void writeToParcel_WritesFieldsToParcel() {
        // arrange
        Parcel parcel = PowerMockito.mock(Parcel.class);

        // act
        user.writeToParcel(parcel, 0);

        // assert
        Mockito.verify(parcel).writeString(name);
        Mockito.verify(parcel).writeString(email);
        Mockito.verify(parcel).writeInt(level);
        Mockito.verify(parcel).writeInt(achievements);
    }

    @Test
    public void newArray_ReturnsArrayOfUsersWithSpecifiedSize() {
        // arrange
        int expected = 1;

        // act
        User[] users = User.CREATOR.newArray(expected);

        // assert
        Assert.assertEquals(expected, users.length);
    }
}

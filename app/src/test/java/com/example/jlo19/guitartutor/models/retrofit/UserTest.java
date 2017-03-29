package com.example.jlo19.guitartutor.models.retrofit;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

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
}

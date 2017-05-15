package com.example.jlo19.guitartutor.application;

import android.app.Application;
import android.preference.PreferenceManager;
import android.support.annotation.VisibleForTesting;

import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.components.DaggerAppComponent;
import com.example.jlo19.guitartutor.modules.AppModule;

/**
 * Extension of Application class to provide Dagger components and set up logged in user
 */
public class App extends Application {

    private static AppComponent component;
    private LoggedInUser loggedInUser;

    @Override public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        loggedInUser = new LoggedInUser(PreferenceManager.getDefaultSharedPreferences(this));
    }

    public static AppComponent getComponent() {
        return component;
    }

    public LoggedInUser getLoggedInUser() {
        return loggedInUser;
    }

    @VisibleForTesting
    public void setComponent(AppComponent component) {
        App.component = component;
    }
}

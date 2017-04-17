package com.example.jlo19.guitartutor.application;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.components.DaggerAppComponent;
import com.example.jlo19.guitartutor.modules.AppModule;

/**
 * Extension of Application class to provide Dagger components
 */

public class App extends Application {

    private static AppComponent component;

    @Override public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getComponent() {
        return component;
    }

    @VisibleForTesting
    public void setComponent(AppComponent component) {
        App.component = component;
    }
}

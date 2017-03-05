package com.example.jlo19.guitartutor.components;

import com.example.jlo19.guitartutor.activities.AllChordsActivity;
import com.example.jlo19.guitartutor.activities.ChordActivity;
import com.example.jlo19.guitartutor.modules.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * The bridge between the injection of dependencies and the AppModule
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(AllChordsActivity activity);
    void inject(ChordActivity activity);
}

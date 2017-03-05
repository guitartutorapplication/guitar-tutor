package com.example.jlo19.guitartutor.modules;

import com.example.jlo19.guitartutor.presenters.AllChordsPresenter;
import com.example.jlo19.guitartutor.presenters.ChordPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides dependencies
 */

@Module
public class AppModule {
    @Provides
    @Singleton
    AllChordsPresenter provideAllChordsPresenter() {
        return new AllChordsPresenter();
    }

    @Provides
    @Singleton
    ChordPresenter provideChordPresenter() {
        return new ChordPresenter();
    }
}

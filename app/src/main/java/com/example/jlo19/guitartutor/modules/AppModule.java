package com.example.jlo19.guitartutor.modules;

import com.example.jlo19.guitartutor.models.LearnChordModel;
import com.example.jlo19.guitartutor.models.LearnViewAllChordsModel;
import com.example.jlo19.guitartutor.models.PractiseModel;
import com.example.jlo19.guitartutor.models.PractiseSetupModel;
import com.example.jlo19.guitartutor.models.interfaces.ILearnChordModel;
import com.example.jlo19.guitartutor.models.interfaces.ILearnViewAllChordsModel;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseModel;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseSetupModel;
import com.example.jlo19.guitartutor.presenters.LearnChordPresenter;
import com.example.jlo19.guitartutor.presenters.LearnViewAllChordsPresenter;
import com.example.jlo19.guitartutor.presenters.PractisePresenter;
import com.example.jlo19.guitartutor.presenters.PractiseSetupPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IPresenter;
import com.example.jlo19.guitartutor.services.AmazonS3Service;
import com.example.jlo19.guitartutor.services.DatabaseService;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;

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
    IPresenter provideLearnViewAllChordsPresenter() {
        return new LearnViewAllChordsPresenter();
    }

    @Provides
    @Singleton
    ILearnChordPresenter provideLearnChordPresenter() {
        return new LearnChordPresenter();
    }

    @Provides
    @Singleton
    IAmazonS3Service provideAmazonS3Service() { return new AmazonS3Service(); }

    @Provides
    @Singleton
    IPractiseModel providePractiseModel() { return new PractiseModel(); }

    @Provides
    @Singleton
    IPractisePresenter providePractisePresenter() {return new PractisePresenter();}

    @Provides
    @Singleton
    IPractiseSetupModel providePractiseSetupModel() {return new PractiseSetupModel(); }

    @Provides
    @Singleton
    IPractiseSetupPresenter providePractiseSetupPresenter() {return new PractiseSetupPresenter();}

    @Provides
    @Singleton
    DatabaseApi provideDatabaseApi() {return DatabaseService.getApi();}

    @Provides
    @Singleton
    ILearnChordModel provideLearnChordModel() {return new LearnChordModel();}

    @Provides
    @Singleton
    ILearnViewAllChordsModel provideLearnViewAllChordsModel() {return new LearnViewAllChordsModel();}
}

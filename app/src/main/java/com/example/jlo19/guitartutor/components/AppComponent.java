package com.example.jlo19.guitartutor.components;

import com.example.jlo19.guitartutor.activities.LearnChordActivity;
import com.example.jlo19.guitartutor.activities.LearnViewAllChordsActivity;
import com.example.jlo19.guitartutor.activities.PractiseActivity;
import com.example.jlo19.guitartutor.activities.PractiseSetupActivity;
import com.example.jlo19.guitartutor.models.LearnChordModel;
import com.example.jlo19.guitartutor.models.LearnViewAllChordsModel;
import com.example.jlo19.guitartutor.models.PractiseSetupModel;
import com.example.jlo19.guitartutor.modules.AppModule;
import com.example.jlo19.guitartutor.presenters.LearnChordPresenter;
import com.example.jlo19.guitartutor.presenters.LearnViewAllChordsPresenter;
import com.example.jlo19.guitartutor.presenters.PractisePresenter;
import com.example.jlo19.guitartutor.presenters.PractiseSetupPresenter;
import com.example.jlo19.guitartutor.services.AmazonS3Service;

import javax.inject.Singleton;

import dagger.Component;

/**
 * The bridge between the injection of dependencies and the AppModule
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(LearnViewAllChordsActivity activity);
    void inject(LearnChordActivity activity);
    void inject(LearnChordPresenter presenter);
    void inject(PractiseSetupActivity activity);
    void inject(PractisePresenter presenter);
    void inject(PractiseActivity activity);
    void inject(PractiseSetupPresenter presenter);
    void inject(PractiseSetupModel model);
    void inject(LearnChordModel model);
    void inject(LearnViewAllChordsPresenter presenter);
    void inject(LearnViewAllChordsModel model);
    void inject(AmazonS3Service service);
}

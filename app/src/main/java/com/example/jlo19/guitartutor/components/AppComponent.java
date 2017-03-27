package com.example.jlo19.guitartutor.components;

import com.example.jlo19.guitartutor.activities.LearnAllChordsActivity;
import com.example.jlo19.guitartutor.activities.LearnChordActivity;
import com.example.jlo19.guitartutor.activities.LoginActivity;
import com.example.jlo19.guitartutor.activities.PractiseActivity;
import com.example.jlo19.guitartutor.activities.PractiseSetupActivity;
import com.example.jlo19.guitartutor.activities.RegisterActivity;
import com.example.jlo19.guitartutor.activities.SongLibraryActivity;
import com.example.jlo19.guitartutor.models.LearnAllChordsModel;
import com.example.jlo19.guitartutor.models.LearnChordModel;
import com.example.jlo19.guitartutor.models.LoginModel;
import com.example.jlo19.guitartutor.models.PractiseSetupModel;
import com.example.jlo19.guitartutor.models.RegisterModel;
import com.example.jlo19.guitartutor.models.SongLibraryModel;
import com.example.jlo19.guitartutor.modules.AppModule;
import com.example.jlo19.guitartutor.presenters.LearnChordPresenter;
import com.example.jlo19.guitartutor.presenters.LearnAllChordsPresenter;
import com.example.jlo19.guitartutor.presenters.LoginPresenter;
import com.example.jlo19.guitartutor.presenters.PractisePresenter;
import com.example.jlo19.guitartutor.presenters.PractiseSetupPresenter;
import com.example.jlo19.guitartutor.presenters.RegisterPresenter;
import com.example.jlo19.guitartutor.presenters.SongLibraryPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * The bridge between the injection of dependencies and the AppModule
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(LearnAllChordsActivity activity);
    void inject(LearnChordActivity activity);
    void inject(LearnChordPresenter presenter);
    void inject(PractiseSetupActivity activity);
    void inject(PractisePresenter presenter);
    void inject(PractiseActivity activity);
    void inject(PractiseSetupPresenter presenter);
    void inject(PractiseSetupModel model);
    void inject(LearnChordModel model);
    void inject(LearnAllChordsPresenter presenter);
    void inject(LearnAllChordsModel model);
    void inject(SongLibraryActivity activity);
    void inject(SongLibraryPresenter presenter);
    void inject(SongLibraryModel model);
    void inject(RegisterActivity activity);
    void inject(RegisterPresenter presenter);
    void inject(RegisterModel model);
    void inject(LoginActivity activity);
    void inject(LoginPresenter presenter);
    void inject(LoginModel model);
}

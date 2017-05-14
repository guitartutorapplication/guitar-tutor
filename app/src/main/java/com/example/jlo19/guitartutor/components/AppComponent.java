package com.example.jlo19.guitartutor.components;

import com.example.jlo19.guitartutor.activities.AccountActivity;
import com.example.jlo19.guitartutor.activities.AccountActivityActivity;
import com.example.jlo19.guitartutor.activities.EditAccountActivity;
import com.example.jlo19.guitartutor.activities.LearnAllChordsActivity;
import com.example.jlo19.guitartutor.activities.LearnChordActivity;
import com.example.jlo19.guitartutor.activities.LoginActivity;
import com.example.jlo19.guitartutor.activities.PractiseActivity;
import com.example.jlo19.guitartutor.activities.PractiseSetupActivity;
import com.example.jlo19.guitartutor.activities.RegisterActivity;
import com.example.jlo19.guitartutor.activities.SongActivity;
import com.example.jlo19.guitartutor.activities.SongLibraryActivity;
import com.example.jlo19.guitartutor.models.PractiseModel;
import com.example.jlo19.guitartutor.modules.AppModule;
import com.example.jlo19.guitartutor.presenters.PractisePresenter;

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
    void inject(PractiseSetupActivity activity);

    void inject(PractisePresenter presenter);
    void inject(PractiseActivity activity);
    void inject(PractiseModel model);

    void inject(SongLibraryActivity activity);
    void inject(RegisterActivity activity);
    void inject(LoginActivity activity);
    void inject(AccountActivity activity);
    void inject(EditAccountActivity activity);
    void inject(SongActivity activity);
    void inject(AccountActivityActivity activity);
}

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
import com.example.jlo19.guitartutor.models.AccountActivityModel;
import com.example.jlo19.guitartutor.models.AccountModel;
import com.example.jlo19.guitartutor.models.EditAccountModel;
import com.example.jlo19.guitartutor.models.LearnAllChordsModel;
import com.example.jlo19.guitartutor.models.LearnChordModel;
import com.example.jlo19.guitartutor.models.LoginModel;
import com.example.jlo19.guitartutor.models.PractiseModel;
import com.example.jlo19.guitartutor.models.PractiseSetupModel;
import com.example.jlo19.guitartutor.models.RegisterModel;
import com.example.jlo19.guitartutor.models.SongLibraryModel;
import com.example.jlo19.guitartutor.models.SongModel;
import com.example.jlo19.guitartutor.modules.AppModule;
import com.example.jlo19.guitartutor.presenters.AccountActivityPresenter;
import com.example.jlo19.guitartutor.presenters.AccountPresenter;
import com.example.jlo19.guitartutor.presenters.EditAccountPresenter;
import com.example.jlo19.guitartutor.presenters.LearnChordPresenter;
import com.example.jlo19.guitartutor.presenters.LearnAllChordsPresenter;
import com.example.jlo19.guitartutor.presenters.LoginPresenter;
import com.example.jlo19.guitartutor.presenters.PractisePresenter;
import com.example.jlo19.guitartutor.presenters.PractiseSetupPresenter;
import com.example.jlo19.guitartutor.presenters.RegisterPresenter;
import com.example.jlo19.guitartutor.presenters.SongLibraryPresenter;
import com.example.jlo19.guitartutor.presenters.SongPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * The bridge between the injection of dependencies and the AppModule
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(LearnAllChordsActivity activity);
    void inject(LearnAllChordsPresenter presenter);
    void inject(LearnAllChordsModel model);

    void inject(LearnChordActivity activity);
    void inject(LearnChordPresenter presenter);
    void inject(LearnChordModel model);

    void inject(PractiseSetupActivity activity);
    void inject(PractiseSetupPresenter presenter);
    void inject(PractiseSetupModel model);

    void inject(PractisePresenter presenter);
    void inject(PractiseActivity activity);
    void inject(PractiseModel model);

    void inject(SongLibraryActivity activity);
    void inject(SongLibraryPresenter presenter);
    void inject(SongLibraryModel model);

    void inject(RegisterActivity activity);
    void inject(RegisterPresenter presenter);
    void inject(RegisterModel model);

    void inject(LoginActivity activity);
    void inject(LoginPresenter presenter);
    void inject(LoginModel model);

    void inject(AccountActivity activity);
    void inject(AccountPresenter presenter);
    void inject(AccountModel model);

    void inject(EditAccountActivity activity);
    void inject(EditAccountPresenter presenter);
    void inject(EditAccountModel model);

    void inject(SongActivity activity);
    void inject(SongPresenter presenter);
    void inject(SongModel model);

    void inject(AccountActivityActivity activity);
    void inject(AccountActivityPresenter presenter);
    void inject(AccountActivityModel model);
}

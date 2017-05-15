package com.example.jlo19.guitartutor.modules;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.interactors.AddUserChordInteractor;
import com.example.jlo19.guitartutor.interactors.GetSongsInteractor;
import com.example.jlo19.guitartutor.interactors.GetUserChordsInteractor;
import com.example.jlo19.guitartutor.interactors.EditAccountDetailsInteractor;
import com.example.jlo19.guitartutor.interactors.GetAccountDetailsInteractor;
import com.example.jlo19.guitartutor.interactors.GetChordsInteractor;
import com.example.jlo19.guitartutor.interactors.LoginInteractor;
import com.example.jlo19.guitartutor.interactors.UpdateUserChordsInteractor;
import com.example.jlo19.guitartutor.interactors.RegisterInteractor;
import com.example.jlo19.guitartutor.interactors.interfaces.IAddUserChordInteractor;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetChordsInteractor;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetUserChordsInteractor;
import com.example.jlo19.guitartutor.interactors.interfaces.IEditAccountDetailsInteractor;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetAccountDetailsInteractor;
import com.example.jlo19.guitartutor.interactors.interfaces.ILoginInteractor;
import com.example.jlo19.guitartutor.interactors.interfaces.IUpdateUserChordsInteractor;
import com.example.jlo19.guitartutor.interactors.interfaces.IRegisterInteractor;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetSongsInteractor;
import com.example.jlo19.guitartutor.presenters.AccountActivityPresenter;
import com.example.jlo19.guitartutor.presenters.AccountPresenter;
import com.example.jlo19.guitartutor.presenters.EditAccountPresenter;
import com.example.jlo19.guitartutor.presenters.LearnAllChordsPresenter;
import com.example.jlo19.guitartutor.presenters.LearnChordPresenter;
import com.example.jlo19.guitartutor.presenters.LoginPresenter;
import com.example.jlo19.guitartutor.presenters.PractisePresenter;
import com.example.jlo19.guitartutor.presenters.PractiseSetupPresenter;
import com.example.jlo19.guitartutor.presenters.RegisterPresenter;
import com.example.jlo19.guitartutor.presenters.SongLibraryPresenter;
import com.example.jlo19.guitartutor.presenters.SongPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountActivityPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IEditAccountPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongLibraryPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongPresenter;
import com.example.jlo19.guitartutor.services.AmazonS3Service;
import com.example.jlo19.guitartutor.services.DatabaseService;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;
import com.example.jlo19.guitartutor.timers.BeatTimer;
import com.example.jlo19.guitartutor.timers.PractiseActivityTimer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides dependencies
 */
@Module
public class AppModule {

    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    // Presenters
    @Provides
    @Singleton
    ILearnAllChordsPresenter provideLearnAllChordsPresenter() {
        return new LearnAllChordsPresenter(createGetChordsInteractor(), application.getLoggedInUser());
    }

    @Provides
    @Singleton
    ILearnChordPresenter provideLearnChordPresenter() {
        return new LearnChordPresenter(createAddUserChordInteractor(), createAmazonS3Service(),
                application.getLoggedInUser());
    }

    @Provides
    IPractisePresenter providePractisePresenter() {
        return new PractisePresenter(createUpdateUserChordsInteractor(), application.getLoggedInUser(),
                new BeatTimer(), new PractiseActivityTimer());
    }

    @Provides
    @Singleton
    IPractiseSetupPresenter providePractiseSetupPresenter() {
        return new PractiseSetupPresenter(createGetUserChordsInteractor(), application.getLoggedInUser(),
                new BeatTimer());
    }

    @Provides
    @Singleton
    ISongLibraryPresenter provideSongLibraryPresenter() {
        return new SongLibraryPresenter(createGetSongsInteractor(), application.getLoggedInUser());
    }

    @Provides
    @Singleton
    IRegisterPresenter provideRegisterPresenter() {
        return new RegisterPresenter(createRegisterInteractor());
    }

    @Provides
    @Singleton
    ILoginPresenter provideLoginPresenter() {
        return new LoginPresenter(createLoginInteractor(),
                application.getLoggedInUser());
    }

    @Provides
    @Singleton
    IAccountPresenter provideAccountPresenter() {
        return new AccountPresenter(createGetAccountDetailsInteractor(),
                application.getLoggedInUser());
    }

    @Provides
    @Singleton
    IEditAccountPresenter provideEditAccountPresenter() {
        return new EditAccountPresenter(createEditAccountDetailsInteractor(),
                application.getLoggedInUser());
    }

    @Provides
    @Singleton
    ISongPresenter provideSongPresenter() {
        return new SongPresenter(createAmazonS3Service());
    }

    @Provides
    @Singleton
    IAccountActivityPresenter provideAccountActivityPresenter() {
        return new AccountActivityPresenter(createGetUserChordsInteractor(),
                application.getLoggedInUser());
    }

    // Interactors
    private IGetSongsInteractor createGetSongsInteractor(){
        return new GetSongsInteractor(createDatabaseApi(), createGetUserChordsInteractor());
    }

    private IRegisterInteractor createRegisterInteractor() {
        return new RegisterInteractor(createDatabaseApi());
    }

    private ILoginInteractor createLoginInteractor() {
        return new LoginInteractor(createDatabaseApi());
    }
    private IGetAccountDetailsInteractor createGetAccountDetailsInteractor() {
        return new GetAccountDetailsInteractor(createDatabaseApi());
    }

    private IEditAccountDetailsInteractor createEditAccountDetailsInteractor() {
        return new EditAccountDetailsInteractor(createDatabaseApi());
    }

    private IGetUserChordsInteractor createGetUserChordsInteractor() {
        return new GetUserChordsInteractor(createDatabaseApi());
    }
    private IUpdateUserChordsInteractor createUpdateUserChordsInteractor() {
        return new UpdateUserChordsInteractor(createDatabaseApi());
    }

    private IAddUserChordInteractor createAddUserChordInteractor() {
        return new AddUserChordInteractor(createDatabaseApi());
    }

    private IGetChordsInteractor createGetChordsInteractor() {
        return new GetChordsInteractor(createDatabaseApi(), createGetUserChordsInteractor(),
                createGetAccountDetailsInteractor());
    }

    // Services
    private IAmazonS3Service createAmazonS3Service() {
        return new AmazonS3Service(application.getApplicationContext());
    }

    private DatabaseApi createDatabaseApi() {
        return DatabaseService.getApi(application.getApplicationContext());
    }
}

package com.example.jlo19.guitartutor.modules;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.models.AddUserChordInteractor;
import com.example.jlo19.guitartutor.models.GetUserChordsInteractor;
import com.example.jlo19.guitartutor.models.EditAccountDetailsInteractor;
import com.example.jlo19.guitartutor.models.GetAccountDetailsInteractor;
import com.example.jlo19.guitartutor.models.GetChordsInteractor;
import com.example.jlo19.guitartutor.models.LoginInteractor;
import com.example.jlo19.guitartutor.models.PractiseModel;
import com.example.jlo19.guitartutor.models.PractiseSetupModel;
import com.example.jlo19.guitartutor.models.RegisterInteractor;
import com.example.jlo19.guitartutor.models.SongLibraryModel;
import com.example.jlo19.guitartutor.models.interfaces.IAddUserChordInteractor;
import com.example.jlo19.guitartutor.models.interfaces.IGetChordsInteractor;
import com.example.jlo19.guitartutor.models.interfaces.IGetUserChordsInteractor;
import com.example.jlo19.guitartutor.models.interfaces.IEditAccountDetailsInteractor;
import com.example.jlo19.guitartutor.models.interfaces.IGetAccountDetailsInteractor;
import com.example.jlo19.guitartutor.models.interfaces.ILoginInteractor;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseModel;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseSetupModel;
import com.example.jlo19.guitartutor.models.interfaces.IRegisterInteractor;
import com.example.jlo19.guitartutor.models.interfaces.ISongLibraryModel;
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

    @Provides
    @Singleton
    ILearnAllChordsPresenter provideLearnAllChordsPresenter() {
        return new LearnAllChordsPresenter(createGetChordsInteractor(), application.getLoggedInUser());
    }

    @Provides
    @Singleton
    ILearnChordPresenter provideLearnChordPresenter() {
        return new LearnChordPresenter(createAddUserChordInteractor(), provideAmazonS3Service(),
                application.getLoggedInUser());
    }

    @Provides
    @Singleton
    IAmazonS3Service provideAmazonS3Service() {
        return new AmazonS3Service(application.getApplicationContext());
    }

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
    DatabaseApi provideDatabaseApi() {return DatabaseService.getApi(application.getApplicationContext());}

    private IAddUserChordInteractor createAddUserChordInteractor() {
        return new AddUserChordInteractor(provideDatabaseApi());
    }

    private IGetChordsInteractor createGetChordsInteractor() {
        return new GetChordsInteractor(provideDatabaseApi());
    }

    @Provides
    @Singleton
    ISongLibraryPresenter provideSongLibraryPresenter() {return new SongLibraryPresenter();}

    @Provides
    @Singleton
    ISongLibraryModel provideSongLibraryModel(){return new SongLibraryModel();}

    @Provides
    @Singleton
    IRegisterPresenter provideRegisterPresenter() {
        return new RegisterPresenter(createRegisterInteractor());
    }

    private IRegisterInteractor createRegisterInteractor() {
        return new RegisterInteractor(provideDatabaseApi());
    }

    @Provides
    @Singleton
    ILoginPresenter provideLoginPresenter() {
        return new LoginPresenter(createLoginInteractor(),
            application.getLoggedInUser());
    }

    private ILoginInteractor createLoginInteractor() {
        return new LoginInteractor(provideDatabaseApi());
    }

    @Provides
    @Singleton
    IAccountPresenter provideAccountPresenter() {
        return new AccountPresenter(createGetAccountDetailsInteractor(),
                application.getLoggedInUser());
    }

    private IGetAccountDetailsInteractor createGetAccountDetailsInteractor() {
        return new GetAccountDetailsInteractor(provideDatabaseApi());
    }

    @Provides
    @Singleton
    IEditAccountPresenter provideEditAccountPresenter() {
        return new EditAccountPresenter(createEditAccountDetailsInteractor(),
                application.getLoggedInUser());
    }

    private IEditAccountDetailsInteractor createEditAccountDetailsInteractor() {
        return new EditAccountDetailsInteractor(provideDatabaseApi());
    }

    @Provides
    @Singleton
    ISongPresenter provideSongPresenter() {
        return new SongPresenter(provideAmazonS3Service());
    }

    @Provides
    @Singleton
    IAccountActivityPresenter provideAccountActivityPresenter() {
        return new AccountActivityPresenter(createGetUserChordsInteractor(),
                application.getLoggedInUser());
    }

    private IGetUserChordsInteractor createGetUserChordsInteractor() {
        return new GetUserChordsInteractor(provideDatabaseApi());
    }
}

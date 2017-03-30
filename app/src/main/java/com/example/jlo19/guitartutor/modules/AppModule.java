package com.example.jlo19.guitartutor.modules;

import com.example.jlo19.guitartutor.models.AccountModel;
import com.example.jlo19.guitartutor.models.EditAccountModel;
import com.example.jlo19.guitartutor.models.LearnChordModel;
import com.example.jlo19.guitartutor.models.LearnAllChordsModel;
import com.example.jlo19.guitartutor.models.LoginModel;
import com.example.jlo19.guitartutor.models.PractiseModel;
import com.example.jlo19.guitartutor.models.PractiseSetupModel;
import com.example.jlo19.guitartutor.models.RegisterModel;
import com.example.jlo19.guitartutor.models.SongLibraryModel;
import com.example.jlo19.guitartutor.models.interfaces.IAccountModel;
import com.example.jlo19.guitartutor.models.interfaces.IEditAccountModel;
import com.example.jlo19.guitartutor.models.interfaces.ILearnChordModel;
import com.example.jlo19.guitartutor.models.interfaces.ILearnViewAllChordsModel;
import com.example.jlo19.guitartutor.models.interfaces.ILoginModel;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseModel;
import com.example.jlo19.guitartutor.models.interfaces.IPractiseSetupModel;
import com.example.jlo19.guitartutor.models.interfaces.IRegisterModel;
import com.example.jlo19.guitartutor.models.interfaces.ISongLibraryModel;
import com.example.jlo19.guitartutor.presenters.AccountPresenter;
import com.example.jlo19.guitartutor.presenters.EditAccountPresenter;
import com.example.jlo19.guitartutor.presenters.LearnAllChordsPresenter;
import com.example.jlo19.guitartutor.presenters.LearnChordPresenter;
import com.example.jlo19.guitartutor.presenters.LoginPresenter;
import com.example.jlo19.guitartutor.presenters.PractisePresenter;
import com.example.jlo19.guitartutor.presenters.PractiseSetupPresenter;
import com.example.jlo19.guitartutor.presenters.RegisterPresenter;
import com.example.jlo19.guitartutor.presenters.SongLibraryPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IEditAccountPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.IRegisterPresenter;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongLibraryPresenter;
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
        return new LearnAllChordsPresenter();
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
    ILearnViewAllChordsModel provideLearnViewAllChordsModel() {return new LearnAllChordsModel();}

    @Provides
    @Singleton
    ISongLibraryPresenter provideSongLibraryPresenter() {return new SongLibraryPresenter();}

    @Provides
    @Singleton
    ISongLibraryModel provideSongLibraryModel(){return new SongLibraryModel();}

    @Provides
    @Singleton
    IRegisterPresenter provideRegisterPresenter() {return new RegisterPresenter();}

    @Provides
    @Singleton
    IRegisterModel provideRegisterModel() {return new RegisterModel(); }

    @Provides
    @Singleton
    ILoginPresenter provideLoginPresenter() {return new LoginPresenter();}

    @Provides
    @Singleton
    ILoginModel provideLoginModel() {return new LoginModel();}

    @Provides
    @Singleton
    IAccountPresenter provideAccountPresenter() {return new AccountPresenter();}

    @Provides
    @Singleton
    IAccountModel provideAccountModel() {return new AccountModel();}

    @Provides
    @Singleton
    IEditAccountPresenter provideEditAccountPresenter() {return new EditAccountPresenter();}

    @Provides
    @Singleton
    IEditAccountModel provideEditAccountModel() {return new EditAccountModel();}
}

package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.interfaces.ILoginModel;
import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;
import com.example.jlo19.guitartutor.views.LoginView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Testing LoginPresenter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class LoginPresenterTest {

    private ILoginPresenter presenter;
    private LoginView view;
    private ILoginModel model;
    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new LoginPresenter();

        sharedPreferences = Mockito.mock(SharedPreferences.class);
        presenter.setSharedPreferences(sharedPreferences);

        model = Mockito.mock(ILoginModel.class);
        ((LoginPresenter) presenter).setModel(model);

        view = Mockito.mock(LoginView.class);
        presenter.setView(view);
    }

    @Test
    public void setModel_SetsSharedPreferencesOnModel() {
        // assert
        Mockito.verify(model).setSharedPreferences(sharedPreferences);
    }

    @Test
    public void modelOnUserAlreadyLoggedIn_StartHomeActivityOnView() {
        // act
        presenter.modelOnUserAlreadyLoggedIn();

        // assert
        Mockito.verify(view).startHomeActivity();
    }

    @Test
    public void setModel_CallsCheckForPreexistingUserOnModel() {
        // assert
        Mockito.verify(model).checkForPreexistingLogIn();
    }

    @Test
    public void setModel_SetsPresenterOnModel() {
        // assert
        Mockito.verify(model).setPresenter(presenter);
    }

    @Test
    public void viewOnRegister_CallsStartRegisterActivityOnView() {
        // act
        presenter.viewOnRegister();

        // assert
        Mockito.verify(view).startRegisterActivity();
    }

    @Test
    public void viewOnLogin_ShowsProgressBarOnView() {
        // act
        presenter.viewOnLogin("kate@gmail.com", "password");

        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void viewOnLogin_ResetErrorOnView() {
        // act
        presenter.viewOnLogin("kate@gmail.com", "password");

        // assert
        Mockito.verify(view).resetFieldEmptyErrors();
    }

    @Test
    public void viewOnLogin_CallsLoginOnModel() {
        // act
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "password";
        presenter.viewOnLogin(expectedEmail, expectedPassword);

        // assert
        Mockito.verify(model).login(expectedEmail, expectedPassword);
    }

    @Test
    public void modelOnFieldEmptyEmail_ShowsFieldEmptyErrorOnView() {
        // act
        presenter.modelOnFieldEmailEmpty();

        // assert
        Mockito.verify(view).showFieldEmailEmptyError();
    }

    @Test
    public void modelOnFieldEmptyEmail_HidesProgressBarOnView() {
        // act
        presenter.modelOnFieldEmailEmpty();

        // assert
        Mockito.verify(view).hideProgressBar();
    }


    @Test
    public void modelOnFieldEmptyPassword_ShowsFieldEmptyErrorOnView() {
        // act
        presenter.modelOnFieldPasswordEmpty();

        // assert
        Mockito.verify(view).showFieldPasswordEmptyError();
    }

    @Test
    public void modelOnFieldEmptyPassword_HidesProgressBarOnView() {
        // act
        presenter.modelOnFieldPasswordEmpty();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnLoginError_HidesProgressBarOnView() {
        // act
        presenter.modelOnLoginError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnLoginError_ShowsLoginErrorOnView() {
        // act
        presenter.modelOnLoginError();

        // assert
        Mockito.verify(view).showLoginError();
    }

    @Test
    public void modelOnLoginSuccess_HidesProgressBarOnView() {
        // act
        presenter.modelOnLoginSuccess();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnLoginSuccess_CallsStartHomeActivityOnView() {
        // act
        presenter.modelOnLoginSuccess();

        // assert
        Mockito.verify(view).startHomeActivity();
    }
}

package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.models.interfaces.ILoginInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.ILoginPresenter;
import com.example.jlo19.guitartutor.views.LoginView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Testing LoginPresenter
 */
public class LoginPresenterTest {

    private ILoginPresenter presenter;
    private LoginView view;
    private ILoginInteractor loginInteractor;
    private LoggedInUser loggedInUser;

    @Before
    public void setUp() {
        loggedInUser = Mockito.mock(LoggedInUser.class);
        loginInteractor = Mockito.mock(ILoginInteractor.class);
        presenter = new LoginPresenter(loginInteractor, loggedInUser);

        view = Mockito.mock(LoginView.class);
        presenter.setView(view);
    }

    @Test
    public void userIsLoggedIn_SetView_StartHomeActivityOnView() {
        // arrange
        Mockito.when(loggedInUser.isLoggedIn()).thenReturn(true);

        // act
        presenter.setView(view);

        // assert
        Mockito.verify(view).startHomeActivity();
    }

    @Test
    public void setsPresenterAsListenerOnInteractor() {
        // assert
        Mockito.verify(loginInteractor).setListener(presenter);
    }

    @Test
    public void viewOnRegister_CallsStartRegisterActivityOnView() {
        // act
        presenter.viewOnRegister();

        // assert
        Mockito.verify(view).startRegisterActivity();
    }

    @Test
    public void viewOnLoginWithValidCredentials_ShowsProgressBarOnView() {
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
    public void viewOnLoginWithValidCredentials_CallsLoginOnInteractor() {
        // act
        String expectedEmail = "kate@gmail.com";
        String expectedPassword = "password";
        presenter.viewOnLogin(expectedEmail, expectedPassword);

        // assert
        Mockito.verify(loginInteractor).login(expectedEmail, expectedPassword);
    }

    @Test
    public void loginWithFieldEmptyEmail_ShowsFieldEmptyErrorOnView() {
        // act
        presenter.viewOnLogin("", "password");

        // assert
        Mockito.verify(view).showFieldEmailEmptyError();
    }

    @Test
    public void loginWithFieldEmptyPassword_ShowsFieldEmptyErrorOnView() {
        // act
        presenter.viewOnLogin("katesmith@gmail.com", "");

        // assert
        Mockito.verify(view).showFieldPasswordEmptyError();
    }

    @Test
    public void onLoginError_HidesProgressBarOnView() {
        // act
        presenter.onLoginError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onLoginError_ShowsLoginErrorOnView() {
        // act
        presenter.onLoginError();

        // assert
        Mockito.verify(view).showLoginError();
    }

    @Test
    public void onLoginSuccess_HidesProgressBarOnView() {
        // act
        presenter.onLoginSuccess(new User(1, "api_key"));

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onLoginSuccess_CallsStartHomeActivityOnView() {
        // act
        presenter.onLoginSuccess(new User(1, "api_key"));

        // assert
        Mockito.verify(view).startHomeActivity();
    }

    @Test
    public void onLoginSuccess_CallsLoginOnLoggedInUser() {
        // act
        User user = new User(1, "api_key");
        presenter.onLoginSuccess(user);

        // assert
        Mockito.verify(loggedInUser).login(user.getId(), user.getApiKey());
    }
}

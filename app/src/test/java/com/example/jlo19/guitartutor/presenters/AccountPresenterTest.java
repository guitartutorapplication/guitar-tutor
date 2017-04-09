package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.interfaces.IAccountModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountPresenter;
import com.example.jlo19.guitartutor.views.AccountView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Testing AccountPresenter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class AccountPresenterTest {

    private IAccountPresenter presenter;
    private SharedPreferences sharedPreferences;
    private IAccountModel model;
    private AccountView view;

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new AccountPresenter();

        sharedPreferences = Mockito.mock(SharedPreferences.class);
        presenter.setSharedPreferences(sharedPreferences);

        model = Mockito.mock(IAccountModel.class);
        ((AccountPresenter) presenter).setModel(model);

        view = Mockito.mock(AccountView.class);
        presenter.setView(view);
    }

    @Test
    public void setModel_SetsSharedPreferencesOnModel() {
        // assert
        Mockito.verify(model).setSharedPreferences(sharedPreferences);
    }

    @Test
    public void setModel_SetsPresenterOnModel() {
        // assert
        Mockito.verify(model).setPresenter(presenter);
    }

    @Test
    public void setModel_CallsGetAccountDetailsOnModel() {
        // assert
        Mockito.verify(model).getAccountDetails();
    }

    @Test
    public void setView_HidesAccountButtonOnView() {
        // assert
        Mockito.verify(view).hideAccountButton();
    }

    @Test
    public void setView_ShowProgressBarOnView() {
        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void modelOnAccountDetailsRetrieved_SetsAccountDetailsOnView() {
        // act
        User user = new User("Kate", "katesmith@gmail.com", 2, 2000);
        presenter.modelOnAccountDetailsRetrieved(user);

        // assert
        Mockito.verify(view).setAccountDetails(user);
    }

    @Test
    public void modelOnAccountDetailsRetrieved_HidesProgressBarOnView() {
        // act
        User user = new User("Kate", "katesmith@gmail.com", 2, 2000);
        presenter.modelOnAccountDetailsRetrieved(user);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnError_ShowsErrorOnView() {
        // act
        presenter.modelOnError();

        // assert
        Mockito.verify(view).showError();
    }

    @Test
    public void modelOnError_HidesProgressBarOnView() {
        // act
        presenter.modelOnError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void viewOnLogOut_CallsLogoutOnModel() {
        // act
        presenter.viewOnLogout();

        // assert
        Mockito.verify(model).logout();
    }

    @Test
    public void viewOnLogout_CallsStartLoginActivityOnView() {
        // act
        presenter.viewOnLogout();

        // assert
        Mockito.verify(view).startLoginActivity();
    }

    @Test
    public void viewOnEditAccount_CallsStartEditAccountActivityOnView() {
        // act
        presenter.viewOnEditAccount();

        // assert
        Mockito.verify(view).startEditAccountActivity();
    }
}

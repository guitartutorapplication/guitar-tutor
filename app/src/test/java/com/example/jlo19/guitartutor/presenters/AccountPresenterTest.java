package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.models.interfaces.IGetAccountDetailsInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountPresenter;
import com.example.jlo19.guitartutor.views.AccountView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Testing AccountPresenter
 */
public class AccountPresenterTest {

    private IAccountPresenter presenter;
    private IGetAccountDetailsInteractor getAccountDetailsInteractor;
    private AccountView view;
    private LoggedInUser loggedInUser;

    @Before
    public void setUp() {
        loggedInUser = Mockito.mock(LoggedInUser.class);
        Mockito.when(loggedInUser.getApiKey()).thenReturn("api_key");
        Mockito.when(loggedInUser.getUserId()).thenReturn(2);

        getAccountDetailsInteractor = Mockito.mock(IGetAccountDetailsInteractor.class);
        presenter = new AccountPresenter(getAccountDetailsInteractor, loggedInUser);

        view = Mockito.mock(AccountView.class);
        presenter.setView(view);
    }

    @Test
    public void setsPresenterAsListenerOnInteractor() {
        // assert
        Mockito.verify(getAccountDetailsInteractor).setListener(presenter);
    }

    @Test
    public void setView_CallsGetAccountDetailsOnInteractor() {
        // assert
        Mockito.verify(getAccountDetailsInteractor).getAccountDetails(loggedInUser.getApiKey(),
                loggedInUser.getUserId());
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
    public void onAccountDetailsRetrieved_SetsAccountDetailsOnView() {
        // act
        User user = new User("Kate", "katesmith@gmail.com", 2, 2000);
        presenter.onAccountDetailsRetrieved(user);

        // assert
        Mockito.verify(view).setAccountDetails(user.getName(), user.getEmail(), user.getLevel(),
                user.getAchievements());
    }

    @Test
    public void onAccountDetailsRetrieved_HidesProgressBarOnView() {
        // act
        User user = new User("Kate", "katesmith@gmail.com", 2, 2000);
        presenter.onAccountDetailsRetrieved(user);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onError_ShowsErrorOnView() {
        // act
        presenter.onError();

        // assert
        Mockito.verify(view).showError();
    }

    @Test
    public void onError_HidesProgressBarOnView() {
        // act
        presenter.onError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void viewOnLogout_CallsLogoutOnLoggedInUser() {
        // act
        presenter.viewOnLogout();

        // assert
        Mockito.verify(loggedInUser).logout();
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
        // arrange
        User user = new User("Kate", "katesmith@gmail.com", 2, 2000);
        presenter.onAccountDetailsRetrieved(user);

        // act
        presenter.viewOnEditAccount();

        // assert
        Mockito.verify(view).startEditAccountActivity(user);
    }

    @Test
    public void viewOnAccountActivityRequested_CallsStartAccountActivityActivityOnView(){
        // act
        presenter.viewOnAccountActivityRequested();

        // assert
        Mockito.verify(view).startAccountActivityActivity();
    }

    @Test
    public void viewOnConfirmError_CallFinishActivityOnView() {
        // act
        presenter.viewOnConfirmError();

        // assert
        Mockito.verify(view).finishActivity();
    }
}

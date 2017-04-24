package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.models.interfaces.IGetUserChordsInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountActivityPresenter;
import com.example.jlo19.guitartutor.views.AccountActivityView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

/**
 * Testing AccountActivityPresenter
 */
public class AccountActivityPresenterTest {

    private IAccountActivityPresenter presenter;
    private IGetUserChordsInteractor getUserChordsInteractor;
    private AccountActivityView view;
    private LoggedInUser loggedInUser;

    @Before
    public void setUp() {
        loggedInUser = Mockito.mock(LoggedInUser.class);
        Mockito.when(loggedInUser.getUserId()).thenReturn(2);
        Mockito.when(loggedInUser.getApiKey()).thenReturn("api_key");
        getUserChordsInteractor = Mockito.mock(IGetUserChordsInteractor.class);
        presenter = new AccountActivityPresenter(getUserChordsInteractor, loggedInUser);

        view = Mockito.mock(AccountActivityView.class);
        presenter.setView(view);
    }

    @Test
    public void setView_CallsShowProgressBarOnView() {
        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void setView_CallsGetUserChordsOnInteractor() {
        // assert
        Mockito.verify(getUserChordsInteractor).getUserChords(loggedInUser.getApiKey(),
                loggedInUser.getUserId());
    }

    @Test
    public void setsPresenterAsListenerOnInteractor() {
        // assert
        Mockito.verify(getUserChordsInteractor).setListener(presenter);
    }

    @Test
    public void onUserChordsRetrieved_HidesProgressBarOnView() {
        // act
        presenter.onUserChordsRetrieved(null);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onUserChordsRetrieved_SetsActivityOnView() {
        // act
        List<Chord> expectedChords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
        presenter.onUserChordsRetrieved(expectedChords);

        // assert
        Mockito.verify(view).setAccountActivity(expectedChords);
    }

    @Test
    public void onError_HidesProgressBarOnView() {
        // act
        presenter.onError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onError_ShowErrorOnView() {
        // act
        presenter.onError();

        // assert
        Mockito.verify(view).showError();
    }

    @Test
    public void viewOnConfirmError_CallFinishActivityOnView() {
        // act
        presenter.viewOnConfirmError();

        // assert
        Mockito.verify(view).finishActivity();
    }
}

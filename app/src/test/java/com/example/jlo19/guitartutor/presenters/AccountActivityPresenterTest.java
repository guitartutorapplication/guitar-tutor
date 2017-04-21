package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.interfaces.IAccountActivityModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IAccountActivityPresenter;
import com.example.jlo19.guitartutor.views.AccountActivityView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Testing AccountActivityPresenter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class AccountActivityPresenterTest {

    private IAccountActivityPresenter presenter;
    private IAccountActivityModel model;
    private AccountActivityView view;
    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new AccountActivityPresenter();

        sharedPreferences = Mockito.mock(SharedPreferences.class);
        presenter.setSharedPreferences(sharedPreferences);

        model = Mockito.mock(IAccountActivityModel.class);
        ((AccountActivityPresenter) presenter).setModel(model);

        view = Mockito.mock(AccountActivityView.class);
        presenter.setView(view);
    }

    @Test
    public void setView_CallsShowProgressBarOnView() {
        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void setModel_CallsGetActivityOnModel() {
        // assert
        Mockito.verify(model).getAccountActivity();
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
    public void modelOnAccountActivityRetrieved_HidesProgressBarOnView() {
        // act
        presenter.modelOnAccountActivityRetrieved(null);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnAccountActivityRetrieved_SetsActivityOnView() {
        // act
        List<Chord> expectedChords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
        presenter.modelOnAccountActivityRetrieved(expectedChords);

        // assert
        Mockito.verify(view).setAccountActivity(expectedChords);
    }

    @Test
    public void modelOnError_HidesProgressBarOnView() {
        // act
        presenter.modelOnError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnError_ShowErrorOnView() {
        // act
        presenter.modelOnError();

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

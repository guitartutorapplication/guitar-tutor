package com.example.jlo19.guitartutor.presenters;

import android.view.View;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.interfaces.ISongModel;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongPresenter;
import com.example.jlo19.guitartutor.views.SongView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Testing SongPresenter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class SongPresenterTest {

    private ISongPresenter presenter;
    private SongView view;
    private ISongModel model;
    private String audioFilename;

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new SongPresenter();

        view = Mockito.mock(SongView.class);
        audioFilename = "Adventure of a Lifetime.wav";
        Mockito.when(view.getAudioFilename()).thenReturn(audioFilename);
        presenter.setView(view);

        model = Mockito.mock(ISongModel.class);
        ((SongPresenter) presenter).setModel(model);
    }

    @Test
    public void setModel_setsPresenterOnModel() {
        // assert
        Mockito.verify(model).setPresenter(presenter);
    }

    @Test
    public void viewOnPlay_CallsGetAudioOnModel() {
        // act
        presenter.viewOnPlay();

        // assert
        Mockito.verify(model).getAudio(audioFilename);
    }

    @Test
    public void viewOnPlay_CallsShowProgressBarOnView() {
        // act
        presenter.viewOnPlay();

        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void modelOnUrlDownloadSuccess_CallsPlayAudioOnView() {
        // act
        String url = "url";
        presenter.modelOnUrlDownloadSuccess(url);

        // assert
        Mockito.verify(view).playAudio(url);
    }

    @Test
    public void modelOnUrlDownloadFailed_CallsHideProgressBarOnView() {
        // act
        presenter.modelOnUrlDownloadFailed();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnUrlDownloadFailed_CallsShowErrorOnView() {
        // act
        presenter.modelOnUrlDownloadFailed();

        // assert
        Mockito.verify(view).showError();
    }

    @Test
    public void viewOnAudioLoadFailed_CallsHideProgressBarOnView() {
        // act
        presenter.viewOnAudioLoadFailed();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void viewOnAudioLoadFailed_CallsShowErrorOnView() {
        // act
        presenter.viewOnAudioLoadFailed();

        // assert
        Mockito.verify(view).showError();
    }

    @Test
    public void viewOnAudioLoaded_CallsHideProgressBarOnView() {
        // act
        presenter.viewOnAudioLoaded();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void viewOnAudioLoaded_CallsSetStopButtonVisibilityWithVisibleOnView() {
        // act
        presenter.viewOnAudioLoaded();

        // assert
        Mockito.verify(view).setStopButtonVisibility(View.VISIBLE);
    }

    @Test
    public void viewOnAudioLoaded_CallsSetPlayButtonVisibilityWithInvisibleOnView() {
        // act
        presenter.viewOnAudioLoaded();

        // assert
        Mockito.verify(view).setPlayButtonVisibility(View.INVISIBLE);
    }

    @Test
    public void viewOnStop_CallsSetStopButtonVisibilityWithInvisibleOnView() {
        // act
        presenter.viewOnStop();

        // assert
        Mockito.verify(view).setStopButtonVisibility(View.INVISIBLE);
    }

    @Test
    public void viewOnStop_CallsSetPlayButtonVisibilityWithVisibleOnView() {
        // act
        presenter.viewOnStop();

        // assert
        Mockito.verify(view).setPlayButtonVisibility(View.VISIBLE);
    }

    @Test
    public void viewOnStop_CallsStopAudioOnView() {
        // act
        presenter.viewOnStop();

        // assert
        Mockito.verify(view).stopAudio();
    }

}

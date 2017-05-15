package com.example.jlo19.guitartutor.presenters;

import android.view.View;

import com.example.jlo19.guitartutor.presenters.interfaces.ISongPresenter;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;
import com.example.jlo19.guitartutor.views.SongView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Testing SongPresenter
 */
public class SongPresenterTest {

    private ISongPresenter presenter;
    private SongView view;
    private String audioFilename;
    private IAmazonS3Service amazonS3Service;

    @Before
    public void setUp() {
        amazonS3Service = Mockito.mock(IAmazonS3Service.class);
        presenter = new SongPresenter(amazonS3Service);

        view = Mockito.mock(SongView.class);
        audioFilename = "Adventure of a Lifetime.wav";
        Mockito.when(view.getAudioFilename()).thenReturn(audioFilename);
        presenter.setView(view);
    }

    @Test
    public void setsPresenterAsListenerOnService() {
        // assert
        Mockito.verify(amazonS3Service).setUrlListener(presenter);
    }

    @Test
    public void viewOnPlay_CallsGetUrlOnService() {
        // act
        presenter.viewOnPlay();

        // assert
        Mockito.verify(amazonS3Service).getUrl(audioFilename);
    }

    @Test
    public void viewOnPlay_CallsShowProgressBarOnView() {
        // act
        presenter.viewOnPlay();

        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void onUrlDownloadSuccess_CallsPlayAudioOnView() {
        // act
        String url = "url";
        presenter.onUrlDownloadSuccess(url);

        // assert
        Mockito.verify(view).playAudio(url);
    }

    @Test
    public void onUrlDownloadFailed_CallsHideProgressBarOnView() {
        // act
        presenter.onUrlDownloadFailed();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onUrlDownloadFailed_CallsShowErrorOnView() {
        // act
        presenter.onUrlDownloadFailed();

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

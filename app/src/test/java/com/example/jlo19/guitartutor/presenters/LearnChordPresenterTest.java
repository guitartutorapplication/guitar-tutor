package com.example.jlo19.guitartutor.presenters;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.interfaces.ILearnChordModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;
import com.example.jlo19.guitartutor.views.LearnChordView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Testing LearnChordPresenter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class LearnChordPresenterTest {

    private ILearnChordPresenter presenter;
    private LearnChordView view;
    private ILearnChordModel model;
    private Context context;
    private Chord selectedChord;

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new LearnChordPresenter();

        view = Mockito.mock(LearnChordView.class);
        context = Mockito.mock(Context.class);
        Mockito.when(view.getContext()).thenReturn(context);
        selectedChord = new Chord(1, "A", "MAJOR", "A.png", "A.mp4");
        Mockito.when(view.getChord()).thenReturn(selectedChord);
        presenter.setView(view);

        model = Mockito.mock(ILearnChordModel.class);
        ((LearnChordPresenter) presenter).setModel(model);
    }

    @Test
    public void setView_CallsShowProgressBarOnView() {
        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void setModel_setsPresenterOnModel() {
        // assert
        Mockito.verify(model).setPresenter(presenter);
    }

    @Test
    public void setModel_setsContextFromViewOnModel() {
        // assert
        Mockito.verify(model).setContext(context);
    }

    @Test
    public void setModel_getsImageWithChordFromViewOnModel() {
        // assert
        Mockito.verify(model).getImage(selectedChord.getDiagramFilename());
    }

    @Test
    public void viewOnVideoRequested_GetsVideoWithChordFromViewOnModel() {
        // act
        presenter.viewOnVideoRequested();

        // assert
        Mockito.verify(model).getVideo(selectedChord.getVideoFilename());
    }

    @Test
    public void viewOnVideoRequested_ShowsProgressBarOnView() {
        // act
        presenter.viewOnVideoRequested();

        // assert (once for image load and once for video load)
        Mockito.verify(view, Mockito.times(2)).showProgressBar();
    }

    @Test
    public void modelOnImageDownloadFailed_CallsShowErrorOnView() {
        // act
        presenter.modelOnImageDownloadFailed();

        // assert
        Mockito.verify(view).showImageLoadError();
    }

    @Test
    public void modelOnImageDownloadSuccess_CallsHideProgressBarOnView() {
        // act
        presenter.modelOnImageDownloadSuccess(Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888));

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnImageDownloadSuccessBitmap_CallsSetImageOnView() {
        // act
        Bitmap expectedImage = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888);
        presenter.modelOnImageDownloadSuccess(expectedImage);

        // assert
        Mockito.verify(view).setImage(expectedImage);
    }

    @Test
    public void modelOnVideoDownloadSuccess_CallsHideProgressBarOnView() {
        // act
        presenter.modelOnVideoDownloadSuccess("url");

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnVideoDownloadSuccess_CallsPlayVideoOnView() {
        // act
        String expectedUrl = "url";
        presenter.modelOnVideoDownloadSuccess(expectedUrl);

        // assert
        Mockito.verify(view).playVideo(expectedUrl);
    }

    @Test
    public void modelOnVideoDownloadFailed_HidesProgressBarOnView() {
        // act
        presenter.modelOnVideoDownloadFailed();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnVideoDownloadFailed_ShowVideoLoadErrorOnView() {
        // act
        presenter.modelOnVideoDownloadFailed();

        // assert
        Mockito.verify(view).showVideoLoadError();
    }
}

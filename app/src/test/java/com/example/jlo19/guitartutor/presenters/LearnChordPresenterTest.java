package com.example.jlo19.guitartutor.presenters;

import android.graphics.Bitmap;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.interactors.interfaces.IAddUserChordInteractor;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnChordPresenter;
import com.example.jlo19.guitartutor.services.interfaces.IAmazonS3Service;
import com.example.jlo19.guitartutor.views.LearnChordView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Testing LearnChordPresenter
 */
public class LearnChordPresenterTest {

    private ILearnChordPresenter presenter;
    private LearnChordView view;
    private IAddUserChordInteractor addUserChordInteractor;
    private Chord selectedChord;
    private LoggedInUser loggedInUser;
    private IAmazonS3Service amazonS3Service;

    @Before
    public void setUp() {
        loggedInUser = Mockito.mock(LoggedInUser.class);
        amazonS3Service = Mockito.mock(IAmazonS3Service.class);
        addUserChordInteractor = Mockito.mock(IAddUserChordInteractor.class);
        presenter = new LearnChordPresenter(addUserChordInteractor, amazonS3Service, loggedInUser);

        view = Mockito.mock(LearnChordView.class);
        selectedChord = new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1);
        Mockito.when(view.getChord()).thenReturn(selectedChord);
        presenter.setView(view);
    }

    @Test
    public void setView_LearnChordFalse_EnablesLearntButtonOnView() {
        // arrange
        Mockito.when(view.getLearntChord()).thenReturn(false);

        // act
        presenter.setView(view);

        // assert
        Mockito.verify(view, times(2)).enableLearntButton(true);
    }

    @Test
    public void setView_LearnChordTrue_DisablesLearntButtonOnView() {
        // arrange
        Mockito.when(view.getLearntChord()).thenReturn(true);

        // act
        presenter.setView(view);

        // assert
        Mockito.verify(view).enableLearntButton(false);
    }

    @Test
    public void setView_CallsShowProgressBarOnView() {
        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void setsPresenterAsListenerOnInteractor() {
        // assert
        Mockito.verify(addUserChordInteractor).setListener(presenter);
    }

    @Test
    public void getsImageWithChordFromViewOnService() {
        // assert
        Mockito.verify(amazonS3Service).getImage(selectedChord.getDiagramFilename());
    }

    @Test
    public void viewOnVideoRequested_GetsUrlWithChordFromViewOnService() {
        // act
        presenter.viewOnVideoRequested();

        // assert
        Mockito.verify(amazonS3Service).getUrl(selectedChord.getVideoFilename());
    }

    @Test
    public void viewOnVideoRequested_ShowsProgressBarOnView() {
        // act
        presenter.viewOnVideoRequested();

        // assert (once for image load and once for video load)
        Mockito.verify(view, Mockito.times(2)).showProgressBar();
    }

    @Test
    public void onImageDownloadFailed_CallsShowErrorOnView() {
        // act
        presenter.onImageDownloadFailed();

        // assert
        Mockito.verify(view).showImageLoadError();
    }

    @Test
    public void onImageDownloadSuccess_CallsHideProgressBarOnView() {
        // act
        presenter.onImageDownloadSuccess(Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888));

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onImageDownloadSuccessBitmap_CallsSetImageOnView() {
        // act
        Bitmap expectedImage = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888);
        presenter.onImageDownloadSuccess(expectedImage);

        // assert
        Mockito.verify(view).setImage(expectedImage);
    }

    @Test
    public void onUrlDownloadSuccess_CallsHideProgressBarOnView() {
        // act
        presenter.onUrlDownloadSuccess("url");

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onUrlDownloadSuccess_CallsPlayVideoOnView() {
        // act
        String expectedUrl = "url";
        presenter.onUrlDownloadSuccess(expectedUrl);

        // assert
        Mockito.verify(view).playVideo(expectedUrl);
    }

    @Test
    public void onUrlDownloadFailed_HidesProgressBarOnView() {
        // act
        presenter.onUrlDownloadFailed();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onUrlDownloadFailed_ShowVideoLoadErrorOnView() {
        // act
        presenter.onUrlDownloadFailed();

        // assert
        Mockito.verify(view).showVideoLoadError();
    }

    @Test
    public void viewOnLearnt_ShowsLearntConfirmDialogOnView() {
        // act
        presenter.viewOnLearnt();

        // assert
        Mockito.verify(view).showLearntConfirmDialog();
    }

    @Test
    public void viewOnConfirmLearnt_ShowsProgressBarOnView() {
        // so doesn't account for calls to showProgressBar before
        Mockito.reset(view);
        Mockito.when(view.getChord()).thenReturn(selectedChord);

        // act
        presenter.viewOnConfirmLearnt();

        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void viewOnConfirmLearnt_CallsAddUserChordOnInteractor() {
        // act
        presenter.viewOnConfirmLearnt();

        // assert
        Mockito.verify(addUserChordInteractor).addUserChord(loggedInUser.getApiKey(), loggedInUser.getUserId(),
                selectedChord.getId());
    }

    @Test
    public void onChordAdded_CallsHideProgressBarOnView() {
        // act
        presenter.onChordAdded(0, 0);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onChordAddedWithZeroLevelAndAchievements_ShowsAddLearntChordSuccessOnView() {
        // act
        presenter.onChordAdded(0, 0);

        // assert
        Mockito.verify(view).showAddLearntChordSuccess();
    }

    @Test
    public void onChordAddedWithNonZeroAchievements_ShowsAddLearntChordSuccessWithAchievementsOnView() {
        // act
        int achievements = 100;
        presenter.onChordAdded(0, achievements);

        // assert
        Mockito.verify(view).showAddLearntChordSuccess(achievements);
    }

    @Test
    public void onChordAddedWithNonZeroAchievementsAndLevel_ShowsAddLearntChordSuccessWithAchievementsAndLevelOnView() {
        // act
        int achievements = 1000;
        int level = 2;
        presenter.onChordAdded(level, achievements);

        // assert
        Mockito.verify(view).showAddLearntChordSuccess(level, achievements);
    }

    @Test
    public void onAddChordError_CallsHideProgressBarOnView() {
        // act
        presenter.onAddChordError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onAddChordError_ShowsAddLearntChordErrorOnView() {
        // act
        presenter.onAddChordError();

        // assert
        Mockito.verify(view).showAddLearntChordError();
    }

    @Test
    public void viewOnConfirmError_CallFinishActivityOnView() {
        // act
        presenter.viewOnConfirmError();

        // assert
        Mockito.verify(view).finishActivity();
    }

    @Test
    public void viewOnConfirmLearntSuccess_CallStartLearnAllChordsActivityOnView() {
        // act
        presenter.viewOnConfirmLearntSuccess();

        // assert
        Mockito.verify(view).startLearnAllChordsActivity();
    }

    @Test
    public void viewOnHelpRequested_CallsStartDiagramHelpActivityOnView() {
        // act
        presenter.viewOnHelpRequested();

        // assert
        Mockito.verify(view).startDiagramHelpActivity();
    }
}

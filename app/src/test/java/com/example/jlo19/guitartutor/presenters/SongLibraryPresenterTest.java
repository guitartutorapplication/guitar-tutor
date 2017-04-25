package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.models.interfaces.IGetSongsInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongLibraryPresenter;
import com.example.jlo19.guitartutor.views.SongLibraryView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Testing SongLibraryPresenter
 */
public class SongLibraryPresenterTest {

    private ISongLibraryPresenter presenter;
    private SongLibraryView view;
    private IGetSongsInteractor getSongsInteractor;
    private LoggedInUser loggedInUser;

    @Before
    public void setUp() {
        loggedInUser = Mockito.mock(LoggedInUser.class);
        getSongsInteractor = Mockito.mock(IGetSongsInteractor.class);
        presenter = new SongLibraryPresenter(getSongsInteractor, loggedInUser);

        view = Mockito.mock(SongLibraryView.class);
        presenter.setView(view);
    }

    @Test
    public void setsPresenterAsListenerOnInteractor() {
        // assert
        Mockito.verify(getSongsInteractor).setListener(presenter);
    }

    @Test
    public void setView_CallsGetAllSongsOnInteractor() {
        // assert
        Mockito.verify(getSongsInteractor).getAllSongs(loggedInUser.getApiKey());
    }

    @Test
    public void setView_CallsShowProgressBarOnView() {
        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void onSongsRetrieved_HidesProgressBarOnView() {
        // act
        presenter.onSongsRetrieved(null);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onSongsRetrieved_SetsSongsOnView() {
        // act
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
        List<Song> expectedSongs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "Adventure of a Lifetime.wav",
                        "contents", chords),
                new Song("Dance with Me Tonight", "Olly Murs", "Dance with me Tonight.wav",
                        "contents", chords));
        presenter.onSongsRetrieved(expectedSongs);

        // assert
        Mockito.verify(view).setSongs(expectedSongs);
    }

    @Test
    public void onError_HidesProgressBarOnView() {
        // act
        presenter.onError();

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
    public void viewOnSongFilterChangedWithTrue_CallsGetAllSongsOnModel() {

        // act
        presenter.viewOnSongFilterChanged(true);

        // assert
        Mockito.verify(getSongsInteractor, times(2)).getAllSongs(loggedInUser.getApiKey());
    }

    @Test
    public void viewOnSongFilterChangedWithFalse_CallsGetSongsUserCanPlayOnModel() {
        // act
        presenter.viewOnSongFilterChanged(false);

        // assert
        Mockito.verify(getSongsInteractor).getSongsUserCanPlay(loggedInUser.getApiKey(),
                loggedInUser.getUserId());
    }

    @Test
    public void viewOnExit_CallsResetSongsOnModel() {
        // act
        presenter.viewOnExit();

        // assert
        Mockito.verify(getSongsInteractor).resetSongs();
    }

    @Test
    public void viewOnConfirmError_CallFinishActivityOnView() {
        // act
        presenter.viewOnConfirmError();

        // assert
        Mockito.verify(view).finishActivity();
    }
}

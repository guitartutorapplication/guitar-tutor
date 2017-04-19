package com.example.jlo19.guitartutor.presenters;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.interfaces.ISongLibraryModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongLibraryPresenter;
import com.example.jlo19.guitartutor.views.SongLibraryView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Testing SongLibraryPresenter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class SongLibraryPresenterTest {

    private ISongLibraryPresenter presenter;
    private SongLibraryView view;
    private ISongLibraryModel model;
    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new SongLibraryPresenter();

        sharedPreferences = Mockito.mock(SharedPreferences.class);
        presenter.setSharedPreferences(sharedPreferences);

        model = Mockito.mock(ISongLibraryModel.class);
        ((SongLibraryPresenter) presenter).setModel(model);

        view = Mockito.mock(SongLibraryView.class);
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
    public void setModel_CallsGetAllSongsOnModel() {
        // assert
        Mockito.verify(model).getAllSongs();
    }

    @Test
    public void setView_CallsShowProgressBarOnView() {
        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void modelOnSongsRetrieved_HidesProgressBarOnView() {
        // act
        presenter.modelOnSongsRetrieved(null);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void modelOnSongsRetrieved_SetsSongsOnView() {
        // act
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
        List<Song> expectedSongs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "Adventure of a Lifetime.wav",
                        "contents", chords),
                new Song("Dance with Me Tonight", "Olly Murs", "Dance with me Tonight.wav",
                        "contents", chords));
        presenter.modelOnSongsRetrieved(expectedSongs);

        // assert
        Mockito.verify(view).setSongs(expectedSongs);
    }

    @Test
    public void modelOnError_HidesProgressBarOnView() {
        // act
        presenter.modelOnError();

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
    public void viewOnSongFilterChangedWithTrue_CallsGetAllSongsOnModel() {

        // act
        presenter.viewOnSongFilterChanged(true);

        // assert
        Mockito.verify(model, times(2)).getAllSongs();
    }

    @Test
    public void viewOnSongFilterChangedWithFalse_CallsGetSongsUserCanPlayOnModel() {
        // act
        presenter.viewOnSongFilterChanged(false);

        // assert
        Mockito.verify(model).getSongsUserCanPlay();
    }

    @Test
    public void viewOnExit_CallsResetSongsOnModel() {
        // act
        presenter.viewOnExit();

        // assert
        Mockito.verify(model).resetSongs();
    }
}

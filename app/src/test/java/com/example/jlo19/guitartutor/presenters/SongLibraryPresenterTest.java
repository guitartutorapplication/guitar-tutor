package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.interfaces.ISongLibraryModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.models.retrofit.Song;
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

/**
 * Testing SongLibraryPresenter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class SongLibraryPresenterTest {

    private ISongLibraryPresenter presenter;
    private SongLibraryView view;
    private ISongLibraryModel model;

    @Before
    public void setUp() {
        // stop real injection of model
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        presenter = new SongLibraryPresenter();

        model = Mockito.mock(ISongLibraryModel.class);
        ((SongLibraryPresenter) presenter).setModel(model);

        view = Mockito.mock(SongLibraryView.class);
        presenter.setView(view);
    }

    @Test
    public void setView_CallsSetToolbarTitleTextOnView() {
        // assert
        Mockito.verify(view).setToolbarTitleText();
    }

    @Test
    public void setModel_SetsPresenterOnModel() {
        // assert
        Mockito.verify(model).setPresenter(presenter);
    }

    @Test
    public void setView_CallsGetSongsOnModel() {
        // assert
        Mockito.verify(model).getSongs();
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
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
        List<Song> expectedSongs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "contents", chords),
                new Song("Dance with Me Tonight", "Olly Murs", "contents", chords));
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
}

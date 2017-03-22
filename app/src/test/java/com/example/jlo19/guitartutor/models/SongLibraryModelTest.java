package com.example.jlo19.guitartutor.models;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeSongsResponseCall;
import com.example.jlo19.guitartutor.models.interfaces.ISongLibraryModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.models.retrofit.Song;
import com.example.jlo19.guitartutor.models.retrofit.SongsResponse;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongLibraryPresenter;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import retrofit2.Response;

/**
 * Testing SongLibraryModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, Response.class})
public class SongLibraryModelTest {

    private ISongLibraryModel model;
    private ISongLibraryPresenter presenter;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new SongLibraryModel();

        presenter = PowerMockito.mock(ISongLibraryPresenter.class);
        model.setPresenter(presenter);
    }

    @Test
    public void getSongs_OnResponse_CallsSongsRetrievedOnPresenterWithSongs() {
        // arrange
        // sets fake call with a response with songs
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
        List<Song> songs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "contents", chords),
                new Song("Dance with Me Tonight", "Olly Murs", "contents", chords));
        SongsResponse songsResponse = new SongsResponse(false, songs);

        Response<SongsResponse> response = (Response<SongsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.body()).thenReturn(songsResponse);

        DatabaseApi api = new FakeDatabaseApi(new FakeSongsResponseCall(response));
        ((SongLibraryModel) model).setApi(api);

        // act
        model.getSongs();

        // assert
        Mockito.verify(presenter).modelOnSongsRetrieved(songs);
    }

    @Test
    public void getSongs_OnFailure_CallsErrorOnPresenter() {
        // arrange
        // sets fake call with no response (failure)
        FakeSongsResponseCall call = new FakeSongsResponseCall(null);
        DatabaseApi api = new FakeDatabaseApi(call);
        ((SongLibraryModel) model).setApi(api);

        // act
        model.getSongs();

        // assert
        Mockito.verify(presenter).modelOnError();
    }

}

package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.helpers.FakeChordsCall;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.helpers.FakeSongsCall;
import com.example.jlo19.guitartutor.models.interfaces.ISongLibraryModel;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;
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
import java.util.Collections;
import java.util.List;

import retrofit2.Response;

import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Testing SongLibraryModel
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, Response.class})
public class SongLibraryModelTest {

    private ISongLibraryModel model;
    private ISongLibraryPresenter presenter;
    private int userId;
    private List<Song> songs;
    private List<Chord> userChords;

    @Before
    public void setUp() {
        // stop real injection of API
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(PowerMockito.mock(AppComponent.class));

        model = new SongLibraryModel();

        userId = 1;
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        Mockito.when(sharedPreferences.getInt("user_id", 0)).thenReturn(userId);
        model.setSharedPreferences(sharedPreferences);

        presenter = PowerMockito.mock(ISongLibraryPresenter.class);
        model.setPresenter(presenter);

        userChords = Arrays.asList(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
        songs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "contents", Arrays.asList(
                        new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                        new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1))),
                new Song("Dance with Me Tonight", "Olly Murs", "contents", userChords));
    }

    @Test
    public void getAllSongs_CallsGetSongsOnApi() {
        // arrange
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsCall(null)));
        ((SongLibraryModel) model).setApi(api);

        // act
        model.getAllSongs();

        // assert
        Mockito.verify(api).getSongs();
    }

    @Test
    public void getAllSongs_OnSuccessfulResponse_CallsSongsRetrievedOnPresenterWithSongs() {
        // arrange
        Response<List<Song>> response = FakeResponseCreator.getSongsResponse(true, songs);
        DatabaseApi api = new FakeDatabaseApi(new FakeSongsCall(response));
        ((SongLibraryModel) model).setApi(api);

        // act
        model.getAllSongs();

        // assert
        Mockito.verify(presenter).modelOnSongsRetrieved(songs);
    }

    @Test
    public void getAllSongs_OnUnsuccessfulResponse_CallsErrorOnPresenter() {
        // arrange
        Response<List<Song>> response = FakeResponseCreator.getSongsResponse(false, null);
        DatabaseApi api = new FakeDatabaseApi(new FakeSongsCall(response));
        ((SongLibraryModel) model).setApi(api);

        // act
        model.getAllSongs();

        // assert
        Mockito.verify(presenter).modelOnError();
    }

    @Test
    public void getAllSongs_OnFailure_CallsErrorOnPresenter() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        FakeSongsCall call = new FakeSongsCall(null);
        DatabaseApi api = new FakeDatabaseApi(call);
        ((SongLibraryModel) model).setApi(api);

        // act
        model.getAllSongs();

        // assert
        Mockito.verify(presenter).modelOnError();
    }

    @Test
    public void getAllSongsWithResponse_GetAllSongs_DoesntCallApiASecondTime() {
        // arrange
        Response<List<Song>> response = FakeResponseCreator.getSongsResponse(true, songs);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsCall(response)));
        ((SongLibraryModel) model).setApi(api);
        model.getAllSongs();

        // act
        model.getAllSongs();

        // assert
        Mockito.verify(presenter, times(2)).modelOnSongsRetrieved(songs);
        Mockito.verify(api, times(1)).getSongs();
    }

    @Test
    public void getAllSongsWithResponse_ResetSongs_GetAllSongs_CallsApiASecondTime() {
        // arrange
        Response<List<Song>> response = FakeResponseCreator.getSongsResponse(true, songs);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsCall(response)));
        ((SongLibraryModel) model).setApi(api);
        model.getAllSongs();
        model.resetSongs();

        // act
        model.getAllSongs();

        // assert
        Mockito.verify(presenter, times(2)).modelOnSongsRetrieved(songs);
        Mockito.verify(api, times(2)).getSongs();
    }

    @Test
    public void getSongsUserCanPlay_CallsGetUserChordsOnApiWithIdFromSharedPreferences() {
        // arrange
        Response<List<Song>> response = FakeResponseCreator.getSongsResponse(true, songs);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsCall(response),
                new FakeChordsCall(null)));
        ((SongLibraryModel) model).setApi(api);
        model.getAllSongs();

        // act
        model.getSongsUserCanPlay();

        // assert
        Mockito.verify(api).getUserChords(userId);
    }

    @Test
    public void getSongsUserCanPlay_OnSuccessfulResponse_CallsSongsRetrievedOnPresenterWithSongs() {
        // arrange
        Response<List<Song>> songsResponse = FakeResponseCreator.getSongsResponse(true, songs);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(true,
                userChords);
        DatabaseApi api = new FakeDatabaseApi(new FakeSongsCall(songsResponse),
                new FakeChordsCall(userChordsResponse));
        ((SongLibraryModel) model).setApi(api);
        model.getAllSongs();

        // act
        model.getSongsUserCanPlay();

        // assert
        Mockito.verify(presenter).modelOnSongsRetrieved(Collections.singletonList(songs.get(1)));
    }

    @Test
    public void getSongsUserCanPlay_OnUnsuccessfulResponse_CallsErrorOnPresenter() {
        // arrange
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(false,
                null);
        DatabaseApi api = new FakeDatabaseApi(new FakeChordsCall(userChordsResponse));
        ((SongLibraryModel) model).setApi(api);

        // act
        model.getSongsUserCanPlay();

        // assert
        Mockito.verify(presenter).modelOnError();
    }

    @Test
    public void getSongsUserCanPlay_OnFailure_CallsErrorOnPresenter() {
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = new FakeDatabaseApi(new FakeChordsCall(null));
        ((SongLibraryModel) model).setApi(api);

        // act
        model.getSongsUserCanPlay();

        // assert
        Mockito.verify(presenter).modelOnError();
    }

    @Test
    public void getSongsUserCanPlay_GetSongsUserCanPlay_DoesntCallApiSecondTime() {
        // arrange
        // sets fake call with a response with songs
        Response<List<Song>> songsResponse = FakeResponseCreator.getSongsResponse(true, songs);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(true,
                userChords);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsCall(songsResponse),
                new FakeChordsCall(userChordsResponse)));
        ((SongLibraryModel) model).setApi(api);

        model.getAllSongs();
        model.getSongsUserCanPlay();

        // act
        model.getSongsUserCanPlay();

        // assert
        Mockito.verify(api, times(1)).getUserChords(userId);
        Mockito.verify(presenter, times(2)).modelOnSongsRetrieved(Collections.singletonList(songs.get(1)));
    }

    @Test
    public void getSongsUserCanPlay_ResetSongs_GetSongsUserCanPlay_CallsApiASecondTime() {
        // arrange
        Response<List<Song>> songsResponse = FakeResponseCreator.getSongsResponse(true, songs);
        Response<List<Chord>> userChordsResponse = FakeResponseCreator.getChordsResponse(true,
                userChords);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsCall(songsResponse),
                new FakeChordsCall(userChordsResponse)));
        ((SongLibraryModel) model).setApi(api);

        model.getAllSongs();
        model.getSongsUserCanPlay();
        model.resetSongs();
        model.getAllSongs();

        // act
        model.getSongsUserCanPlay();

        // assert
        Mockito.verify(api, times(2)).getUserChords(userId);
        Mockito.verify(presenter, times(2)).modelOnSongsRetrieved(Collections.singletonList(songs.get(1)));
    }
}

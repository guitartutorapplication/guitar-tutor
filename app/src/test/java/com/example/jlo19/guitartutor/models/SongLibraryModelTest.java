package com.example.jlo19.guitartutor.models;

import android.content.SharedPreferences;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeSongsResponseCall;
import com.example.jlo19.guitartutor.helpers.FakeUserChordsResponseCall;
import com.example.jlo19.guitartutor.models.interfaces.ISongLibraryModel;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.models.retrofit.Song;
import com.example.jlo19.guitartutor.models.retrofit.SongsResponse;
import com.example.jlo19.guitartutor.models.retrofit.UserChord;
import com.example.jlo19.guitartutor.models.retrofit.UserChordsResponse;
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
    }

    @Test
    public void getSongs_OnResponse_CallsSongsRetrievedOnPresenterWithSongs() {
        // arrange
        // sets fake call with a response with songs
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", 1));
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
        model.getAllSongs();

        // assert
        Mockito.verify(presenter).modelOnSongsRetrieved(songs);
    }

    @Test
    public void getSongsWithResponse_GetSongs_DoesntCallApiASecondTime() {
        // arrange
        // sets fake call with a response with songs
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", 1));
        List<Song> songs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "contents", chords),
                new Song("Dance with Me Tonight", "Olly Murs", "contents", chords));
        SongsResponse songsResponse = new SongsResponse(false, songs);

        Response<SongsResponse> response = (Response<SongsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.body()).thenReturn(songsResponse);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsResponseCall(response)));
        ((SongLibraryModel) model).setApi(api);
        model.getAllSongs();

        // act
        model.getAllSongs();

        // assert
        Mockito.verify(presenter, times(2)).modelOnSongsRetrieved(songs);
        Mockito.verify(api, times(1)).getSongs();
    }

    @Test
    public void getSongsWithResponse_ResetSongs_GetSongs_CallsApiASecondTime() {
        // arrange
        // sets fake call with a response with songs
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", 1));
        List<Song> songs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "contents", chords),
                new Song("Dance with Me Tonight", "Olly Murs", "contents", chords));
        SongsResponse songsResponse = new SongsResponse(false, songs);

        Response<SongsResponse> response = (Response<SongsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.body()).thenReturn(songsResponse);

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsResponseCall(response)));
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
    public void getSongs_OnFailure_CallsErrorOnPresenter() {
        // arrange
        // sets fake call with no response (failure)
        FakeSongsResponseCall call = new FakeSongsResponseCall(null);
        DatabaseApi api = new FakeDatabaseApi(call);
        ((SongLibraryModel) model).setApi(api);

        // act
        model.getAllSongs();

        // assert
        Mockito.verify(presenter).modelOnError();
    }

    @Test
    public void getSongsUserCanPlay_CallsGetUserChordsOnApiWithIdFromSharedPreferences() {
        // arrange
        // sets fake call with a response with songs
        List<Song> songs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "contents", Arrays.asList(
                        new Chord(1, "A", "MAJOR", "A.png", "A.mp4", 1),
                        new Chord(2, "B", "MAJOR", "B.png", "B.mp4", 1))),
                new Song("Dance with Me Tonight", "Olly Murs", "contents", Arrays.asList(
                        new Chord(3, "C", "MAJOR", "C.png", "C.mp4", 1),
                        new Chord(2, "B", "MAJOR", "B.png", "B.mp4", 1))));
        Response<SongsResponse> songsResponse = (Response<SongsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(songsResponse.body()).thenReturn(new SongsResponse(false, songs));

        // sets fake call with user chords
        List<UserChord> userChords = Arrays.asList(new UserChord(1), new UserChord(2));
        Response<UserChordsResponse> userChordsResponse = (Response<UserChordsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(userChordsResponse.body()).thenReturn(new UserChordsResponse(userChords));

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsResponseCall(songsResponse),
                new FakeUserChordsResponseCall(userChordsResponse)));
        ((SongLibraryModel) model).setApi(api);
        model.getAllSongs();

        // act
        model.getSongsUserCanPlay();

        // assert
        Mockito.verify(api).getUserChords(userId);
    }

    @Test
    public void getSongsUserCanPlay_OnResponse_CallsSongsRetrievedOnPresenterWithSongs() {
        // arrange
        // sets fake call with a response with songs
        List<Song> songs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "contents", Arrays.asList(
                        new Chord(1, "A", "MAJOR", "A.png", "A.mp4", 1),
                        new Chord(2, "B", "MAJOR", "B.png", "B.mp4", 1))),
                new Song("Dance with Me Tonight", "Olly Murs", "contents", Arrays.asList(
                        new Chord(3, "C", "MAJOR", "C.png", "C.mp4", 1),
                        new Chord(2, "B", "MAJOR", "B.png", "B.mp4", 1))));
        Response<SongsResponse> songsResponse = (Response<SongsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(songsResponse.body()).thenReturn(new SongsResponse(false, songs));

        // sets fake call with user chords
        List<UserChord> userChords = Arrays.asList(new UserChord(1), new UserChord(2));
        Response<UserChordsResponse> userChordsResponse = (Response<UserChordsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(userChordsResponse.body()).thenReturn(new UserChordsResponse(userChords));

        DatabaseApi api = new FakeDatabaseApi(new FakeSongsResponseCall(songsResponse),
                new FakeUserChordsResponseCall(userChordsResponse));
        ((SongLibraryModel) model).setApi(api);
        model.getAllSongs();

        // act
        model.getSongsUserCanPlay();

        // assert
        Mockito.verify(presenter).modelOnSongsRetrieved(Collections.singletonList(songs.get(0)));
    }

    @Test
    public void getSongsUserCanPlay_GetSongsUserCanPlay_DoesntCallApiSecondTime() {
        // arrange
        // sets fake call with a response with songs
        List<Song> songs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "contents", Arrays.asList(
                        new Chord(1, "A", "MAJOR", "A.png", "A.mp4", 1),
                        new Chord(2, "B", "MAJOR", "B.png", "B.mp4", 1))),
                new Song("Dance with Me Tonight", "Olly Murs", "contents", Arrays.asList(
                        new Chord(3, "C", "MAJOR", "C.png", "C.mp4", 1),
                        new Chord(2, "B", "MAJOR", "B.png", "B.mp4", 1))));
        Response<SongsResponse> songsResponse = (Response<SongsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(songsResponse.body()).thenReturn(new SongsResponse(false, songs));

        // sets fake call with user chords
        List<UserChord> userChords = Arrays.asList(new UserChord(1), new UserChord(2));
        Response<UserChordsResponse> userChordsResponse = (Response<UserChordsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(userChordsResponse.body()).thenReturn(new UserChordsResponse(userChords));

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsResponseCall(songsResponse),
                new FakeUserChordsResponseCall(userChordsResponse)));
        ((SongLibraryModel) model).setApi(api);

        model.getAllSongs();
        model.getSongsUserCanPlay();

        // act
        model.getSongsUserCanPlay();

        // assert
        Mockito.verify(api, times(1)).getUserChords(userId);
        Mockito.verify(presenter, times(2)).modelOnSongsRetrieved(Collections.singletonList(songs.get(0)));
    }

    @Test
    public void getSongsUserCanPlay_ResetSongs_GetSongsUserCanPlay_CallsApiASecondTime() {
        // arrange
        // sets fake call with a response with songs
        List<Song> songs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "contents", Arrays.asList(
                        new Chord(1, "A", "MAJOR", "A.png", "A.mp4", 1),
                        new Chord(2, "B", "MAJOR", "B.png", "B.mp4", 1))),
                new Song("Dance with Me Tonight", "Olly Murs", "contents", Arrays.asList(
                        new Chord(3, "C", "MAJOR", "C.png", "C.mp4", 1),
                        new Chord(2, "B", "MAJOR", "B.png", "B.mp4", 1))));
        Response<SongsResponse> songsResponse = (Response<SongsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(songsResponse.body()).thenReturn(new SongsResponse(false, songs));

        // sets fake call with user chords
        List<UserChord> userChords = Arrays.asList(new UserChord(1), new UserChord(2));
        Response<UserChordsResponse> userChordsResponse = (Response<UserChordsResponse>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(userChordsResponse.body()).thenReturn(new UserChordsResponse(userChords));

        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsResponseCall(songsResponse),
                new FakeUserChordsResponseCall(userChordsResponse)));
        ((SongLibraryModel) model).setApi(api);

        model.getAllSongs();
        model.getSongsUserCanPlay();
        model.resetSongs();
        model.getAllSongs();

        // act
        model.getSongsUserCanPlay();

        // assert
        Mockito.verify(api, times(2)).getUserChords(userId);
        Mockito.verify(presenter, times(2)).modelOnSongsRetrieved(Collections.singletonList(songs.get(0)));
    }

    @Test
    public void getSongsUserCanPlay_OnFailure_CallsErrorOnPresenter() {
        // sets fake call with no response (failure)
        DatabaseApi api = new FakeDatabaseApi(new FakeUserChordsResponseCall(null));
        ((SongLibraryModel) model).setApi(api);

        // act
        model.getSongsUserCanPlay();

        // assert
        Mockito.verify(presenter).modelOnError();
    }

}

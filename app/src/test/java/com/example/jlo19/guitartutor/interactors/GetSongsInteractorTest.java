package com.example.jlo19.guitartutor.interactors;

import com.example.jlo19.guitartutor.helpers.FakeDatabaseApi;
import com.example.jlo19.guitartutor.helpers.FakeResponseCreator;
import com.example.jlo19.guitartutor.helpers.FakeSongsCall;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetSongsInteractor;
import com.example.jlo19.guitartutor.interactors.interfaces.IGetUserChordsInteractor;
import com.example.jlo19.guitartutor.listeners.GetSongsListener;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.models.Song;
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

import static org.mockito.Mockito.never;

/**
 * Testing GetSongsInteractor
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Response.class})
public class GetSongsInteractorTest {

    private GetSongsListener listener;
    private List<Song> songs;
    private List<Chord> userChords;
    private IGetUserChordsInteractor getUserChordsInteractor;

    @Before
    public void setUp() {
        getUserChordsInteractor = PowerMockito.mock(IGetUserChordsInteractor.class);
        listener = PowerMockito.mock(GetSongsListener.class);

        userChords = Arrays.asList(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
        songs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "Adventure of a Lifetime.wav",
                        "contents", Arrays.asList(
                        new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                        new Chord(3, "C", "MAJOR", "C.png", "C.mp4", "C.wav", 1))),
                new Song("Dance with Me Tonight", "Olly Murs", "Dance with me Tonight.wav",
                        "contents", userChords));
    }

    @Test
    public void getAllSongs_CallsGetSongsOnApi() {
        // arrange
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsCall(null)));
        IGetSongsInteractor getSongsInteractor = new GetSongsInteractor(api, getUserChordsInteractor);
        getSongsInteractor.setListener(listener);

        // act
        String apiKey = "api_key";
        getSongsInteractor.getAllSongs(apiKey);

        // assert
        Mockito.verify(api).getSongs(apiKey);
    }

    @Test
    public void getAllSongs_OnSuccessfulResponse_CallsSongsRetrievedOnListener() {
        // arrange
        // sets up API with songs response that is successful
        Response<List<Song>> response = FakeResponseCreator.getSongsResponse(true, songs);
        DatabaseApi api = new FakeDatabaseApi(new FakeSongsCall(response));

        IGetSongsInteractor getSongsInteractor = new GetSongsInteractor(api, getUserChordsInteractor);
        getSongsInteractor.setListener(listener);

        // act
        getSongsInteractor.getAllSongs("api_key");

        // assert
        Mockito.verify(listener).onSongsRetrieved(songs);
    }

    @Test
    public void getAllSongs_OnUnsuccessfulResponse_CallsErrorOnListener() {
        // arrange
        // sets up API with songs response that is unsuccessful
        Response<List<Song>> response = FakeResponseCreator.getSongsResponse(false, null);
        DatabaseApi api = new FakeDatabaseApi(new FakeSongsCall(response));

        IGetSongsInteractor getSongsInteractor = new GetSongsInteractor(api, getUserChordsInteractor);
        getSongsInteractor.setListener(listener);

        // act
        getSongsInteractor.getAllSongs("api_key");

        // assert
        Mockito.verify(listener).onError();
    }

    @Test
    public void getAllSongs_OnFailure_CallsErrorOnListener() {
        // arrange
        // on failure is triggered when a null response is passed through to fake call
        DatabaseApi api = new FakeDatabaseApi(new FakeSongsCall(null));
        IGetSongsInteractor getSongsInteractor = new GetSongsInteractor(api, getUserChordsInteractor);
        getSongsInteractor.setListener(listener);

        // act
        getSongsInteractor.getAllSongs("api_key");

        // assert
        Mockito.verify(listener).onError();
    }

    @Test
    public void getAllSongsWithResponse_GetAllSongs_DoesntCallApiASecondTime() {
        // arrange
        Response<List<Song>> response = FakeResponseCreator.getSongsResponse(true, songs);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsCall(response)));

        IGetSongsInteractor getSongsInteractor = new GetSongsInteractor(api, getUserChordsInteractor);
        getSongsInteractor.setListener(listener);

        String apiKey = "api_key";
        getSongsInteractor.getAllSongs(apiKey);
        // so doesn't account for calls before second getAllSongs when verifying
        Mockito.reset(listener);
        Mockito.reset(api);

        // act
        getSongsInteractor.getAllSongs(apiKey);

        // assert
        Mockito.verify(listener).onSongsRetrieved(songs);
        Mockito.verify(api, never()).getSongs(apiKey);
    }

    @Test
    public void getAllSongsWithResponse_ResetSongs_GetAllSongs_CallsApiASecondTime() {
        // arrange
        Response<List<Song>> response = FakeResponseCreator.getSongsResponse(true, songs);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsCall(response)));

        IGetSongsInteractor getSongsInteractor = new GetSongsInteractor(api, getUserChordsInteractor);
        getSongsInteractor.setListener(listener);

        String apiKey = "api_key";
        getSongsInteractor.getAllSongs(apiKey);
        getSongsInteractor.resetSongs();
        // so doesn't account for calls before second getAllSongs when verifying
        Mockito.reset(listener);
        Mockito.reset(api);

        // act
        getSongsInteractor.getAllSongs(apiKey);

        // assert
        Mockito.verify(listener).onSongsRetrieved(songs);
        Mockito.verify(api).getSongs(apiKey);
    }

    @Test
    public void getSongsUserCanPlay_CallsGetUserChordsOnGetUserChordsInteractor() {
        // arrange
        // sets up API with songs response that is successful
        Response<List<Song>> response = FakeResponseCreator.getSongsResponse(true, songs);
        DatabaseApi api = Mockito.spy(new FakeDatabaseApi(new FakeSongsCall(response)));

        IGetSongsInteractor getSongsInteractor = new GetSongsInteractor(api, getUserChordsInteractor);
        getSongsInteractor.setListener(listener);

        // act
        String apiKey = "api_key";
        int userId = 2;
        getSongsInteractor.getSongsUserCanPlay(apiKey, userId);

        // assert
        Mockito.verify(getUserChordsInteractor).getUserChords(apiKey, userId);
    }

    @Test
    public void getAllSongsWithSuccessfulResponse_OnUserChordsRetrieved_CallsSongsRetrievedOnListener() {
        // arrange
        // sets up API with songs response that is successful
        Response<List<Song>> songsResponse = FakeResponseCreator.getSongsResponse(true, songs);
        DatabaseApi api = new FakeDatabaseApi(new FakeSongsCall(songsResponse));

        IGetSongsInteractor getSongsInteractor = new GetSongsInteractor(api, getUserChordsInteractor);
        getSongsInteractor.setListener(listener);
        getSongsInteractor.getAllSongs("api_key");

        // act
        getSongsInteractor.onUserChordsRetrieved(userChords);

        // assert
        Mockito.verify(listener).onSongsRetrieved(Collections.singletonList(songs.get(1)));
    }

    @Test
    public void onError_CallsErrorOnListener() {
        // arrange
        IGetSongsInteractor getSongsInteractor = new GetSongsInteractor(Mockito.mock(DatabaseApi.class),
                getUserChordsInteractor);
        getSongsInteractor.setListener(listener);

        // act
        getSongsInteractor.onGetUserChordsError();

        // assert
        Mockito.verify(listener).onError();
    }
}

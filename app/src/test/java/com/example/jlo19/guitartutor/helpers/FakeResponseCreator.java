package com.example.jlo19.guitartutor.helpers;

import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;
import com.example.jlo19.guitartutor.models.retrofit.objects.User;
import com.example.jlo19.guitartutor.models.retrofit.responses.ResponseWithMessage;
import com.google.gson.Gson;

import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Helper to create mocked responses for API calls
 */
public class FakeResponseCreator {

    public static Response<List<Chord>> getChordsResponse(boolean isSuccess, List<Chord> chords) {
        Response<List<Chord>> chordsResponse = (Response<List<Chord>>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(chordsResponse.isSuccessful()).thenReturn(isSuccess);

        if (isSuccess) {
            PowerMockito.when(chordsResponse.body()).thenReturn(chords);
        }
        return chordsResponse;
    }

    public static Response<User> getUserResponse(boolean isSuccess, User user) {
        Response<User> userResponse = (Response<User>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(userResponse.isSuccessful()).thenReturn(isSuccess);

        if (isSuccess) {
            PowerMockito.when(userResponse.body()).thenReturn(user);
        }
        return userResponse;
    }

    public static Response<List<Song>> getSongsResponse(boolean isSuccess, List<Song> songs) {
        Response<List<Song>> songsResponse = (Response<List<Song>>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(songsResponse.isSuccessful()).thenReturn(isSuccess);

        if (isSuccess) {
            PowerMockito.when(songsResponse.body()).thenReturn(songs);
        }
        return songsResponse;
    }

    public static Response<ResponseWithMessage> getResponseWithMessage(boolean isSuccess, String
            responseErrorMessage) throws IOException {
        Response<ResponseWithMessage> response = (Response<ResponseWithMessage>)
                PowerMockito.mock(Response.class);
        PowerMockito.when(response.isSuccessful()).thenReturn(isSuccess);

        if (!isSuccess) {
            ResponseBody errorBody = PowerMockito.mock(ResponseBody.class);
            PowerMockito.when(errorBody.string()).thenReturn(new Gson().toJson(new ResponseWithMessage(
                    responseErrorMessage)));
            PowerMockito.when(response.errorBody()).thenReturn(errorBody);
        }
        return response;
    }
}

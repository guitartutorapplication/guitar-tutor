package com.example.jlo19.guitartutor.models.interfaces;

import com.example.jlo19.guitartutor.listeners.GetSongsListener;
import com.example.jlo19.guitartutor.listeners.GetUserChordsListener;

/**
 * Interface for GetSongsInteractor
 */
public interface IGetSongsInteractor extends GetUserChordsListener {
    void setListener(GetSongsListener listener);
    void getAllSongs(String apiKey);
    void getSongsUserCanPlay(String apiKey, int userId);
    void resetSongs();
}

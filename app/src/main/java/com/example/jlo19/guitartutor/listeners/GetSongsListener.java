package com.example.jlo19.guitartutor.listeners;

import com.example.jlo19.guitartutor.models.Song;

import java.util.List;

/**
 * Listener for GetSongsInteractor
 */
public interface GetSongsListener {
    void onSongsRetrieved(List<Song> songs);
    void onError();
}

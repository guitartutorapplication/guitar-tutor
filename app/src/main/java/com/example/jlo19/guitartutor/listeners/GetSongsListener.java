package com.example.jlo19.guitartutor.listeners;

import com.example.jlo19.guitartutor.models.retrofit.objects.Song;

import java.util.List;

public interface GetSongsListener {
    void onSongsRetrieved(List<Song> songs);
    void onError();
}

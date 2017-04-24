package com.example.jlo19.guitartutor.listeners;

import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;

import java.util.List;


public interface GetUserChordsListener {
    void onUserChordsRetrieved(List<Chord> chords);
    void onError();
}

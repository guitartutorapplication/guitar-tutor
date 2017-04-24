package com.example.jlo19.guitartutor.listeners;

import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;

import java.util.List;

public interface GetChordsListener {
    void onChordsAndDetailsRetrieved(List<Chord> allChords, int userLevel, List<Integer> userChords);
    void onError();
}

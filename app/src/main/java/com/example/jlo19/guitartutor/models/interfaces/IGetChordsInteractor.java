package com.example.jlo19.guitartutor.models.interfaces;

import com.example.jlo19.guitartutor.listeners.GetAccountDetailsListener;
import com.example.jlo19.guitartutor.listeners.GetChordsListener;
import com.example.jlo19.guitartutor.listeners.GetUserChordsListener;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;

import java.util.List;

/**
 * Interface for GetChordsInteractor
 */
public interface IGetChordsInteractor extends GetUserChordsListener, GetAccountDetailsListener {
    void setListener(GetChordsListener listener);
    void getChordsAndDetails(String apiKey, int userId);
    List<Chord> getAllChords();
    List<Integer> getUserChordIds();
}

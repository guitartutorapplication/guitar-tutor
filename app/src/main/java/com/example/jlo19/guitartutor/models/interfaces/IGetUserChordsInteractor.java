package com.example.jlo19.guitartutor.models.interfaces;

import com.example.jlo19.guitartutor.listeners.GetUserChordsListener;

/**
 * Interface for GetUserChordsInteractor
 */
public interface IGetUserChordsInteractor {
    void setListener(GetUserChordsListener listener);
    void getUserChords(String apiKey, int userId);
}

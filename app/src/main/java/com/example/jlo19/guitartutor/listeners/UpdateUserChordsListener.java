package com.example.jlo19.guitartutor.listeners;

/**
 * Listener for UpdateUserChordsInteractor
 */
public interface UpdateUserChordsListener {
    void onUpdateUserChordsSuccess(int level, int achievements);
    void onUpdateUserChordsError();
}

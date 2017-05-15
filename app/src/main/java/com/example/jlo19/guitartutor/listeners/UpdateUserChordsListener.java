package com.example.jlo19.guitartutor.listeners;

/**
 *
 */
public interface UpdateUserChordsListener {
    void onUpdateUserChordsSuccess(int level, int achievements);
    void onUpdateUserChordsError();
}

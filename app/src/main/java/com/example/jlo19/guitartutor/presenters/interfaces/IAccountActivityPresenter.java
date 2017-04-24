package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.listeners.GetUserChordsListener;

/**
 * Interface for AccountActivityPresenter
 */
public interface IAccountActivityPresenter extends IPresenter, GetUserChordsListener {
    void viewOnConfirmError();
}

package com.example.jlo19.guitartutor.listeners;

import com.example.jlo19.guitartutor.models.User;

/**
 * Listener for GetAccountDetailsInteractor
 */
public interface GetAccountDetailsListener {
    void onAccountDetailsRetrieved(User user);
    void onGetAccountDetailsError();
}

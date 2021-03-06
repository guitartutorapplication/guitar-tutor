package com.example.jlo19.guitartutor.interactors.interfaces;

import com.example.jlo19.guitartutor.listeners.GetAccountDetailsListener;

/**
 * Interface for GetAccountDetailsInteractor
 */
public interface IGetAccountDetailsInteractor {
    void setListener(GetAccountDetailsListener listener);
    void getAccountDetails(String apiKey, int userId);
}

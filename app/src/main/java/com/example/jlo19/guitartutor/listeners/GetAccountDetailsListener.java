package com.example.jlo19.guitartutor.listeners;

import com.example.jlo19.guitartutor.models.retrofit.objects.User;


public interface GetAccountDetailsListener {
    void onAccountDetailsRetrieved(User user);
    void onGetAccountDetailsError();
}

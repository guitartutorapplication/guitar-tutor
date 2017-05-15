package com.example.jlo19.guitartutor.interactors.interfaces;

import com.example.jlo19.guitartutor.listeners.EditAccountDetailsListener;

/**
 * Interface for EditAccountDetailsInteractor
 */
public interface IEditAccountDetailsInteractor {
    void setListener(EditAccountDetailsListener listener);
    void save(String apiKey, int userId, String name, String email, String password);
}
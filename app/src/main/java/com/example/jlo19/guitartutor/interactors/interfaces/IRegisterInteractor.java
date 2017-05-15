package com.example.jlo19.guitartutor.interactors.interfaces;

import com.example.jlo19.guitartutor.listeners.RegisterListener;

/**
 * Interface for RegisterInteractor
 */
public interface IRegisterInteractor {
    void setListener(RegisterListener listener);
    void register(String name, String email, String password);
}

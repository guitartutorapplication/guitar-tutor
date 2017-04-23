package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.listeners.GetAccountDetailsListener;

/**
 * Interface for AccountPresenter
 */
public interface IAccountPresenter extends IPresenter, GetAccountDetailsListener {
    void viewOnLogout();
    void viewOnEditAccount();
    void viewOnAccountActivityRequested();
    void viewOnConfirmError();
}

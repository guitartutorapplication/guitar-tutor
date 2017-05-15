package com.example.jlo19.guitartutor.presenters.interfaces;

import com.example.jlo19.guitartutor.listeners.AddUserChordListener;
import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceImageListener;
import com.example.jlo19.guitartutor.listeners.AmazonS3ServiceUrlListener;

/**
 * Interface for LearnChordPresenter
 */
public interface ILearnChordPresenter extends IPresenter, AmazonS3ServiceUrlListener,
        AmazonS3ServiceImageListener, AddUserChordListener {
    void viewOnVideoRequested();
    void viewOnLearnt();
    void viewOnConfirmLearnt();
    void viewOnConfirmError();
    void viewOnConfirmLearntSuccess();
    void viewOnHelpRequested();
}

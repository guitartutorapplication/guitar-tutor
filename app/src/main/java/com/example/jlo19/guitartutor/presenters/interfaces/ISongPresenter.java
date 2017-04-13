package com.example.jlo19.guitartutor.presenters.interfaces;

/**
 * Interface for SongPresenter
 */
public interface ISongPresenter extends IPresenter {
    void viewOnPlay();
    void modelOnUrlDownloadSuccess(String url);
    void modelOnUrlDownloadFailed();
    void viewOnAudioLoadFailed();
    void viewOnAudioLoaded();
    void viewOnStop();
}

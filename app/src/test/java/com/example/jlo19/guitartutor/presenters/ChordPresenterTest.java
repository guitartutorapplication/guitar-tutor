package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.services.AmazonS3Service;
import com.example.jlo19.guitartutor.views.ChordView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Testing ChordPresenter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class ChordPresenterTest {

    private ChordPresenter presenter;
    private AmazonS3Service service;
    private Chord selectedChord;

    @Before
    public void setUp() {
        presenter = new ChordPresenter();

        // mocking view and service
        ChordView view = Mockito.mock(ChordView.class);
        service = Mockito.mock(AmazonS3Service.class);

        // setting selected chord
        selectedChord = new Chord(1, "A", "MAJOR", "A.png", "A.mp4");
        PowerMockito.when(view.getChord()).thenReturn(selectedChord);

        // mocking app component
        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.getComponent()).thenReturn(Mockito.mock(AppComponent.class));

        presenter.setView(view);
        presenter.createAmazonS3Service(service);
    }

    @Test
    public void getVideo_CallsGetVideoOnService() {
        // act
        presenter.getVideo();

        // assert
        Mockito.verify(service).getVideo(selectedChord.getVideoFilename());
    }
}

package com.example.jlo19.guitartutor.presenters;

import com.example.jlo19.guitartutor.application.LoggedInUser;
import com.example.jlo19.guitartutor.models.interfaces.IGetChordsInteractor;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.ILearnAllChordsPresenter;
import com.example.jlo19.guitartutor.views.LearnAllChordsView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

/**
 * Testing LearnAllChordsPresenter
 */
public class LearnAllChordsPresenterTest {

    private LearnAllChordsView view;
    private ILearnAllChordsPresenter presenter;
    private IGetChordsInteractor getChordsInteractor;
    private List<Chord> chords;
    private List<Integer> userChords;
    private int userLevel;
    private LoggedInUser loggedInUser;

    @Before
    public void setUp() {
        loggedInUser = Mockito.mock(LoggedInUser.class);
        Mockito.when(loggedInUser.getApiKey()).thenReturn("api_key");
        Mockito.when(loggedInUser.getUserId()).thenReturn(3);
        getChordsInteractor = Mockito.mock(IGetChordsInteractor.class);
        presenter = new LearnAllChordsPresenter(getChordsInteractor, loggedInUser);

        view = Mockito.mock(LearnAllChordsView.class);
        presenter.setView(view);

        chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1),
                new Chord(3, "A", "MINOR", "Am.png", "Am.mp4", "Am.wav", 2),
                new Chord(4, "B", "MINOR", "Bm.png", "Bm.mp4", "Bm.wav", 2),
                new Chord(5, "A", "SEVEN", "A7.png", "A7.mp4", "A7.wav", 3),
                new Chord(6, "B", "SEVEN", "B7.png", "B7.mp4", "B7.wav", 3),
                new Chord(7, "B", "FLAT", "Bb.png", "Bb.mp4", "Bb.wav", 4),
                new Chord(8, "D", "FLAT", "Db.png", "Db.mp4", "Db.wav", 4),
                new Chord(9, "F", "SHARP", "F#.png", "F#.mp4", "F#.wav", 5),
                new Chord(10, "C", "SHARP", "C#.png", "C#.mp4", "C#.wav", 5),
                new Chord(11, "C", "SHARP_MINOR", "C#m.png", "C#m.mp4", "C#m.wav", 6),
                new Chord(12, "F", "SHARP_MINOR", "F#m.png", "F#m.mp4", "F#m.wav", 6));
        Mockito.when(getChordsInteractor.getAllChords()).thenReturn(chords);

        userChords = Arrays.asList(1, 3, 5, 7, 9, 11);
        Mockito.when(getChordsInteractor.getUserChordIds()).thenReturn(userChords);

        userLevel = 3;
    }

    @Test
    public void setView_CallsShowProgressBarOnView() {
        // assert
        Mockito.verify(view).showProgressBar();
    }

    @Test
    public void setView_CallsGetChordsAndDetailsOnInteractor() {
        // assert
        Mockito.verify(getChordsInteractor).getChordsAndDetails(loggedInUser.getApiKey(),
                loggedInUser.getUserId());
    }

    @Test
    public void setsPresenterAsListenerOnInteractor() {
        // assert
        Mockito.verify(getChordsInteractor).setListener(presenter);
    }

    @Test
    public void onChordsAndDetailsRetrieved_HidesProgressBarOnView() {
        // act
        presenter.onChordsAndDetailsRetrieved(chords, userLevel, userChords);

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onError_HidesProgressBarOnView() {
        // act
        presenter.onError();

        // assert
        Mockito.verify(view).hideProgressBar();
    }

    @Test
    public void onError_ShowErrorOnView() {
        // act
        presenter.onError();

        // assert
        Mockito.verify(view).showError();
    }

    @Test
    public void onChordsAndDetailsRetrieved_CallsAddChordButtonOnViewForEachChord() {
        // act
        presenter.onChordsAndDetailsRetrieved(chords, userLevel, userChords);

        // assert
        for (int i = 0; i < chords.size(); i++) {
            Mockito.verify(view).addChordButton(i);
        }
    }

    @Test
    public void onChordsAndDetailsRetrieved_CallsSetButtonTextOnViewForEachChord() {
        // act
        presenter.onChordsAndDetailsRetrieved(chords, userLevel, userChords);

        // assert
        for (int i = 0; i < chords.size(); i++) {
            Mockito.verify(view).setChordButtonText(i, chords.get(i).toString());
        }
    }

    @Test
    public void onChordsAndDetailsRetrieved_CallsEnableButtonOnViewForEachChord() {
        // act
        presenter.onChordsAndDetailsRetrieved(chords, userLevel, userChords);

        // assert
        for (int i = 0; i < chords.size(); i++) {
            Mockito.verify(view).enableChordButton(i, chords.get(i).getLevelRequired() <= userLevel);
        }
    }

    @Test
    public void onChordsAndDetailsRetrieved_CallsSetButtonBackgroundOnViewForEachChord() {
        // act
        presenter.onChordsAndDetailsRetrieved(chords, userLevel, userChords);

        // assert
        for (int i = 0; i < chords.size(); i++) {
            boolean userHasLearntChord = userChords.contains(chords.get(i).getId());

            String doneIdentifier = "";
            String levelNumberIdentifier;
            if (userHasLearntChord) {
                doneIdentifier = "done_";
            }

            if (chords.get(i).getLevelRequired() == 1) {
                levelNumberIdentifier = "one";
            }
            else if (chords.get(i).getLevelRequired() == 2) {
                levelNumberIdentifier = "two";
            }
            else if (chords.get(i).getLevelRequired() == 3) {
                levelNumberIdentifier = "three";
            }
            else if (chords.get(i).getLevelRequired() == 4) {
                levelNumberIdentifier = "four";
            }
            else if (chords.get(i).getLevelRequired() == 5) {
                levelNumberIdentifier = "five";
            }
            else {
                levelNumberIdentifier = "six";
            }

            Mockito.verify(view).setChordButtonBackground(i, doneIdentifier, levelNumberIdentifier);
        }
    }

    @Test
    public void onChordsAndDetailsRetrieved_CallsSetChordButtonsOnView() {
        // act
        presenter.onChordsAndDetailsRetrieved(chords, userLevel, userChords);

        // assert
        Mockito.verify(view).setChordButtons();
    }

    @Test
    public void viewOnChordRequested_StartsLearnChordActivity() {
        // act
        int pos = 3;
        presenter.viewOnChordRequested(pos);

        // assert
        Chord chord = chords.get(pos);
        Mockito.verify(view).startLearnChordActivity(chord, userChords.contains(chord.getId()));
    }

    @Test
    public void viewOnConfirmError_CallFinishActivityOnView() {
        // act
        presenter.viewOnConfirmError();

        // assert
        Mockito.verify(view).finishActivity();
    }
}

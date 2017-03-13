package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractiseSetupPresenter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowProgressDialog;
import org.robolectric.shadows.ShadowToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.robolectric.Shadows.shadowOf;

/**
 * Testing PractiseSetupActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class PractiseSetupActivityTest {

    private PractiseSetupActivity activity;
    private IPractiseSetupPresenter presenter;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp()
    {
        // stops real injection of presenter
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        activity = Robolectric.buildActivity(PractiseSetupActivity.class)
                .create().get();
        presenter = PowerMockito.mock(IPractiseSetupPresenter.class);
        activity.setPresenter(presenter);
    }

    @Test
    public void oneChordSelected_WhenPractiseButtonIsClicked_CallsChordsSelectedOnPresenter() {
        // arrange
        final List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"),
                new Chord(3, "C", "MAJOR", "C.png", "C.mp4"),
                new Chord(4, "D", "MAJOR", "D.png", "D.mp4"));
        activity.setChords(chords);
        Spinner spnChord1 = (Spinner) activity.findViewById(R.id.spnChord1);
        // index 1 as default option will be at index 0
        spnChord1.setSelection(1);

        // act
        Button btnPractise = (Button) activity.findViewById(R.id.btnPractise);
        btnPractise.performClick();

        // assert
        ArrayList<String> expectedSelectedChords = new ArrayList<String>(){{
            add(chords.get(0).toString());
        }};
        Mockito.verify(presenter).viewOnChordsSelected(expectedSelectedChords);
    }

    @Test
    public void twoChordsSelected_WhenPractiseButtonIsClicked_CallsChordsSelectedOnPresenter() {
        // arrange
        final List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"),
                new Chord(3, "C", "MAJOR", "C.png", "C.mp4"),
                new Chord(4, "D", "MAJOR", "D.png", "D.mp4"));
        activity.setChords(chords);
        Spinner spnChord1 = (Spinner) activity.findViewById(R.id.spnChord1);
        // index 1 as default option will be at index 0
        spnChord1.setSelection(1);
        Spinner spnChord2 = (Spinner) activity.findViewById(R.id.spnChord2);
        spnChord2.setSelection(2);

        // act
        Button btnPractise = (Button) activity.findViewById(R.id.btnPractise);
        btnPractise.performClick();

        // assert
        ArrayList<String> expectedSelectedChords = new ArrayList<String>(){{
            add(chords.get(0).toString());
            add(chords.get(1).toString());
        }};
        Mockito.verify(presenter).viewOnChordsSelected(expectedSelectedChords);
    }

    @Test
    public void threeChordsSelected_WhenPractiseButtonIsClicked_CallsChordsSelectedOnPresenter() {
        // arrange
        final List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"),
                new Chord(3, "C", "MAJOR", "C.png", "C.mp4"),
                new Chord(4, "D", "MAJOR", "D.png", "D.mp4"));
        activity.setChords(chords);
        Spinner spnChord1 = (Spinner) activity.findViewById(R.id.spnChord1);
        // index 1 as default option will be at index 0
        spnChord1.setSelection(1);
        Spinner spnChord2 = (Spinner) activity.findViewById(R.id.spnChord2);
        spnChord2.setSelection(2);
        Spinner spnChord3 = (Spinner) activity.findViewById(R.id.spnChord3);
        spnChord3.setSelection(3);

        // act
        Button btnPractise = (Button) activity.findViewById(R.id.btnPractise);
        btnPractise.performClick();

        // assert
        ArrayList<String> expectedSelectedChords = new ArrayList<String>(){{
            add(chords.get(0).toString());
            add(chords.get(1).toString());
            add(chords.get(2).toString());
        }};
        Mockito.verify(presenter).viewOnChordsSelected(expectedSelectedChords);
    }

    @Test
    public void fourChordsSelected_WhenPractiseButtonIsClicked_CallsChordsSelectedOnPresenter() {
        // arrange
        final List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"),
                new Chord(3, "C", "MAJOR", "C.png", "C.mp4"),
                new Chord(4, "D", "MAJOR", "D.png", "D.mp4"));
        activity.setChords(chords);
        Spinner spnChord1 = (Spinner) activity.findViewById(R.id.spnChord1);
        // index 1 as default option will be at index 0
        spnChord1.setSelection(1);
        Spinner spnChord2 = (Spinner) activity.findViewById(R.id.spnChord2);
        spnChord2.setSelection(2);
        Spinner spnChord3 = (Spinner) activity.findViewById(R.id.spnChord3);
        spnChord3.setSelection(3);
        Spinner spnChord4 = (Spinner) activity.findViewById(R.id.spnChord4);
        spnChord4.setSelection(4);

        // act
        Button btnPractise = (Button) activity.findViewById(R.id.btnPractise);
        btnPractise.performClick();

        // assert
        ArrayList<String> expectedSelectedChords = new ArrayList<String>(){{
            add(chords.get(0).toString());
            add(chords.get(1).toString());
            add(chords.get(2).toString());
            add(chords.get(3).toString());
        }};
        Mockito.verify(presenter).viewOnChordsSelected(expectedSelectedChords);
    }

    @Test
    public void setPresenter_SetsActivityAsViewInPresenter() {
        // assert
        Mockito.verify(presenter).setView(activity);
    }

    @Test
    public void setToolbarTitleText_SetsToolbarTextToPractiseSetupName() {
        // act
        activity.setToolbarTitleText();

        // assert
        TextView view = (TextView) activity.findViewById(R.id.toolbarTitle);
        Assert.assertEquals(getApp().getResources().getString(R.string.practise_setup_name),
                view.getText());
    }

    @Test
    public void showProgressBar_ProgressDialogWillShowWithMessage() {
        // act
        activity.showProgressBar();

        // assert
        ProgressDialog dialog = (ProgressDialog) ShadowProgressDialog.getLatestDialog();
        Assert.assertTrue(dialog.isShowing());
        Assert.assertEquals(getApp().getResources().getString(R.string.loading_chords_message),
                (shadowOf(dialog).getMessage()));
    }

    @Test
    public void showProgressBar_hideProgressBar_NoProgressDialogShowing() {
        // arrange
        activity.showProgressBar();

        // act
        activity.hideProgressBar();

        // assert
        ProgressDialog dialog = (ProgressDialog) ShadowProgressDialog.getLatestDialog();
        Assert.assertFalse(dialog.isShowing());
    }

    @Test
    public void setChords_SetsSpinnersWithChordsAndDefaultOption() {
        // act
        final List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
        activity.setChords(chords);

        // assert
        Spinner spnChord1 = (Spinner) activity.findViewById(R.id.spnChord1);
        Spinner spnChord2 = (Spinner) activity.findViewById(R.id.spnChord2);
        Spinner spnChord3 = (Spinner) activity.findViewById(R.id.spnChord3);
        Spinner spnChord4 = (Spinner) activity.findViewById(R.id.spnChord4);

        Assert.assertEquals(getApp().getResources().getString(R.string.select_chord_instruction),
                spnChord1.getAdapter().getItem(0));
        Assert.assertEquals(getApp().getResources().getString(R.string.select_chord_instruction),
                spnChord2.getAdapter().getItem(0));
        Assert.assertEquals(getApp().getResources().getString(R.string.select_chord_instruction),
                spnChord3.getAdapter().getItem(0));
        Assert.assertEquals(getApp().getResources().getString(R.string.select_chord_instruction),
                spnChord4.getAdapter().getItem(0));

        Assert.assertEquals(chords.get(0).toString(), spnChord1.getAdapter().getItem(1));
        Assert.assertEquals(chords.get(0).toString(), spnChord2.getAdapter().getItem(1));
        Assert.assertEquals(chords.get(0).toString(), spnChord3.getAdapter().getItem(1));
        Assert.assertEquals(chords.get(0).toString(), spnChord4.getAdapter().getItem(1));

        Assert.assertEquals(chords.get(1).toString(), spnChord1.getAdapter().getItem(2));
        Assert.assertEquals(chords.get(1).toString(), spnChord2.getAdapter().getItem(2));
        Assert.assertEquals(chords.get(1).toString(), spnChord3.getAdapter().getItem(2));
        Assert.assertEquals(chords.get(1).toString(), spnChord4.getAdapter().getItem(2));
    }

    @Test
    public void showLoadChordError_MakesToastWithLoadChordsFailureMessage() {
        // act
        activity.showLoadChordsError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.loading_chords_message_failure),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showLessThanTwoChordsSelectedError_MakesToastWithLessThanTwoChordsSelectedMessage() {
        // act
        activity.showLessThanTwoChordsSelectedError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.less_than_two_selected_chords_error),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showSameSelectedChordError_MakesToastWithSameChordSelectedMessage() {
        // act
        activity.showSameSelectedChordError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.same_chord_selected_error),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void startPractiseActivity_PractiseActivityIsStartedWithSelectedChords() {
        // act
        ArrayList<String> chords = new ArrayList<String>() {{
            add("A");
            add("B");
        }};
        activity.startPractiseActivity(chords);

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(PractiseActivity.class.getName(), intent.getComponent().getClassName());
        // checks correct chord is passed through
        Assert.assertEquals(chords, intent.getExtras().getStringArrayList("CHORDS"));
    }
}

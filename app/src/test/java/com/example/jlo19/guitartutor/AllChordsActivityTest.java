package com.example.jlo19.guitartutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.jlo19.guitartutor.activities.AllChordsActivity;
import com.example.jlo19.guitartutor.activities.ChordActivity;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.presenters.AllChordsPresenter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowProgressDialog;
import org.robolectric.shadows.ShadowToast;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Testing AllChordsActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class AllChordsActivityTest {

    private AllChordsActivity activity;
    private AllChordsPresenter presenter;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp()
    {
        // stops real injection of presenter
        getApp().setComponent(Mockito.mock(AppComponent.class));

        activity = Robolectric.buildActivity(AllChordsActivity.class)
                .create().get();
        presenter = Mockito.mock(AllChordsPresenter.class);
        activity.setPresenter(presenter);
    }

    @Test
    public void setPresenter_SetsActivityAsViewInPresenter() {
        // assert
        verify(presenter).setView(activity);
    }

    @Test
    public void setChords_SetsGridViewWithChordItems() {
        // arrange
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png"),
                new Chord(2, "B", "MAJOR", "B.png"));

        // act
        activity.setChords(chords);

        // assert
        GridView gridView = (GridView) activity.findViewById(R.id.gridView);
        Assert.assertEquals(chords.get(0), gridView.getAdapter().getItem(0));
        Assert.assertEquals(chords.get(1), gridView.getAdapter().getItem(1));
    }

    @Test
    public void setChords_WhenChordButtonClicked_ChordsActivityIsStartedWithSelectedChord() {
        // arrange
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png"),
                new Chord(2, "B", "MAJOR", "B.png"));
        activity.setChords(chords);

        // act
        GridView gridView = (GridView) activity.findViewById(R.id.gridView);

        int selectedChord = 0;
        Button button = (Button) gridView.getAdapter().getView(selectedChord, null, gridView);
        button.performClick();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(ChordActivity.class.getName(), intent.getComponent().getClassName());
        // checks correct chord is passed through
        Assert.assertEquals(intent.getParcelableExtra("CHORD"), chords.get(selectedChord));
    }

    @Test
    public void showError_MakesToastWithErrorMessage() {
        // act
        activity.showError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.loading_chords_message_failure),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void setToolbarTitleText_SetsTitleOfToolbar() {
        // act
        activity.setToolbarTitleText();

        // assert
        TextView view = (TextView) activity.findViewById(R.id.toolbar_title);
        Assert.assertEquals(getApp().getResources().getString(R.string.all_chords_name),
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

}
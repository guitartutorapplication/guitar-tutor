package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
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
    private SoundPool soundPool;

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
        soundPool = PowerMockito.mock(SoundPool.class);
        activity.setSoundPool(soundPool);
    }

    @Test
    public void setPresenter_SetsSharedPreferencesOnPresenter() {
        // assert
        Mockito.verify(presenter).setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(activity));
    }

    @Test
    public void setSelectedChordChordChangeSpeedAndBeatSpeed_PractiseButtonClicked_CallsPractiseOnPresenter() {
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

        RadioGroup rGroupChordChange = (RadioGroup) activity.findViewById(R.id.rGroupChordChange);
        rGroupChordChange.check(R.id.rbtnEightBeats);

        RadioGroup rGroupBeatSpeed = (RadioGroup) activity.findViewById(R.id.rGroupBeatSpeed);
        rGroupBeatSpeed.check(R.id.rbtnFastBeat);

        // act
        Button btnPractise = (Button) activity.findViewById(R.id.btnPractise);
        btnPractise.performClick();

        // assert
        ArrayList<String> expectedSelectedChords = new ArrayList<String>(){{
            add(chords.get(0).toString());
        }};
        Mockito.verify(presenter).viewOnPractise(expectedSelectedChords, 3, 3);
    }

    @Test
    public void onBeatSpeedChanged_CallsBeatSpeedChangedOnPresenter() {
        // act
        RadioButton rbtnFastBeat = (RadioButton) activity.findViewById(R.id.rbtnFastBeat);
        rbtnFastBeat.setChecked(true);

        // assert
        Mockito.verify(presenter).viewOnBeatSpeedChanged();
    }

    @Test
    public void previewButtonClicked_CallsBeatPreviewOnPresenter() {
        // assert
        RadioButton rbtnFastBeat = (RadioButton) activity.findViewById(R.id.rbtnFastBeat);
        rbtnFastBeat.setChecked(true);

        // act
        Button btnPreview = (Button) activity.findViewById(R.id.btnPreview);
        btnPreview.performClick();

        // assert
        Mockito.verify(presenter).viewOnBeatPreview(3);
    }

    @Test
    public void setPresenter_SetsActivityAsViewInPresenter() {
        // assert
        Mockito.verify(presenter).setView(activity);
    }

    @Test
    public void setsToolbarTextToPractiseSetupName() {
        // assert
        TextView view = (TextView) activity.findViewById(R.id.toolbarTitle);
        Assert.assertEquals(getApp().getResources().getString(R.string.practise_setup_name),
                view.getText().toString());
    }

    @Test
    public void accountButtonClicked_StartsAccountActivity() {
        // act
        Button btnAccount = (Button) activity.findViewById(R.id.btnAccount);
        btnAccount.performClick();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(AccountActivity.class.getName(), intent.getComponent().getClassName());
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
    public void showPreviewBeatError_MakesToastWithPreviewBeatFailureMessage() {
        // act
        activity.showPreviewBeatError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.practise_beat_preview_error_message),
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
    public void startPractiseActivity_PractiseActivityIsStartedWithSelectedChordsChordChangeAndBeatSpeed() {
        // act
        ArrayList<String> chords = new ArrayList<String>() {{
            add("A");
            add("B");
        }};
        ChordChange chordChange = ChordChange.ONE_BEAT;
        BeatSpeed beatSpeed = BeatSpeed.VERY_SLOW;
        activity.startPractiseActivity(chords, chordChange, beatSpeed);

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(PractiseActivity.class.getName(), intent.getComponent().getClassName());
        // checks correct chord is passed through
        Assert.assertEquals(chords, intent.getExtras().getStringArrayList("CHORDS"));
        // checks correct chord change is passed through
        Assert.assertEquals(chordChange, intent.getSerializableExtra("CHORD_CHANGE"));
        // checks correct beat speed is passed through
        Assert.assertEquals(beatSpeed, intent.getSerializableExtra("BEAT_SPEED"));
    }

    @Test
    public void onDestroy_SoundPoolReleased() {
        // act
        activity.onDestroy();

        // assert
        Mockito.verify(soundPool).release();
    }

    @Test
    public void onDestroy_CallsOnDestroyOnPresenter() {
        // act
        activity.onDestroy();

        // assert
        Mockito.verify(presenter).viewOnDestroy();
    }

    @Test
    public void loadSound_CallsLoadOnSoundPoolWithMetronomeClip() {
        // act
        activity.loadSound();

        // assert
        Mockito.verify(soundPool).load(activity, R.raw.metronome_sound, 1);
    }

    @Test
    public void playSound_CallsPlayOnSoundPoolWithLoadedSoundId() {
        // arrange
        int expectedSoundId = 1;
        Mockito.when(soundPool.load(activity, R.raw.metronome_sound, 1)).thenReturn(expectedSoundId);
        activity.loadSound();

        // act
        activity.playSound();

        // assert
        Mockito.verify(soundPool).play(expectedSoundId, 1.0f, 1.0f, 1, 0, 0.75f);
    }

    @Test
    public void enablePreviewButtonWithFalse_DisablesPreviewButton() {
        // act
        activity.enablePreviewButton(false);

        // assert
        Button btnPreview = (Button) activity.findViewById(R.id.btnPreview);
        Assert.assertFalse(btnPreview.isEnabled());
    }

    @Test
    public void enablePreviewButtonWithTrue_EnablesPreviewButton() {
        // act
        activity.enablePreviewButton(true);

        // assert
        Button btnPreview = (Button) activity.findViewById(R.id.btnPreview);
        Assert.assertTrue(btnPreview.isEnabled());
    }

    @Test
    public void onPause_CallsOnPauseOnPresenter() {
        // act
        activity.onPause();

        // assert
        Mockito.verify(presenter).viewOnPause();
    }

    @Test
    public void onStop_CallsOnStopOnPresenter() {
        // act
        activity.onStop();

        // assert
        Mockito.verify(presenter).viewOnStop();
    }

    @Test
    public void homeButtonClicked_StartsHomeActivity() {
        // act
        Button btnHome = (Button) activity.findViewById(R.id.btnHome);
        btnHome.performClick();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(HomeActivity.class.getName(), intent.getComponent().getClassName());
    }
}

package com.example.jlo19.guitartutor.activities;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;

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
import org.robolectric.shadows.ShadowToast;

import java.util.ArrayList;
import java.util.List;

import static org.robolectric.Shadows.shadowOf;

/**
 * Testing PractiseActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class PractiseActivityTest {

    private PractiseActivity activity;
    private IPractisePresenter presenter;
    private SoundPool soundPool;
    private ArrayList<String> selectedChords;
    private ChordChange chordChange;
    private BeatSpeed beatSpeed;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp()
    {
        // stops real injection of presenter
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        // giving activity selected chords, chord change and beat speed
        selectedChords = new ArrayList<String>(){
            {
                add("A");
                add("B");
                add("C");
            }};
        chordChange = ChordChange.EIGHT_BEATS;
        beatSpeed = BeatSpeed.FAST;
        Intent intent = new Intent();
        intent.putExtra("CHORDS", selectedChords);
        intent.putExtra("CHORD_CHANGE", chordChange);
        intent.putExtra("BEAT_SPEED", beatSpeed);

        activity = Robolectric.buildActivity(PractiseActivity.class, intent)
                .create().get();

        presenter = PowerMockito.mock(IPractisePresenter.class);
        activity.setPresenter(presenter);
        soundPool = PowerMockito.mock(SoundPool.class);
        activity.setSoundPool(soundPool);
    }

    @Test
    public void setSoundPool_SetsLoadCompleteListener() {
        // assert
        Mockito.verify(soundPool).setOnLoadCompleteListener((SoundPool.OnLoadCompleteListener) Mockito.any());
    }

    @Test
    public void onLoadCompleteFiveTimes_CallsSoundsLoadedOnPresenter() {
        // arrange
        activity.onLoadCompleteListener.onLoadComplete(soundPool, 1, 1);
        activity.onLoadCompleteListener.onLoadComplete(soundPool, 1, 1);
        activity.onLoadCompleteListener.onLoadComplete(soundPool, 1, 1);
        activity.onLoadCompleteListener.onLoadComplete(soundPool, 1, 1);

        // act
        activity.onLoadCompleteListener.onLoadComplete(soundPool, 1, 1);

        // assert
        Mockito.verify(presenter).viewOnSoundsLoaded();
    }

    @Test
    public void setPresenter_SetsActivityAsViewInPresenter() {
        // assert
        Mockito.verify(presenter).setView(activity);
    }

    @Test
    public void stopButtonClick_CallsStopPractisingOnPresenter() {
        // act
        Button button = (Button) activity.findViewById(R.id.btnStop);
        button.performClick();

        // assert
        Mockito.verify(presenter).viewOnStopPractising();
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
    public void onStop_CallsOnStopOnPresenter() {
        // act
        activity.onStop();

        // assert
        Mockito.verify(presenter).viewOnStop();
    }

    @Test
    public void setsToolbarTextToPractisingChordsName() {
        // assert
        TextView view = (TextView) activity.findViewById(R.id.toolbarTitle);
        Assert.assertEquals(getApp().getResources().getString(R.string.practising_chords_name),
                view.getText().toString());
    }

    @Test
    public void onPause_CallsOnPauseOnPresenter() {
        // act
        activity.onPause();

        // assert
        Mockito.verify(presenter).viewOnPause();
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
    public void getSelectedChords_ReturnsSelectedChordsFromIntent() {
        // act
        List<String> actualSelectedChords = activity.getSelectedChords();

        // assert
        Assert.assertEquals(selectedChords, actualSelectedChords);
    }

    @Test
    public void getChordChange_ReturnsChordChangeFromIntent() {
        // act
        ChordChange actualChordChange = activity.getChordChange();

        // assert
        Assert.assertEquals(chordChange, actualChordChange);
    }

    @Test
    public void getBeatSpeed_ReturnsBeatSpeedFromIntent() {
        // act
        BeatSpeed actualBeatSpeed = activity.getBeatSpeed();

        // assert
        Assert.assertEquals(beatSpeed, actualBeatSpeed);
    }

    @Test
    public void setChordText_SetsTextView() {
        // act
        String expectedChordText = "C";
        activity.setChordText(expectedChordText);

        // assert
        TextView txtChord = (TextView) activity.findViewById(R.id.txtChord);
        String actualChordText = (String) txtChord.getText();
        Assert.assertEquals(expectedChordText, actualChordText);
    }

    @Test
    public void showStopButton_SetsVisibilityOnStopButtonToVisible() {
        // act
        activity.showStopButton();

        // assert
        Button btnStop = (Button) activity.findViewById(R.id.btnStop);
        int actualVisibility = btnStop.getVisibility();
        Assert.assertEquals(View.VISIBLE, actualVisibility);
    }

    @Test
    public void loadSounds_CallsLoadOnSoundPoolWith5SoundClips() {
        // act
        activity.loadSounds();

        // assert
        Mockito.verify(soundPool).load(activity, R.raw.metronome_sound, 1);
        Mockito.verify(soundPool).load(activity, R.raw.countdown_3, 1);
        Mockito.verify(soundPool).load(activity, R.raw.countdown_2, 1);
        Mockito.verify(soundPool).load(activity, R.raw.countdown_1, 1);
        Mockito.verify(soundPool).load(activity, R.raw.countdown_go, 1);
    }

    @Test
    public void playMetronomeSound_CallsPlayOnSoundPoolWithLoadedSoundId() {
        // arrange
        int expectedSoundId = 1;
        Mockito.when(soundPool.load(activity, R.raw.metronome_sound, 1)).thenReturn(expectedSoundId);
        activity.loadSounds();

        // act
        activity.playMetronomeSound();

        // assert
        Mockito.verify(soundPool).play(expectedSoundId, 1.0f, 1.0f, 1, 0, 0.75f);
    }

    @Test
    public void showError_MakesToastWithErrorMessage() {
        // act
        activity.showError();

        // arrange
        Assert.assertEquals(getApp().getResources().getString(R.string.practise_error_occurred_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void setCountdownText_SetsCountdownText() {
        // act
        String expectedText = "3";
        activity.setCountdownText(expectedText);

        // assert
        TextView txtCountdown = (TextView) activity.findViewById(R.id.txtCountdown);
        Assert.assertEquals(expectedText, txtCountdown.getText());
    }

    @Test
    public void hideCountdown_SetsCountdownTextToInvisible() {
        // act
        activity.hideCountdown();

        // assert
        TextView txtCountdown = (TextView) activity.findViewById(R.id.txtCountdown);
        Assert.assertEquals(View.INVISIBLE, txtCountdown.getVisibility());
    }

    @Test
    public void hideFirstChordInstruction_SetsFirstChordAndInstructionToInvisible() {
        // act
        activity.hideFirstChordInstruction();

        // assert
        TextView firstChord = (TextView) activity.findViewById(R.id.txtFirstChord);
        TextView firstChordInstruction = (TextView) activity.findViewById(R.id.txtFirstChordInstruction);
        Assert.assertEquals(View.INVISIBLE, firstChord.getVisibility());
        Assert.assertEquals(View.INVISIBLE, firstChordInstruction.getVisibility());
    }

    @Test
    public void setFirstChordText_SetsFirstChordText() {
        // act
        String expectedText = "A";
        activity.setFirstChordText(expectedText);

        // assert
        TextView txtFirstChord = (TextView) activity.findViewById(R.id.txtFirstChord);
        Assert.assertEquals(expectedText, txtFirstChord.getText());
    }

    @Test
    public void playCountdownOneSound_CallsPlayOnSoundPoolWithLoadedSoundId() {
        // arrange
        int expectedSoundId = 2;
        Mockito.when(soundPool.load(activity, R.raw.countdown_1, 1)).thenReturn(expectedSoundId);
        activity.loadSounds();

        // act
        activity.playCountdownOneSound();

        // assert
        Mockito.verify(soundPool).play(expectedSoundId, 1.0f, 1.0f, 1, 0, 0.75f);
    }

    @Test
    public void playCountdownTwoSound_CallsPlayOnSoundPoolWithLoadedSoundId() {
        // arrange
        int expectedSoundId = 3;
        Mockito.when(soundPool.load(activity, R.raw.countdown_2, 1)).thenReturn(expectedSoundId);
        activity.loadSounds();

        // act
        activity.playCountdownTwoSound();

        // assert
        Mockito.verify(soundPool).play(expectedSoundId, 1.0f, 1.0f, 1, 0, 0.75f);
    }

    @Test
    public void playCountdownThreeSound_CallsPlayOnSoundPoolWithLoadedSoundId() {
        // arrange
        int expectedSoundId = 4;
        Mockito.when(soundPool.load(activity, R.raw.countdown_3, 1)).thenReturn(expectedSoundId);
        activity.loadSounds();

        // act
        activity.playCountdownThreeSound();

        // assert
        Mockito.verify(soundPool).play(expectedSoundId, 1.0f, 1.0f, 1, 0, 0.75f);
    }

    @Test
    public void playCountdownGoSound_CallsPlayOnSoundPoolWithLoadedSoundId() {
        // arrange
        int expectedSoundId = 5;
        Mockito.when(soundPool.load(activity, R.raw.countdown_go, 1)).thenReturn(expectedSoundId);
        activity.loadSounds();

        // act
        activity.playCountdownGoSound();

        // assert
        Mockito.verify(soundPool).play(expectedSoundId, 1.0f, 1.0f, 1, 0, 0.75f);
    }

    @Test
    public void returnToPractiseSetup_CallsFinish() {
        // act
        activity.returnToPractiseSetup();

        // assert
        Assert.assertTrue(activity.isFinishing());
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

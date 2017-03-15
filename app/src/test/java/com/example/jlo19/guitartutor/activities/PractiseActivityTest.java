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

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp()
    {
        // stops real injection of presenter
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        // giving activity selected chords
        selectedChords = new ArrayList<String>(){
            {
                add("A");
                add("B");
                add("C");
            }};
        chordChange = ChordChange.EIGHT_BEATS;
        Intent intent = new Intent();
        intent.putExtra("CHORDS", selectedChords);
        intent.putExtra("CHORD_CHANGE", chordChange);

        activity = Robolectric.buildActivity(PractiseActivity.class, intent)
                .create().get();

        presenter = PowerMockito.mock(IPractisePresenter.class);
        activity.setPresenter(presenter);
        soundPool = PowerMockito.mock(SoundPool.class);
        activity.setSoundPool(soundPool);
    }

    @Test
    public void setPresenter_SetsActivityAsViewInPresenter() {
        // assert
        Mockito.verify(presenter).setView(activity);
    }

    @Test
    public void startButtonClick_CallsStartPractisingOnPresenter(){
        // act
        Button button = (Button) activity.findViewById(R.id.btnStart);
        button.performClick();

        // assert
        Mockito.verify(presenter).viewOnStartPractising();
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
    public void onBackPressed_CallsStopPractisingOnPresenter() {
        // act
        activity.onBackPressed();

        // assert
        Mockito.verify(presenter).viewOnStopPractising();
    }

    @Test
    public void setToolbarTitleText_SetsToolbarTextToPractisingChordsName() {
        // act
        activity.setToolbarTitleText();

        // assert
        TextView view = (TextView) activity.findViewById(R.id.toolbarTitle);
        Assert.assertEquals(getApp().getResources().getString(R.string.practising_chords_name),
                view.getText());
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
    public void setStopButtonVisibility_SetsVisibilityOnStopButton() {
        // act
        int expectedVisibility = View.VISIBLE;
        activity.setStopButtonVisibility(expectedVisibility);

        // assert
        Button btnStop = (Button) activity.findViewById(R.id.btnStop);
        int actualVisibility = btnStop.getVisibility();
        Assert.assertEquals(expectedVisibility, actualVisibility);
    }

    @Test
    public void setStartButtonVisibility_SetsVisibilityOnStartButton() {
        // act
        int expectedVisibility = View.INVISIBLE;
        activity.setStartButtonVisibility(expectedVisibility);

        // assert
        Button btnStart = (Button) activity.findViewById(R.id.btnStart);
        int actualVisibility = btnStart.getVisibility();
        Assert.assertEquals(expectedVisibility, actualVisibility);
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
    public void showError_MakesToastWithErrorMessage() {
        // act
        activity.showError();

        // arrange
        Assert.assertEquals(getApp().getResources().getString(R.string.practise_error_occurred_message),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void startPractiseSetupActivity_StartsPractiseSetupActivity() {
        // act
        activity.startPractiseSetupActivity();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(PractiseSetupActivity.class.getName(), intent.getComponent().getClassName());
    }
}

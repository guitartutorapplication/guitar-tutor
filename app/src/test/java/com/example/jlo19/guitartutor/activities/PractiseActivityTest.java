package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.jlo19.guitartutor.models.Chord;
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
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
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
    private ArrayList<Chord> selectedChords;
    private ChordChange chordChange;
    private BeatSpeed beatSpeed;
    private List<String> audioFilenames;
    private ArrayList<Integer> audioResources;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp()
    {
        // stops real injection of presenter
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        // sets chords, chord change and beat speed in intent that builds activity
        selectedChords = new ArrayList<Chord>(){
            {
                add(new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1));
                add(new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
            }};
        chordChange = ChordChange.EIGHT_BEATS;
        beatSpeed = BeatSpeed.FAST;
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("CHORDS", selectedChords);
        intent.putExtra("CHORD_CHANGE", chordChange);
        intent.putExtra("BEAT_SPEED", beatSpeed);

        activity = Robolectric.buildActivity(PractiseActivity.class, intent)
                .create().get();

        presenter = PowerMockito.mock(IPractisePresenter.class);
        activity.setPresenter(presenter);
        soundPool = PowerMockito.mock(SoundPool.class);
        activity.setSoundPool(soundPool);

        audioFilenames = Arrays.asList("countdown_3", "countdown_2", "countdown_1",
                "countdown_go", "metronome_sound", "a", "b", "c");
        audioResources = new ArrayList<>();
        for (int i = 0; i < audioFilenames.size(); i++) {
            audioResources.add(getApp().getResources().getIdentifier(
                    audioFilenames.get(i), "raw", getApp().getPackageName()));
            Mockito.when(soundPool.load(activity, audioResources.get(i), 1)).thenReturn(i);
        }
    }

    @Test
    public void setSoundPool_SetsLoadCompleteListener() {
        // assert
        Mockito.verify(soundPool).setOnLoadCompleteListener((SoundPool.OnLoadCompleteListener) Mockito.any());
    }

    @Test
    public void onLoadComplete_CallsSoundLoadedOnPresenter() {
        // act
        int success = 0;
        activity.onLoadCompleteListener.onLoadComplete(soundPool, 1, success);

        // assert
        Mockito.verify(presenter).viewOnSoundLoaded(success);
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
        List<Chord> actualSelectedChords = activity.getSelectedChords();

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
    public void loadSounds_CallsLoadOnSoundPoolWithFilenames() {
        // act
        activity.loadSounds(audioFilenames);

        // assert
        for (int resource : audioResources) {
            Mockito.verify(soundPool).load(activity, resource, 1);
        }
    }

    @Test
    public void playSound_CallsPlayOnSoundPoolWithLoadedSoundId() {
        // arrange
        activity.loadSounds(audioFilenames);

        for (int i = 0; i < audioResources.size(); i++) {
            // act
            activity.playSound(i);

            // assert
            Mockito.verify(soundPool).play(i, 1.0f, 1.0f, 1, 0, 1f);
        }
    }

    @Test
    public void showError_ShowsAlertDialogWithErrorMessage() {
        // act
        activity.showError();

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Assert.assertEquals(getApp().getResources().getString(R.string.practise_error_occurred_message),
                shadowOf(dialog).getMessage());
    }

    @Test
    public void showError_ClickOkButton_CallsConfirmErrorOnPresenter() {
        // arrange
        activity.showError();

        // act
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Button btnOk = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnOk.performClick();

        // assert
        Mockito.verify(presenter).viewOnConfirmError();
    }

    @Test
    public void showPractiseSessionSaveError_ShowsAlertDialogWithErrorMessage() {
        // act
        activity.showPractiseSessionSaveError();

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Assert.assertEquals(getApp().getResources().getString(R.string.save_practise_session_error_message),
                shadowOf(dialog).getMessage());
    }

    @Test
    public void showPractiseSessionSaveError_ClicksOkButton_CallsConfirmErrorOnPresenter() {
        // arrange
        activity.showPractiseSessionSaveError();

        // act
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Button btnOk = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnOk.performClick();

        // assert
        Mockito.verify(presenter).viewOnConfirmError();
    }

    @Test
    public void showPractiseSessionSaveSuccess_ShowsAlertDialogWithSuccessMessage() {
        // act
        activity.showPractiseSessionSaveSuccess();

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        String expectedText = getApp().getResources().getString(
                R.string.save_practise_session_success_message) + "\n" + getApp().getResources()
                .getString(R.string.maximum_achievements_message);
        Assert.assertEquals(expectedText, shadowOf(dialog).getMessage());
    }

    @Test
    public void showPractiseSessionSaveSuccess_ClicksOkButton_CallsConfirmSuccessOnPresenter() {
        // arrange
        activity.showPractiseSessionSaveSuccess();

        // act
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Button btnOk = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnOk.performClick();

        // assert
        Mockito.verify(presenter).viewOnConfirmSuccess();
    }

    @Test
    public void showPractiseSessionSaveSuccessWithAchievements_ShowsAlertDialogWithSuccessMessage() {
        // act
        int achievements = 2100;
        activity.showPractiseSessionSaveSuccess(achievements);

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        String expectedText = getApp().getResources().getString(
                R.string.save_practise_session_success_message) + "\n" + getApp().getResources().
                getString(R.string.gained_15_achievements_message, achievements);
        Assert.assertEquals(expectedText, shadowOf(dialog).getMessage());
    }

    @Test
    public void showPractiseSessionSaveSuccessWithAchievements_ClickOkButton_CallsConfirmSuccessOnPresenter() {
        // arrange
        int achievements = 2100;
        activity.showPractiseSessionSaveSuccess(achievements);

        // act
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Button btnOk = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnOk.performClick();

        // assert
        Mockito.verify(presenter).viewOnConfirmSuccess();
    }

    @Test
    public void showPractiseSessionSaveSuccessWithLevel_ShowsAlertDialogWithSuccessMessage() {
        // act
        int achievements = 2000;
        int level = 3;
        activity.showPractiseSessionSaveSuccess(level, achievements);

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        String expectedText = getApp().getResources().getString(
                R.string.save_practise_session_success_message) + "\n" + getApp().getResources().
                getString(R.string.gained_15_achievements_message, achievements) + "\n" + getApp()
                .getResources().getString(R.string.new_level_message, level);
        Assert.assertEquals(expectedText, shadowOf(dialog).getMessage());
    }

    @Test
    public void showPractiseSessionSaveSuccessWithLevel_ClickOkButton_CallsConfirmSuccessOnPresenter() {
        // arrange
        int achievements = 2000;
        int level = 3;
        activity.showPractiseSessionSaveSuccess(level, achievements);

        // act
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Button btnOk = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnOk.performClick();

        // assert
        Mockito.verify(presenter).viewOnConfirmSuccess();
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
    public void returnToPractiseSetup_CallsFinish() {
        // act
        activity.returnToPractiseSetup();

        // assert
        Assert.assertTrue(activity.isFinishing());
    }

    @Test
    public void homeButtonClicked_StartsHomeActivityWithFlagsSetAndPractiseActivityIsFinished() {
        // act
        Button btnHome = (Button) activity.findViewById(R.id.btnHome);
        btnHome.performClick();

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(HomeActivity.class.getName(), intent.getComponent().getClassName());
        // check flags
        Assert.assertEquals(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK,
                intent.getFlags());
        Assert.assertTrue(activity.isFinishing());
    }
}

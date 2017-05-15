package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.Chord;
import com.example.jlo19.guitartutor.models.Song;
import com.example.jlo19.guitartutor.presenters.interfaces.ISongLibraryPresenter;

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
import org.robolectric.shadows.ShadowProgressDialog;

import java.util.Arrays;
import java.util.List;

import static org.robolectric.Shadows.shadowOf;

/**
 * Testing SongLibraryActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class SongLibraryActivityTest {

    private SongLibraryActivity activity;
    private ISongLibraryPresenter presenter;

    public App getApp() {
        return (App) RuntimeEnvironment.application;
    }

    @Before
    public void setUp()
    {
        // stops real injection of presenter
        getApp().setComponent(PowerMockito.mock(AppComponent.class));

        activity = Robolectric.buildActivity(SongLibraryActivity.class)
                .create().get();
        presenter = PowerMockito.mock(ISongLibraryPresenter.class);
        activity.setPresenter(presenter);
    }

    @Test
    public void setPresenter_SetsActivityAsViewInPresenter() {
        // assert
        Mockito.verify(presenter).setView(activity);
    }

    @Test
    public void setsTitleOfToolbar() {
        // assert
        TextView view = (TextView) activity.findViewById(R.id.toolbarTitle);
        Assert.assertEquals(getApp().getResources().getString(R.string.song_library_name),
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
        org.junit.Assert.assertTrue(dialog.isShowing());
        org.junit.Assert.assertEquals(getApp().getResources().getString(R.string.loading_songs_message),
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
        org.junit.Assert.assertFalse(dialog.isShowing());
    }

    @Test
    public void showError_ShowAlertDialogWithErrorMessage() {
        // act
        activity.showError();

        // assert
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        Assert.assertEquals(getApp().getResources().getString(R.string.loading_songs_message_failure),
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
    public void setSongs_SetsListViewWithSongItems() {
        // act
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav" , 1));
        List<Song> expectedSongs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "Adventure of a Lifetime.wav",
                        "contents", chords),
                new Song("Dance with Me Tonight", "Olly Murs", "Dance with Me Tonight.wav",
                        "contents", chords));
        activity.setSongs(expectedSongs);

        // assert
        ListView listView = (ListView) activity.findViewById(R.id.listView);
        Assert.assertEquals(expectedSongs.get(0), listView.getAdapter().getItem(0));
        Assert.assertEquals(expectedSongs.get(1), listView.getAdapter().getItem(1));
    }

    @Test
    public void setSongs_WhenSongListItemClicked_SongActivityIsStartedWithSelectedSong() {
        // arrange
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));
        List<Song> songs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "Adventure of a Lifetime.wav",
                        "contents", chords),
                new Song("Dance with Me Tonight", "Olly Murs", "Dance with Me Tonight.wav",
                        "contents", chords));
        activity.setSongs(songs);

        // act
        int position = 1;
        ListView listView = (ListView) activity.findViewById(R.id.listView);
        shadowOf(listView).performItemClick(position);

        // assert
        Intent intent = shadowOf(activity).getNextStartedActivity();
        // checks correct activity is started
        Assert.assertEquals(SongActivity.class.getName(), intent.getComponent().getClassName());
        // checks correct song is passed through
        Assert.assertEquals(songs.get(position), intent.getParcelableExtra("SONG"));
    }

    @Test
    public void homeButtonClicked_StartsHomeActivityWithFlagsSetAndSongLibraryActivityIsFinished() {
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

    @Test
    public void songFilterCheckChangedToLearntSongs_CallsViewFilterChangedOnPresenterWithFalse() {
        // act
        RadioButton rbtnViewLearntSongs = (RadioButton) activity.findViewById(R.id.rbtnViewLearntSongs);
        rbtnViewLearntSongs.setChecked(true);

        // assert
        Mockito.verify(presenter).viewOnSongFilterChanged(false);
    }

    @Test
    public void songFilterCheckChangedToAllSongs_CallsViewFilterChangedOnPresenterWithTrue() {
        // arrange
        RadioButton rbtnViewLearntSongs = (RadioButton) activity.findViewById(R.id.rbtnViewLearntSongs);
        rbtnViewLearntSongs.setChecked(true);

        // act
        RadioButton rbtnViewAllSongs = (RadioButton) activity.findViewById(R.id.rbtnViewAll);
        rbtnViewAllSongs.setChecked(true);

        // assert
        Mockito.verify(presenter).viewOnSongFilterChanged(true);
    }

    @Test
    public void onDestroy_CallsExitOnPresenter() {
        // act
        activity.onDestroy();

        // assert
        Mockito.verify(presenter).viewOnExit();
    }

    @Test
    public void finishActivity_FinishesActivity() {
        // act
        activity.finishActivity();

        // assert
        Assert.assertTrue(activity.isFinishing());
    }
}

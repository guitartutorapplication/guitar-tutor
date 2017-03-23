package com.example.jlo19.guitartutor.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.components.AppComponent;
import com.example.jlo19.guitartutor.models.retrofit.Chord;
import com.example.jlo19.guitartutor.models.retrofit.Song;
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
import org.robolectric.shadows.ShadowProgressDialog;
import org.robolectric.shadows.ShadowToast;

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
    public void setToolbarTitleText_SetsTitleOfToolbar() {
        // act
        activity.setToolbarTitleText();

        // assert
        TextView view = (TextView) activity.findViewById(R.id.toolbarTitle);
        Assert.assertEquals(getApp().getResources().getString(R.string.song_library_name),
                view.getText().toString());
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
    public void showError_MakesToastWithErrorMessage() {
        // act
        activity.showError();

        // assert
        Assert.assertEquals(getApp().getResources().getString(R.string.loading_songs_message_failure),
                ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void setSongs_SetsListViewWithSongItems() {
        // act
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
        List<Song> expectedSongs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "contents", chords),
                new Song("Dance with Me Tonight", "Olly Murs", "contents", chords));
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
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
        List<Song> songs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "contents", chords),
                new Song("Dance with Me Tonight", "Olly Murs", "contents", chords));
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
}

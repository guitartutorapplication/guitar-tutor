package com.example.jlo19.guitartutor.adapters;

import android.app.BuildConfig;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.models.retrofit.objects.Song;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

/**
 * Testing SongsListAdapter
 */
@PrepareForTest(LayoutInflater.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class SongsListAdapterTest {

    private SongsListAdapter adapter;
    private List<Song> songs;

    @Before
    public void setUp() {
        List<Chord> chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", 1));
        songs = Arrays.asList(
                new Song("Adventure of a Lifetime", "Coldplay", "contents", chords),
                new Song("Dance with Me Tonight", "Olly Murs", "contents", chords));

        adapter = new SongsListAdapter(RuntimeEnvironment.application, R.layout.song_list_item, songs);
    }

    @Test
    public void getViewWithView_SetsTextOnTextViewsInListItem() {
        // arrange
        View view = Mockito.mock(View.class);

        TextView txtSongTitle = Mockito.mock(TextView.class);
        TextView txtSongArtist = Mockito.mock(TextView.class);
        TextView txtSongChords = Mockito.mock(TextView.class);
        Mockito.when(view.findViewById(R.id.txtSongTitle)).thenReturn(txtSongTitle);
        Mockito.when(view.findViewById(R.id.txtSongArtist)).thenReturn(txtSongArtist);
        Mockito.when(view.findViewById(R.id.txtSongChords)).thenReturn(txtSongChords);

        // act
        int position = 0;
        adapter.getView(position, view, null);

        // assert
        List<Chord> expectedChords = songs.get(position).getChords();
        String expectedChordsList = "";
        for (int i = 0; i < expectedChords.size(); i++) {
            // if last in list, no comma at end
            if (i == expectedChords.size() - 1) {
                expectedChordsList += expectedChords.get(i).toString();
            } else {
                expectedChordsList += expectedChords.get(i).toString() + ", ";
            }
        }

        Mockito.verify(txtSongTitle).setText(songs.get(position).getTitle());
        Mockito.verify(txtSongArtist).setText(songs.get(position).getArtist());
        Mockito.verify(txtSongChords).setText(expectedChordsList);
    }
}

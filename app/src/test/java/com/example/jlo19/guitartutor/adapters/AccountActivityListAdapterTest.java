package com.example.jlo19.guitartutor.adapters;

import android.app.BuildConfig;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;

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
 * Testing AccountActivityListAdapter
 */
@PrepareForTest(LayoutInflater.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class AccountActivityListAdapterTest {

    private AccountActivityListAdapter adapter;
    private List<Chord> chords;

    @Before
    public void setUp() {
        chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1, 25),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1, 23));

        adapter = new AccountActivityListAdapter(RuntimeEnvironment.application,
                R.layout.song_list_item, chords);
    }

    @Test
    public void getViewWithView_SetsTextOnTextViewsInListItem() {
        // arrange
        View view = Mockito.mock(View.class);

        TextView txtPractiseNum = Mockito.mock(TextView.class);
        TextView txtChord = Mockito.mock(TextView.class);
        Mockito.when(view.findViewById(R.id.txtPractiseNum)).thenReturn(txtPractiseNum);
        Mockito.when(view.findViewById(R.id.txtChord)).thenReturn(txtChord);

        // act
        int position = 0;
        adapter.getView(position, view, null);

        // assert
        Mockito.verify(txtPractiseNum).setText(String.valueOf(chords.get(position)
                .getNumTimesPractised()));
        Mockito.verify(txtChord).setText(chords.get(position).toString());
    }
}

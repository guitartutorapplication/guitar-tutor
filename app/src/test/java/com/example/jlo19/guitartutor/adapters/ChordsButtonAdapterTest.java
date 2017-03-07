package com.example.jlo19.guitartutor.adapters;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Button;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.models.Chord;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

/**
 * Testing ChordsButtonAdapter
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class ChordsButtonAdapterTest {

    private ChordsButtonAdapter adapter;
    private List<Chord> chords;

    @Before
    public void setUp() {
        chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
        View.OnClickListener listener = PowerMockito.mock(View.OnClickListener.class);
        Context context = RuntimeEnvironment.application;

        adapter = new ChordsButtonAdapter(context, chords, listener);
    }

    @Test
    public void getCount_ReturnsNumberOfChords() {
        // act
        int actual = adapter.getCount();

        // assert
        Assert.assertEquals(chords.size(), actual);
    }

    @Test
    public void getItem_ReturnsChordAtSpecifiedPostion() {
        int position = 0;

        // act
        Chord actual = (Chord) adapter.getItem(position);

        // assert
        Assert.assertEquals(chords.get(position), actual);
    }

    @Test
    public void getItemId_ReturnsPosition() {
        int position = 0;

        // act
        int actual = (int) adapter.getItemId(position);

        // assert
        Assert.assertEquals(position, actual);
    }

    @Test
    public void getView_ReturnsButtonWithIdAndText() {
        int position = 0;

        // act
        Button button = (Button) adapter.getView(position, null , null);

        // assert
        Assert.assertEquals(position, button.getId());
        Assert.assertEquals(chords.get(position).toString(), button.getText());
    }

}

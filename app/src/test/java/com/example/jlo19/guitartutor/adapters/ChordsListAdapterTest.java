package com.example.jlo19.guitartutor.adapters;

import android.app.Application;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.models.Chord;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

/**
 * Testing ChordsListAdapter
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class ChordsListAdapterTest {
    private List<Chord> chords;
    private Application context;
    private ChordsListAdapter adapter;

    @Before
    public void setUp() {
        chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4", "A.wav", 1),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4", "B.wav", 1));

        context = RuntimeEnvironment.application;

        adapter = new ChordsListAdapter(context, chords);
    }

    @Test
    public void getCount_ReturnsSizeOfChordsListPlusDefault() {
        // act
        int actualCount = adapter.getCount();

        // assert
        Assert.assertEquals(chords.size() + 1, actualCount);
    }

    @Test
    public void getItemAtPosition0_ReturnsNull() {
        // act
        Object actualItem = adapter.getItem(0);

        // assert
        Assert.assertNull(actualItem);
    }

    @Test
    public void getItemAtPosition1_ReturnsFirstChord() {
        // act
        int position = 1;
        Chord actualItem = (Chord) adapter.getItem(position);

        // assert
        Assert.assertEquals(chords.get(position-1), actualItem);
    }

    @Test
    public void getItemId_ReturnsPosition() {
        // act
        int position = 1;
        int actualItemId = (int) adapter.getItemId(position);

        // assert
        Assert.assertEquals(position, actualItemId);
    }

    @Test
    public void getDropDownViewAtPosition0_ReturnsViewWithDefaultText() {
        // act
        View view = adapter.getDropDownView(0, null, null);

        // assert
        TextView actualText = (TextView) view.findViewById(R.id.text);
        Assert.assertEquals(context.getResources().getString(R.string.select_chord_instruction),
                actualText.getText());
    }

    @Test
    public void getDropDownViewAtPosition1_ReturnsViewWithTextOfFirstChord() {
        // act
        int position = 1;
        View view = adapter.getDropDownView(position, null, null);

        // assert
        TextView actualText = (TextView) view.findViewById(R.id.text);
        Assert.assertEquals(chords.get(position-1).toString(),
                actualText.getText());
    }

    @Test
    public void getViewAtPosition0_ReturnsViewWithDefaultText() {
        // act
        View view = adapter.getView(0, null, null);

        // assert
        TextView actualText = (TextView) view.findViewById(android.R.id.text1);
        Assert.assertEquals(context.getResources().getString(R.string.select_chord_instruction),
                actualText.getText());
    }

    @Test
    public void getViewAtPosition1_ReturnsViewWithTextOfFirstChord() {
        // act
        int position = 1;
        View view = adapter.getView(position, null, null);

        // assert
        TextView actualText = (TextView) view.findViewById(android.R.id.text1);
        Assert.assertEquals(chords.get(position-1).toString(),
                actualText.getText());
    }
}

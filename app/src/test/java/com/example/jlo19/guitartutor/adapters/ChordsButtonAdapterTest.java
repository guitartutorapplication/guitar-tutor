package com.example.jlo19.guitartutor.adapters;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jlo19.guitartutor.BuildConfig;
import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.models.retrofit.Chord;

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
    private Context context;

    @Before
    public void setUp() {
        chords = Arrays.asList(
                new Chord(1, "A", "MAJOR", "A.png", "A.mp4"),
                new Chord(2, "B", "MAJOR", "B.png", "B.mp4"));
        View.OnClickListener listener = PowerMockito.mock(View.OnClickListener.class);
        context = RuntimeEnvironment.application;

        adapter = new ChordsButtonAdapter(context, chords, listener);
    }

    @Test
    public void getCount_ReturnsNumberOfChords() {
        // act
        int actualCount = adapter.getCount();

        // assert
        Assert.assertEquals(chords.size(), actualCount);
    }

    @Test
    public void getItem_ReturnsChordAtSpecifiedPostion() {
        int position = 0;

        // act
        Chord actualChord = (Chord) adapter.getItem(position);

        // assert
        Assert.assertEquals(chords.get(position), actualChord);
    }

    @Test
    public void getItemId_ReturnsPosition() {
        int position = 0;

        // act
        int actualId = (int) adapter.getItemId(position);

        // assert
        Assert.assertEquals(position, actualId);
    }

    @Test
    public void getView_ReturnsButton() {
        int position = 0;

        // act
        Button button = (Button) adapter.getView(position, null , null);

        // assert
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int expectedWidthHeightPx = (int) ((context.getResources().getDimension(
                R.dimen.chord_button_width_height) * displayMetrics.density) + 0.5);

        ViewGroup.LayoutParams actualLayoutParams = button.getLayoutParams();

        Assert.assertEquals(expectedWidthHeightPx, actualLayoutParams.height);
        Assert.assertEquals(expectedWidthHeightPx, actualLayoutParams.width);
        Assert.assertEquals(position, button.getId());
        Assert.assertEquals(chords.get(position).toString(), button.getText());
        Assert.assertEquals(Gravity.TOP|Gravity.CENTER_HORIZONTAL, button.getGravity());
        Assert.assertEquals(context.getResources().getDimension(R.dimen.chord_button_text_size),
                button.getTextSize());

    }

}

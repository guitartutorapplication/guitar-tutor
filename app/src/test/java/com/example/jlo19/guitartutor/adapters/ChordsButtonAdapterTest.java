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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Testing ChordsButtonAdapter
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class ChordsButtonAdapterTest {

    private ChordsButtonAdapter adapter;
    private Context context;

    @Before
    public void setUp() {
        View.OnClickListener listener = PowerMockito.mock(View.OnClickListener.class);
        context = RuntimeEnvironment.application;
        adapter = new ChordsButtonAdapter(context, listener);
    }

    @Test
    public void addButtons_GetCount_ReturnsNumberOfButtons() {
        // arrange
        adapter.addButton(0);
        adapter.addButton(1);

        // act
        int actualCount = adapter.getCount();

        // assert
        Assert.assertEquals(2, actualCount);
    }

    @Test
    public void addButton_GetItem_ReturnsButton() {
        // arrange
        int position = 0;
        adapter.addButton(position);

        // act
        Button button = (Button) adapter.getItem(position);

        // assert
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int expectedWidthHeightPx = (int) ((context.getResources().getDimension(
                R.dimen.chord_button_width_height) * displayMetrics.density) + 0.5);
        ViewGroup.LayoutParams actualLayoutParams = button.getLayoutParams();

        Assert.assertEquals(expectedWidthHeightPx, actualLayoutParams.height);
        Assert.assertEquals(expectedWidthHeightPx, actualLayoutParams.width);
        Assert.assertEquals(context.getResources().getDimension(
                R.dimen.chord_button_text_size), button.getTextSize());
        Assert.assertEquals(position, button.getId());
        Assert.assertTrue(button.hasOnClickListeners());
        Assert.assertEquals(Gravity.TOP|Gravity.CENTER_HORIZONTAL, button.getGravity());
    }

    @Test
    public void addButton_GetView_ReturnsButton() {
        // arrange
        int position = 0;
        adapter.addButton(position);

        // act
        Button button = (Button) adapter.getView(position, null, null);

        // assert
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int expectedWidthHeightPx = (int) ((context.getResources().getDimension(
                R.dimen.chord_button_width_height) * displayMetrics.density) + 0.5);
        ViewGroup.LayoutParams actualLayoutParams = button.getLayoutParams();

        Assert.assertEquals(expectedWidthHeightPx, actualLayoutParams.height);
        Assert.assertEquals(expectedWidthHeightPx, actualLayoutParams.width);
        Assert.assertEquals(context.getResources().getDimension(
                R.dimen.chord_button_text_size), button.getTextSize());
        Assert.assertEquals(position, button.getId());
        Assert.assertTrue(button.hasOnClickListeners());
        Assert.assertEquals(Gravity.TOP|Gravity.CENTER_HORIZONTAL, button.getGravity());
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
    public void addButtonAndSetText_GetItem_ReturnsButtonWithText() {
        // arrange
        int id = 0;
        String text = "A";
        adapter.addButton(id);
        adapter.setButtonText(id, text);

        // act
        Button button = (Button) adapter.getItem(id);

        // assert
        Assert.assertEquals(text, button.getText());
    }

    @Test
    public void addButtonAndSetText_GetView_ReturnsButtonWithText() {
        // arrange
        int id = 0;
        String text = "A";
        adapter.addButton(id);
        adapter.setButtonText(id, text);

        // act
        Button button = (Button) adapter.getView(id, null, null);

        // assert
        Assert.assertEquals(text, button.getText());
    }

    @Test
    public void addButtonsAndEnableButtonWithTrue_GetItem_ReturnsEnabledButton() {
        // arrange
        int id = 0;
        boolean isEnabled = true;
        adapter.addButton(id);
        adapter.enableButton(id, isEnabled);

        // act
        Button button = (Button) adapter.getItem(id);

        // assert
        Assert.assertEquals(isEnabled, button.isEnabled());
    }

    @Test
    public void addButtonsAndEnableButtonWithFalse_GetItem_ReturnsDisabledButton() {
        // arrange
        int id = 0;
        boolean isEnabled = false;
        adapter.addButton(id);
        adapter.enableButton(id, isEnabled);

        // act
        Button button = (Button) adapter.getItem(id);

        // assert
        Assert.assertEquals(isEnabled, button.isEnabled());
    }

    @Test
    public void addButtonsAndEnableButtonWithTrue_GetView_ReturnsEnabledButton() {
        // arrange
        int id = 0;
        boolean isEnabled = true;
        adapter.addButton(id);
        adapter.enableButton(id, isEnabled);

        // act
        Button button = (Button) adapter.getView(id, null, null);

        // assert
        Assert.assertEquals(isEnabled, button.isEnabled());
    }

    @Test
    public void addButtonsAndEnableButtonWithFalse_GetView_ReturnsDisabledButton() {
        // arrange
        int id = 0;
        boolean isEnabled = false;
        adapter.addButton(id);
        adapter.enableButton(id, isEnabled);

        // act
        Button button = (Button) adapter.getView(id, null, null);

        // assert
        Assert.assertEquals(isEnabled, button.isEnabled());
    }

    @Test
    public void addButtonAndSetButtonBackground_GetItem_ReturnsButtonWithBackground() {
        // arrange
        int id = 0;
        String doneIdentifier = "done_";
        String levelNumIdentifier = "one";
        adapter.addButton(id);
        adapter.setButtonBackground(id, doneIdentifier, levelNumIdentifier);

        // act
        Button button = (Button) adapter.getItem(id);

        // assert
        Assert.assertEquals(context.getDrawable(R.drawable.chord_done_level_one_button),
                button.getBackground());
    }

    @Test
    public void addButtonAndSetButtonBackground_GetView_ReturnsButtonWithBackground() {
        // arrange
        int id = 0;
        String doneIdentifier = "done_";
        String levelNumIdentifier = "one";
        adapter.addButton(id);
        adapter.setButtonBackground(id, doneIdentifier, levelNumIdentifier);

        // act
        Button button = (Button) adapter.getView(id, null, null);

        // assert
        Assert.assertEquals(context.getDrawable(R.drawable.chord_done_level_one_button),
                button.getBackground());
    }
}

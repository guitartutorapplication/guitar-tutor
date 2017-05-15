package com.example.jlo19.guitartutor.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.application.App;
import com.example.jlo19.guitartutor.enums.BeatSpeed;
import com.example.jlo19.guitartutor.enums.ChordChange;
import com.example.jlo19.guitartutor.models.retrofit.objects.Chord;
import com.example.jlo19.guitartutor.presenters.interfaces.IPractisePresenter;
import com.example.jlo19.guitartutor.views.PractiseView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Activity that runs the practise activity
 */
public class PractiseActivity extends BaseWithToolbarActivity implements PractiseView{

    private IPractisePresenter presenter;
    private Button btnStop;
    private SoundPool soundPool;
    private TextView txtCountdown;
    private TextView txtFirstChordInstruction;
    private TextView txtFirstChord;
    private List<Integer> soundIds;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    SoundPool.OnLoadCompleteListener onLoadCompleteListener;

    @Override
    public int getLayout() {
        return R.layout.activity_practise;
    }

    @Override
    public String getToolbarTitle() {
        return getResources().getString(R.string.practising_chords_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setSoundPool(new SoundPool.Builder().setMaxStreams(4).build());

        txtCountdown = (TextView) findViewById(R.id.txtCountdown);
        txtFirstChordInstruction = (TextView) findViewById(R.id.txtFirstChordInstruction);
        txtFirstChord = (TextView) findViewById(R.id.txtFirstChord);

        // allows injection of presenter
        App.getComponent().inject(this);

        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.viewOnStopPractising();
                }
            });
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void setSoundPool(SoundPool soundPool) {
        this.soundPool = soundPool;
        onLoadCompleteListener = new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                presenter.viewOnSoundLoaded(status);
            }
        };
        soundPool.setOnLoadCompleteListener(onLoadCompleteListener);
    }

    @Inject
    public void setPresenter(IPractisePresenter presenter) {
        this.presenter = presenter;
        presenter.setView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.viewOnDestroy();
        soundPool.release();
        soundPool = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.viewOnPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.viewOnStop();
    }

    @Override
    public List<Chord> getSelectedChords() {
        return getIntent().getExtras().getParcelableArrayList("CHORDS");
    }

    @Override
    public void setChordText(final String chord) {
        final TextView txtChord = (TextView) findViewById(R.id.txtChord);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtChord.setText(chord);
            }
        });
    }

    @Override
    public void showStopButton() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnStop.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void playSound(int index) {
        if (soundPool != null) {
            soundPool.play(soundIds.get(index), 1.0f, 1.0f, 1, 0, 1f);
        }
    }

    @Override
    public void showError() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.practise_error_occurred_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.viewOnConfirmError();
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void returnToPractiseSetup() {
        finish();
    }

    @Override
    public void loadSounds(List<String> filenames) {
        soundIds = new ArrayList<>();
        for (String filename : filenames) {
            int resource = getResources().getIdentifier(
                    filename, "raw", getPackageName());
            soundIds.add(soundPool.load(this, resource, 1));
        }
    }

    @Override
    public ChordChange getChordChange() {
        return (ChordChange) getIntent().getSerializableExtra("CHORD_CHANGE");
    }

    @Override
    public BeatSpeed getBeatSpeed() {
        return (BeatSpeed) getIntent().getSerializableExtra("BEAT_SPEED");
    }

    @Override
    public void setCountdownText(final String second) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtCountdown.setText(second);
            }
        });
    }

    @Override
    public void hideCountdown() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtCountdown.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void hideFirstChordInstruction() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtFirstChord.setVisibility(View.INVISIBLE);
                txtFirstChordInstruction.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void setFirstChordText(String firstChord) {
        txtFirstChord.setText(firstChord);
    }

    @Override
    public void showPractiseSessionSaveSuccess(int achievements) {
        String text = getString(R.string.save_practise_session_success_message) + "\n" +
                getString(R.string.gained_15_achievements_message, achievements);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.viewOnConfirmSuccess();
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void showPractiseSessionSaveError() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.save_practise_session_error_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.viewOnConfirmError();
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void showPractiseSessionSaveSuccess(int level, int achievements) {
        String text = getString(R.string.save_practise_session_success_message) + "\n" +
                getString(R.string.gained_15_achievements_message, achievements) + "\n" +
                getString(R.string.new_level_message, level);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.viewOnConfirmSuccess();
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }

    @Override
    public void showPractiseSessionSaveSuccess() {
        String text = getString(R.string.save_practise_session_success_message) + "\n" +
                getString(R.string.maximum_achievements_message);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.viewOnConfirmSuccess();
                    }
                }).create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,
                R.color.colorAccent));
    }
}

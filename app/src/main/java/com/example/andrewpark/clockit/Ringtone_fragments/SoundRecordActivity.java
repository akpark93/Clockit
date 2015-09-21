package com.example.andrewpark.clockit.Ringtone_fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewpark.clockit.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.io.IOException;

/**
 * Created by andrewpark on 9/10/15.
 */
public class SoundRecordActivity extends Activity {

    private static final String LOG_TAG = SoundRecordActivity.class.getSimpleName();

    DonutProgress donutProgress;

    private MediaRecorder myAudioRecorder = null;
    private Context mContext;
    private Button record_btn, stop_btn;
    private String outputFile = null;
    private String newRecordedFile = "";
    private TextView label_text;
    private TextView recordingText;
    private TextView page_title;

    SeekBar seekbar;
    private Button play_btn, pause_btn;
    private EditText new_record_label;
    MediaPlayer mediaPlayer = null;
    Handler seekHandler = new Handler();

    CountDownTimer countDownTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = new MediaPlayer();
        mContext = getApplicationContext();
        setContentView(R.layout.record_activity);

        getInit();
        setUp();

    }

    private void getInit() {
        Typeface kgp = Typeface.createFromAsset(mContext.getAssets(),"kgp.ttf");
        record_btn = (Button)findViewById(R.id.record_button);
        stop_btn = (Button)findViewById(R.id.stop_button);
        stop_btn.setEnabled(false);
        stop_btn.setVisibility(View.INVISIBLE);

        donutProgress = (DonutProgress)findViewById(R.id.donut_progress);

        recordingText = (TextView)findViewById(R.id.recording_text);
        recordingText.setTypeface(kgp);
        recordingText.setVisibility(View.INVISIBLE);

        page_title = (TextView)findViewById(R.id.record_fragment_title);
        page_title.setTypeface(kgp);

        label_text = (TextView)findViewById(R.id.new_record_label);
        label_text.setTypeface(kgp);

        new_record_label = (EditText)findViewById(R.id.ringtone_label_edittext);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUp() {
        myAudioRecorder = new MediaRecorder();

        final boolean[] mStartRecording = {true};

        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new_record_label.getText().toString().isEmpty()) {
                    Toast.makeText(mContext,"Put in a label!",Toast.LENGTH_LONG).show();
                } else {
                    //create outputfile name
                    newRecordedFile = new_record_label.getText().toString();
                    Log.v(LOG_TAG,"newrecordedfile: " + newRecordedFile);
                    outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + newRecordedFile + ".3gp";

                    startRecording();

                    record_btn.setEnabled(false);
                    record_btn.setVisibility(View.INVISIBLE);

                    stop_btn.setEnabled(true);
                    stop_btn.setVisibility(View.VISIBLE);

                    startProgressBar();

                    Toast.makeText(mContext, "Recording started", Toast.LENGTH_LONG).show();
                }
            }
        });


        stop_btn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                donutProgress.setProgress(0);
                stopRecording();

                stop_btn.setEnabled(false);
                stop_btn.setVisibility(View.INVISIBLE);
                record_btn.setEnabled(true);
                record_btn.setVisibility(View.VISIBLE);

                Toast.makeText(mContext, "Audio recorded successfully", Toast.LENGTH_LONG).show();

                showSaveDialog();
            }
        });
    }

    //initiate donut progress bar
    private void startProgressBar() {
        donutProgress.setMax(30);
        countDownTimer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {
                donutProgress.setProgress(donutProgress.getProgress()+1);
            }
            @Override
            public void onFinish() {
                Toast.makeText(mContext, "Time's Up!", Toast.LENGTH_LONG).show();
                stopRecording();
                showSaveDialog();
            }
        }.start();
    }

    private void showSaveDialog() {
        final AlertDialog.Builder new_ringtone_builder = new AlertDialog.Builder(SoundRecordActivity.this);
        new_ringtone_builder.setTitle("Keep Ringtone?");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View record_layout_view = inflater.inflate(R.layout.record_layout_keep, null);
        try {
            setPlayPauseButton(record_layout_view);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new_ringtone_builder.setView(record_layout_view);
        new_ringtone_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //save ringtone into folder
                mediaPlayer.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;
                Toast.makeText(mContext,"Check the Music Folder!", Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mediaPlayer.stop();
                dialogInterface.dismiss();
            }
        });
        new_ringtone_builder.create().show();
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                seekUpdation();
            }
        }
    };

    private void seekUpdation() {
        seekbar.setMax(mediaPlayer.getDuration());
        seekbar.setProgress(mediaPlayer.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
    }

    private void setPlayPauseButton(View view) throws IOException {

        seekbar = (SeekBar)view.findViewById(R.id.seek_bar_record);
        play_btn = (Button)view.findViewById(R.id.play_button);
        pause_btn = (Button)view.findViewById(R.id.pause_btn);

        Log.v(LOG_TAG,"output file: " + outputFile);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlaying();
                seekUpdation();

                pause_btn.setEnabled(true);
                play_btn.setEnabled(false);

                Toast.makeText(mContext, "Playing audio", Toast.LENGTH_LONG).show();
            }
        });
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();

                pause_btn.setEnabled(false);
                play_btn.setEnabled(true);

                Toast.makeText(mContext, "Audio Paused", Toast.LENGTH_LONG).show();
            }
        });
    }

    //start playing audio
    private void startPlaying() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(outputFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //stop playing audio
    private void stopPlaying() {
        mediaPlayer.release();
        mediaPlayer = null;
    }

    //start recording audio
    private void startRecording(){
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setMaxDuration(30000);
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setOutputFile(outputFile);
        Log.v(LOG_TAG,"outputFile name: " + outputFile);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            myAudioRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        myAudioRecorder.start();
    }

    //stop recording audio
    private void stopRecording() {
        myAudioRecorder.stop();
    }

}

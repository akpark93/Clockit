package com.example.andrewpark.clockit.SingleActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.andrewpark.clockit.Main_fragments.TimerFragment;
import com.example.andrewpark.clockit.R;
import com.example.andrewpark.clockit.model.Timer;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;

/**
 * Created by andrewpark on 9/12/15.
 */
public class TimerScreen extends Activity {

    private static final String LOG_TAG = TimerScreen.class.getSimpleName();

    private Context mContext;

    private final int REFRESH_RATE = 500;

    private TextView timer_text;
    private Button delete_timer;
    private Button timer_screen_play;
    private Button timer_screen_pause;
    private Button timer_screen_refresh;
    private Timer timer;
    private java.util.Timer timerscheduler;
    private DonutProgress timer_progress_donut;

    private Handler myHandler;
    private int position;

    private ArrayList<Timer> timers = TimerFragment.timers;
    private ArrayList<String> timers_str = TimerFragment.timers_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_screen_activity);

        mContext = getApplicationContext();
        getActionBar().setTitle("Timer");
        position = getIntent().getIntExtra("position", -1);
        myHandler = new Handler();
        timer = timers.get(position);

        getInit();
        setUpButtons();
        setUpTimer();
    }

    private void getInit() {
        Typeface kgp = Typeface.createFromAsset(getAssets(),"kgp.ttf");
        timer_text = (TextView) findViewById(R.id.timer_progress_time);
        timer_text.setTypeface(kgp);
        timer_progress_donut = (DonutProgress) findViewById(R.id.timer_donut_progress);
        setUpDonut();
        delete_timer = (Button) findViewById(R.id.timer_screen_delete_btn);
        timer_screen_play = (Button) findViewById(R.id.timer_screen_play_btn);
        timer_screen_pause = (Button) findViewById(R.id.timer_screen_pause_btn);
        timer_screen_refresh = (Button) findViewById(R.id.timer_screen_refresh_btn);
        if (timer.isPaused()) {
            setPlayVisible();
        }else{
            setPauseVisible();
        }
    }
    @SuppressLint("ResourceAsColor")
    private void setUpDonut() {
        timer_progress_donut.setProgress(0);
        long ms = timer.getTotalMs();
        int total_sec = (int)(ms/1000);
        timer_progress_donut.setMax(total_sec - 1);
    }

    private void setUpButtons() {
        delete_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pause,delete,and go back to previous activity
                if (position != -1) {
                    timer.cancel();
                    timers.remove(position);
                    timers_str.remove(position);
                    finish();
                }
            }
        });
        timer_screen_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPauseVisible();
                timer.play();
                timer.setIsPaused(false);
                myHandler.postDelayed(startTimer,0);
            }
        });

        timer_screen_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPlayVisible();
                timer.pause();
                timer.setIsPaused(true);
                myHandler.removeCallbacks(startTimer);
            }
        });

        timer_screen_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder refresh_dialog = new AlertDialog.Builder(TimerScreen.this)
                        .setTitle("Refresh?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                timer.refresh();
                                timer_text.setText(timer.getTimer_text());
                                timer_progress_donut.setProgress(0);
                                setPlayVisible();
                            }
                        });
                refresh_dialog.create().show();
            }
        });
    }

    private void setUpTimer() {
        myHandler.postDelayed(startTimer, 0);
    }

    private Runnable startTimer = new Runnable() {
        @Override
        public void run() {
            if (timer.isDone()) {
                myHandler.removeCallbacks(startTimer);
                return;
            }
            timer_text.setText(timer.getTimer_text());
            timer_progress_donut.setProgress((int) (timer.getElapsedTime() / 1000));
            myHandler.postDelayed(this,REFRESH_RATE);
        }
    };

    private void setPlayVisible() {
        timer_screen_pause.setEnabled(false);
        timer_screen_pause.setVisibility(View.INVISIBLE);
        timer_screen_play.setEnabled(true);
        timer_screen_play.setVisibility(View.VISIBLE);
    }

    private void setPauseVisible() {
        timer_screen_play.setEnabled(false);
        timer_screen_play.setVisibility(View.INVISIBLE);
        timer_screen_pause.setEnabled(true);
        timer_screen_pause.setVisibility(View.VISIBLE);
    }
}

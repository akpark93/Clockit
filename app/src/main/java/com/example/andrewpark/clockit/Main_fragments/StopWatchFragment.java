package com.example.andrewpark.clockit.Main_fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.andrewpark.clockit.Adapter.LapListAdapter;
import com.example.andrewpark.clockit.R;

import java.util.ArrayList;

/**
 * Created by andrewpark on 9/11/15.
 */
public class StopWatchFragment extends Fragment {

    private static final String LOG_TAG = StopWatchFragment.class.getSimpleName();

    private final int REFRESH_RATE = 10;

    private long startTime;
    private long elapsedTime;

    private String hours,minutes,seconds,milliseconds;
    private long secs,mins,hrs,msecs;
    private String current_time, current_time_ms;

    private Context mContext;

    private TextView stopwatch_time, stopwatch_time_ms;
    private Button stopwatch_play_btn = null;
    private Button stopwatch_stop_btn = null;
    private Button stopwatch_lap_btn = null;
    private Button stopwatch_reset_btn = null;
    private ListView stopwatch_listview;

    private boolean stopped;

    private LapListAdapter mLapAdapter;
    private ArrayList<String> lap_times;

    private Handler myHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stopwatch_fragment,container,false);

        mContext = getContext();
        myHandler = new Handler();
        stopped = false;
        getInit(view);
        setUpButtons(view);

        return view;
    }

    private void getInit(View view) {
        Typeface trench = Typeface.createFromAsset(getContext().getAssets(),"kgp.ttf");
        stopwatch_time = (TextView)view.findViewById(R.id.stopwatch_time);
        stopwatch_time.setTypeface(trench);
        stopwatch_time_ms = (TextView)view.findViewById(R.id.stopwatch_time_ms);
        stopwatch_time_ms.setTypeface(trench);
        stopwatch_play_btn = (Button)view.findViewById(R.id.stopwatch_play_button);
        stopwatch_stop_btn = (Button)view.findViewById(R.id.stopwatch_stop_button);
        stopwatch_stop_btn.setEnabled(false);
        stopwatch_lap_btn = (Button)view.findViewById(R.id.stopwatch_lap_button);
        stopwatch_reset_btn = (Button)view.findViewById(R.id.stopwatch_reset_button);

        lap_times = new ArrayList<String>();
        mLapAdapter = new LapListAdapter(mContext, R.layout.lap_list_item, R.id.stop_watch_listview, lap_times);
        stopwatch_listview = (ListView)view.findViewById(R.id.stop_watch_listview);
        stopwatch_listview.setAdapter(mLapAdapter);
    }

    private void setUpButtons(View view) {
        stopwatch_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startClick();
            }
        });
        stopwatch_stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopClick();
            }
        });
        stopwatch_lap_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lapClick();
            }
        });
        stopwatch_reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetClick();
            }
        });
    }

    private Runnable startTimer = new Runnable() {
        @Override
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            myHandler.postDelayed(this, REFRESH_RATE);
        }
    };

    private void updateTimer(float time) {
        msecs = (long) time;
        secs = (long)(time/1000);
        mins = (long)((time/1000)/60);
        hrs = (long)(((time/1000)/60)/60);

        secs = secs % 60;
        seconds = String.valueOf(secs);
        if (secs == 0) {
            seconds = "00";
        }
        if (secs < 10 && secs > 0) {
            seconds = "0" + seconds;
        }

        mins = mins % 60;
        minutes = String.valueOf(mins);
        if (mins == 0) {
            minutes = "00";
        }
        if (mins < 10 && mins > 0) {
            minutes = "0" + minutes;
        }

        hrs = hrs % 60;
        hours = String.valueOf(hrs);
        if (hrs==0) {
            hours = "0";
        }
        if (hrs < 10 && hrs > 0) {
            hours = "" + hrs;
        }

        long msecs_value = (msecs%1000)/10;
        milliseconds = String.valueOf((long)msecs);
        if (msecs_value == 0) {
            milliseconds = "00";
        } else if (msecs_value > 0 && msecs_value < 10) {
            milliseconds = "0" + msecs_value;
        } else {
            milliseconds = "" + msecs_value;
        }

        current_time = hours + ":" + minutes + ":" + seconds;
        stopwatch_time.setText(current_time);

        current_time_ms = "." + milliseconds;
        stopwatch_time_ms.setText(current_time_ms);
    }

    private void startClick() {
        stopwatch_play_btn.setEnabled(false);
        stopwatch_play_btn.setVisibility(View.INVISIBLE);
        stopwatch_stop_btn.setEnabled(true);
        stopwatch_stop_btn.setVisibility(View.VISIBLE);
        if (stopped) {
            //get where it stopped
            startTime = System.currentTimeMillis() - elapsedTime;
        } else {
            //start over
            startTime = System.currentTimeMillis();
        }
        myHandler.removeCallbacks(startTimer);
        myHandler.postDelayed(startTimer,0);
    }

    private void stopClick() {
        stopwatch_stop_btn.setEnabled(false);
        stopwatch_stop_btn.setVisibility(View.INVISIBLE);
        stopwatch_play_btn.setEnabled(true);
        stopwatch_play_btn.setVisibility(View.VISIBLE);
        myHandler.removeCallbacks(startTimer);
        stopped = true;
    }

    private void lapClick() {
        lap_times.add(current_time + current_time_ms);
        mLapAdapter.notifyDataSetChanged();
    }

    private void resetClick() {
        stopClick();
        stopwatch_time.setText("00:00:00");
        stopwatch_time_ms.setText(".0");
        lap_times.clear();
        mLapAdapter.notifyDataSetChanged();
    }
}

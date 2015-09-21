package com.example.andrewpark.clockit.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.andrewpark.clockit.Main_fragments.TimerFragment;
import com.example.andrewpark.clockit.R;
import com.example.andrewpark.clockit.model.Timer;

import java.util.ArrayList;

/**
 * Created by andrewpark on 9/12/15.
 */
public class TimerListAdapter extends ArrayAdapter<String> {

    private static final String LOG_TAG = TimerListAdapter.class.getSimpleName();

    private TextView timer_text_item;
    private Button play_timer_list_btn;
    private Button pause_timer_list_btn;
    private Button delete_timer_btn;
    private ProgressBar progress_hourglass_playing;
    private ImageView progress_hourglass_paused;

    private ArrayList<String> timers_str = TimerFragment.timers_string;
    private ArrayList<Timer> timers = TimerFragment.timers;

    public TimerListAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> objects) {
        super(context, resource, textViewResourceId, objects);
        this.timers_str = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.timer_list_item, parent, false);
        }

        Timer timer = timers.get(position);

        Typeface kgp = Typeface.createFromAsset(getContext().getAssets(),"kgp.ttf");
        String timer_str = getItem(position);
        timer_text_item = (TextView)view.findViewById(R.id.timer_list_item_time);
        timer_text_item.setTypeface(kgp);
        timer_text_item.setText(timer_str);

        progress_hourglass_playing = (ProgressBar)view.findViewById(R.id.hourglass_icon_progress);
        progress_hourglass_paused = (ImageView)view.findViewById(R.id.hourglass_icon);
        progress_hourglass_paused.setVisibility(View.INVISIBLE);

        setUpButtons(view, timer);

        return view;
    }

    private void setUpButtons(View view, final Timer timer) {
        play_timer_list_btn = (Button)view.findViewById(R.id.timer_list_play_btn);
        pause_timer_list_btn = (Button)view.findViewById(R.id.timer_list_pause_btn);
        if (timer.isPaused()) {
            setPlayVisible();
            progress_hourglass_playing.setVisibility(View.INVISIBLE);
            progress_hourglass_paused.setVisibility(View.VISIBLE);
        } else {
            setPauseVisible();
            progress_hourglass_paused.setVisibility(View.INVISIBLE);
            progress_hourglass_playing.setVisibility(View.VISIBLE);
        }

        play_timer_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //continue timer
                timer.play();
                timer.setIsPaused(false);
                setPauseVisible();
            }
        });

        pause_timer_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pause timer
                timer.pause();
                timer.setIsPaused(true);
                setPlayVisible();
            }
        });
    }

    private void setPlayVisible() {
        pause_timer_list_btn.setEnabled(false);
        pause_timer_list_btn.setVisibility(View.INVISIBLE);
        play_timer_list_btn.setEnabled(true);
        play_timer_list_btn.setVisibility(View.VISIBLE);
    }

    private void setPauseVisible() {
        play_timer_list_btn.setEnabled(false);
        play_timer_list_btn.setVisibility(View.INVISIBLE);
        pause_timer_list_btn.setEnabled(true);
        pause_timer_list_btn.setVisibility(View.VISIBLE);
    }
}

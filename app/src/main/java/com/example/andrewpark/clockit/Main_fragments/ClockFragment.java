package com.example.andrewpark.clockit.Main_fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.andrewpark.clockit.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by andrewpark on 9/9/15.
 */
public class ClockFragment extends android.support.v4.app.Fragment {

    private TextClock textClock;

    private LinearLayout clockLayout;
    private AnalogClock analogClock;
    private Button analog_btn;
    boolean analog_open = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clock_fragment, container, false);

        getInit(view);
        setBackground(view);
        setUpButtons(view);

        return view;
    }

    private void getInit(View view) {
        clockLayout = (LinearLayout)view.findViewById(R.id.clock_fragment_layout);

        DateFormat df = new SimpleDateFormat("EEE, MMM d");
        String date = df.format(Calendar.getInstance().getTime());
        TextView date_txtView = (TextView)view.findViewById(R.id.clock_date_text);
        date_txtView.setText(date);
        Typeface kgp = Typeface.createFromAsset(getContext().getAssets(),"kgp.ttf");
        Typeface kg = Typeface.createFromAsset(getContext().getAssets(),"kg.ttf");
        date_txtView.setTypeface(kgp);

        textClock = (TextClock)view.findViewById(R.id.clock_time_text);
        textClock.setTypeface(kg);

        analogClock = (AnalogClock)view.findViewById(R.id.analog_clock);

    }

    private void setBackground(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour>=5 && hour<9) {
            clockLayout.setBackgroundResource(R.drawable.foggy_morning);
        } else if (hour>=9 && hour<19) {
            clockLayout.setBackgroundResource(R.drawable.morning_trail);
        } else {
            clockLayout.setBackgroundResource(R.drawable.red_sky);
        }
    }

    private void setUpButtons(View view) {
        analog_btn = (Button)view.findViewById(R.id.analog_clock_button);
        analog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (analog_open) {
                    //close
                    analogClock.setVisibility(View.GONE);
                    textClock.setVisibility(View.VISIBLE);
                    analog_btn.setAlpha((float)0.5);
                    analog_open=false;
                } else {
                    //open
                    textClock.setVisibility(View.GONE);
                    analogClock.setVisibility(View.VISIBLE);
                    analog_btn.setAlpha((float)1);
                    analog_open=true;
                }
            }
        });
    }


}

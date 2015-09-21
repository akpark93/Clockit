package com.example.andrewpark.clockit.SingleActivity;

import android.app.Activity;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.andrewpark.clockit.R;

/**
 * Created by andrewpark on 9/13/15.
 */
public class TimerAlarmScreen extends Activity {

    private Button dismiss_btn;
    private Ringtone r;
    private TextView timer_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_alarm_screen);
        getActionBar().setTitle("Timer Alarm");

        Typeface kgp = Typeface.createFromAsset(getAssets(),"kgp.ttf");
        timer_title = (TextView)findViewById(R.id.timer_alarm_screen_title);
        timer_title.setTypeface(kgp);

        dismiss_btn = (Button)findViewById(R.id.timer_dismiss_btn);
        setButtons();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }

    private void setButtons() {
        dismiss_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (r.isPlaying()) {
                    r.stop();
                    finish();
                }
            }
        });
    }
}

package com.example.andrewpark.clockit.SingleActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.andrewpark.clockit.R;
import com.example.andrewpark.clockit.Service.AlarmManagerHelper;

import java.io.IOException;


public class AlarmScreen extends Activity {

    public final String LOG_TAG = this.getClass().getSimpleName();


    private WakeLock mWakeLock;
    private MediaPlayer mPlayer;
    private Handler myHandler;
    private Vibrator vibrator;

    private static final int WAKELOCK_TIMEOUT = 60 * 1000;

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (mWakeLock==null) {
            mWakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), LOG_TAG);
        }

        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        myHandler = new Handler();

        Typeface kgp = Typeface.createFromAsset(getAssets(), "kgp.ttf");
        String name = getIntent().getStringExtra(AlarmManagerHelper.NAME);
        int timeHour = getIntent().getIntExtra(AlarmManagerHelper.TIME_HOUR, 0);
        int timeMinute = getIntent().getIntExtra(AlarmManagerHelper.TIME_MINUTE, 0);

        String tone = getIntent().getStringExtra(AlarmManagerHelper.TONE);
        Log.v(LOG_TAG,"tone: " + tone);

        TextView tvName = (TextView) findViewById(R.id.alarm_screen_name);
        tvName.setTypeface(kgp);
        tvName.setText(name);

        TextView tvTime = (TextView) findViewById(R.id.alarm_screen_time);
        tvTime.setTypeface(kgp);
        tvTime.setText(String.format("%02d : %02d", timeHour, timeMinute));

        TextView tvTitle = (TextView)findViewById(R.id.alarm_screen_title);
        tvTitle.setTypeface(kgp);

        final int snoozeTime = getIntent().getIntExtra(AlarmManagerHelper.SNOOZE, 5);
        final long snoozeTime_ms = snoozeTime*1000;

        Button dismissButton = (Button)findViewById(R.id.alarm_screen_dismiss);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.stop();
                vibrator.cancel();
                finish();
            }
        });

        Button snoozeButton = (Button)findViewById(R.id.alarm_screen_snooze);
        snoozeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start timer to start this activity again in 5 minutes
                CountDownTimer countDownTimer = new CountDownTimer(snoozeTime_ms,1000) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(getApplicationContext(), AlarmScreen.class);
                        getApplicationContext().startActivity(intent);
                    }
                };
                finish();
                vibrator.cancel();
                mPlayer.stop();
            }
        });

        mPlayer = new MediaPlayer();
        try {
            Log.v(LOG_TAG,"tone: " + tone);
            if (tone != null && !tone.equals("")) {
                if (tone.contains("storage")) {
                    mPlayer.setDataSource(tone);
                } else {
                    Uri toneUri = Uri.parse(tone);
                    if (toneUri != null) {
                        mPlayer.setDataSource(this, toneUri);
                        mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    }
                }
                mPlayer.setLooping(true);
                mPlayer.prepare();
                mPlayer.start();
                }
            } catch (IOException e1) {
            e1.printStackTrace();
        }

        boolean vibrate = getIntent().getBooleanExtra(AlarmManagerHelper.VIBRATE,false);
        long[] pattern = {0, 1000, 3000};
        vibrator = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrate) {
            vibrator.vibrate(pattern, 0);
        }

        boolean fading = getIntent().getBooleanExtra(AlarmManagerHelper.FADING,false);
        Log.v(LOG_TAG,"fading: " + fading);
        if (fading) {
            myHandler.postDelayed(increase,0);
        }

        Runnable releaseWakelock = new Runnable() {

            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                }
            }
        };

        new Handler().postDelayed(releaseWakelock, WAKELOCK_TIMEOUT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private float leftVol = 0;
    private float rightVol = 0;

    private Runnable increase = new Runnable() {
        @Override
        public void run() {
            mPlayer.setVolume(leftVol,rightVol);
            if (leftVol<1) {
                leftVol += .05;
                rightVol += .05;
                myHandler.postDelayed(increase,1000);
            }
        }
    };

}

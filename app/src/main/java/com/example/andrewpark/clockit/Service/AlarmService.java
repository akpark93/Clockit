package com.example.andrewpark.clockit.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.andrewpark.clockit.SingleActivity.AlarmScreen;

/**
 * Created by andrewpark on 9/8/15.
 */
public class AlarmService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent alarmIntent = new Intent(getBaseContext(), AlarmScreen.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtras(intent);
        getApplication().startActivity(alarmIntent);

        AlarmManagerHelper.setAlarms(this);
        return super.onStartCommand(intent, flags, startId);
    }
}

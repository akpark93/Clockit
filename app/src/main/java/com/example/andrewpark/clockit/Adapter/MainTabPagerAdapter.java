package com.example.andrewpark.clockit.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.andrewpark.clockit.Main_fragments.AlarmFragment;
import com.example.andrewpark.clockit.Main_fragments.ClockFragment;
import com.example.andrewpark.clockit.Main_fragments.StopWatchFragment;
import com.example.andrewpark.clockit.Main_fragments.TimerFragment;

/**
 * Created by andrewpark on 9/9/15.
 */
public class MainTabPagerAdapter extends FragmentPagerAdapter {

    public static AlarmFragment alarmFragment = new AlarmFragment();
    public static ClockFragment clockFragment = new ClockFragment();
    public static TimerFragment timerFragment = new TimerFragment();
    public static StopWatchFragment stopWatchFragment = new StopWatchFragment();

    public MainTabPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return alarmFragment;
            case 1:
                return clockFragment;
            case 2:
                return timerFragment;
            case 3:
                return stopWatchFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}

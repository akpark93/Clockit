package com.example.andrewpark.clockit.model;

import android.net.Uri;

import java.util.Arrays;

/**
 * Created by andrewpark on 9/8/15.
 */
public class AlarmModel {

    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    public long id = -1;
    public int timeHour;
    public int timeMinute;
    private boolean repeatingDays[];
    public boolean repeatWeekly;
    public Uri alarmTone;
    public String name;
    public boolean isEnabled;
    public boolean isVibrate;
    public boolean isFadingIn;
    public int snoozeTime;

    private Melody melody;

    public AlarmModel() {
        repeatingDays = new boolean[7];
        Arrays.fill(repeatingDays,true);
    }

    public void setRepeatingDay(int dayOfWeek, boolean value) {
        repeatingDays[dayOfWeek] = value;
    }

    public boolean getRepeatingDay(int dayOfWeek) {
        return repeatingDays[dayOfWeek];
    }

    public String getTimeText() {
        String hour_string = "";
        String minute_string = "";
        String ampm_string = "";
        if (timeHour >= 12) {
            ampm_string = "PM";
            if (timeHour == 12) {
                hour_string = "" + 12;
            } else {
                hour_string = "" + (timeHour - 12);
            }
        } else {
            ampm_string = "AM";
            if (timeHour == 0) {
                hour_string = ""+12;
            } else {
                hour_string = "" + timeHour;
            }
        }
        if (timeMinute >= 10) {
            minute_string = "" + timeMinute;
        } else {
            minute_string = "0" + timeMinute;
        }
        return hour_string + ":" + minute_string + " " +ampm_string;
    }

    public long getId() {
        return id;
    }

    public void setIsVibrate(boolean isVibrate) {
        this.isVibrate = isVibrate;
    }

    public void setIsFadingIn(boolean isFadingIn) {
        this.isFadingIn = isFadingIn;
    }

}

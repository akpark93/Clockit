package com.example.andrewpark.clockit.model;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import com.example.andrewpark.clockit.SingleActivity.TimerAlarmScreen;

/**
 * Created by andrewpark on 9/12/15.
 */
public class Timer {

    private Context mContext;

    private static final String LOG_TAG = Timer.class.getSimpleName();

    private long remainingTime;

    private int hours;
    private int minutes;
    private int seconds;
    private String timer_text;
    private boolean isPaused;
    private boolean done;

    private TimerCountDownTimer timerCountDownTimer;

    private long totalTime;
    private long elapsedTime;

    public Timer(Context context, int hours, int minutes, int seconds, String timer_text, boolean isPaused) {
        this.mContext = context;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.timer_text = timer_text;
        this.isPaused = isPaused;
    }

    public String getTimer_text() {
        return timer_text;
    }

    public long getHours() {
        return hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public long getTotalMs() {
        return totalTime;
    }

    public void setUpTimer() {
        long hours = getHours();
        long hours_ms = hours * 3600000;

        long minutes = getMinutes();
        long minutes_ms = minutes * 60000;

        long seconds = getSeconds();
        long seconds_ms = seconds * 1000;

        totalTime = hours_ms + minutes_ms + seconds_ms;
    }

    public void startTimer() {
        setUpTimer();
        timerCountDownTimer = new TimerCountDownTimer(totalTime,1000);
        timerCountDownTimer.start();
    }

    public void pause() {
        timerCountDownTimer.cancel();
    }

    public void play() {
        timerCountDownTimer = new TimerCountDownTimer(remainingTime,1000);
        timerCountDownTimer.start();
    }

    public void refresh() {
        timerCountDownTimer.cancel();
        remainingTime = totalTime;
        setText(totalTime);
    }

    public void cancel() {
        timerCountDownTimer.cancel();
    }

    private void setText(long millisUntilFinished) {
        long secondsUntilFinished = millisUntilFinished / 1000;
        String sec_string = "";
        String min_string = "";
        String hr_string = "";
        int sec = (int) (secondsUntilFinished % 60);
        if (sec < 10 && sec >= 0) {
            sec_string = "0" + sec;
        } else { sec_string = "" + sec;}
        int min = (int) (secondsUntilFinished / 60) % 60;
        if (min < 10 && min >= 0) {
            min_string = "0" + min;
        } else { min_string = "" + min;}
        int hr = (int) (secondsUntilFinished / 60 / 60);
        hr_string = "" + hr;
        timer_text = "" + hr_string + ":" + min_string + ":" + sec_string;
    }

    public class TimerCountDownTimer extends CountDownTimer {

        public TimerCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            done = false;
            setText(millisUntilFinished);
            remainingTime = millisUntilFinished;
            elapsedTime = totalTime - millisUntilFinished;
        }

        @Override
        public void onFinish() {
            timer_text = "0:00:00";
            done = true;
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass(mContext,TimerAlarmScreen.class);
            mContext.startActivity(intent);
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public boolean isDone() {
        return done;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }
}

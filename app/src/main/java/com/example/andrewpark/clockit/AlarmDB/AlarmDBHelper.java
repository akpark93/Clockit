package com.example.andrewpark.clockit.AlarmDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.andrewpark.clockit.model.AlarmModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrewpark on 9/8/15.
 */
public class AlarmDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "alarmclock.db";

    private static final String SQL_CREATE_ALARM = "CREATE TABLE " + AlarmContract.Alarm.TABLE_NAME + " (" +
            AlarmContract.Alarm._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME + " TEXT," +
            AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_HOUR + " INTEGER," +
            AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_MINUTE + " INTEGER," +
            AlarmContract.Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS + " TEXT," +
            AlarmContract.Alarm.COLUMN_NAME_ALARM_REPEAT_WEEKLY + " BOOLEAN," +
            AlarmContract.Alarm.COLUMN_NAME_ALARM_TONE + " TEXT," +
            AlarmContract.Alarm.COLUMN_NAME_ALARM_ENABLED + " BOOLEAN," +
            AlarmContract.Alarm.COLUMN_NAME_ALARM_VIBRATE + " BOOLEAN," +
            AlarmContract.Alarm.COLUMN_NAME_ALARM_FADING + " BOOLEAN," +
            AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE + " INTEGER" +
            " )";

    private static final String SQL_DELETE_ALARM =
            "DROP TABLE IF EXISTS " + AlarmContract.Alarm.TABLE_NAME;

    public AlarmDBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ALARM);
        onCreate(sqLiteDatabase);
    }

    private AlarmModel populateModel(Cursor c) {
        AlarmModel model = new AlarmModel();
        model.id = c.getLong(c.getColumnIndex(AlarmContract.Alarm._ID));
        model.name = c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME));
        model.timeHour = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_HOUR));
        model.timeMinute = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_MINUTE));
        model.repeatWeekly = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_REPEAT_WEEKLY)) == 0 ? false : true;
        model.alarmTone = c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_TONE)) != "" ? Uri.parse(c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_TONE))) : null;
        model.isEnabled = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_ENABLED)) == 0 ? false : true;
        model.isVibrate = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_VIBRATE)) == 0 ? false : true;
        model.isFadingIn = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_FADING)) == 0 ? false : true;
        model.snoozeTime = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE));

        String[] repeatingDays = c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS)).split(",");
        for (int i=0; i<repeatingDays.length; ++i) {
            model.setRepeatingDay(i,repeatingDays[i].equals("false") ? false : true);
        }

        return model;
    }

    private ContentValues populateContent(AlarmModel model) {
        ContentValues values = new ContentValues();
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME, model.name);
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_HOUR, model.timeHour);
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_MINUTE, model.timeMinute);
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_REPEAT_WEEKLY, model.repeatWeekly);
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_TONE, model.alarmTone != null ? model.alarmTone.toString() : "");
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_ENABLED, model.isEnabled);
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_VIBRATE, model.isVibrate);
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_FADING, model.isFadingIn);
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE, model.snoozeTime);

        String repeatingDays = "";
        for (int i=0; i<7; ++i) {
            repeatingDays += model.getRepeatingDay(i) + ",";
        }
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS, repeatingDays);

        return values;
    }

    public long createAlarm(AlarmModel model) {
        ContentValues values = populateContent(model);
        return getWritableDatabase().insert(AlarmContract.Alarm.TABLE_NAME, null, values);
    }

    public long updateAlarm(AlarmModel model) {
        ContentValues values = populateContent(model);
        return getWritableDatabase().update(AlarmContract.Alarm.TABLE_NAME, values, AlarmContract.Alarm._ID + " = ?", new String[] { String.valueOf(model.id) });
    }

    public AlarmModel getAlarm(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM " + AlarmContract.Alarm.TABLE_NAME + " WHERE " + AlarmContract.Alarm._ID + " = " + id;
        Cursor c = db.rawQuery(select, null);
        if (c.moveToNext()) {
            return populateModel(c);
        }
        return null;
    }

    public List<AlarmModel> getAlarms() {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + AlarmContract.Alarm.TABLE_NAME;

        Cursor c = db.rawQuery(select, null);

        List<AlarmModel> alarmList = new ArrayList<AlarmModel>();

        while (c.moveToNext()) {
            alarmList.add(populateModel(c));
        }

        if (!alarmList.isEmpty()) {
            return alarmList;
        }

        return null;
    }

    public int deleteAlarm(long id) {
        return getWritableDatabase().delete(AlarmContract.Alarm.TABLE_NAME, AlarmContract.Alarm._ID + " = ?", new String[]{String.valueOf(id)});
    }

}

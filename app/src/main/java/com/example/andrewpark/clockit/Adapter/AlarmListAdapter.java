package com.example.andrewpark.clockit.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewpark.clockit.AlarmDB.AlarmDBHelper;
import com.example.andrewpark.clockit.Service.AlarmManagerHelper;
import com.example.andrewpark.clockit.R;
import com.example.andrewpark.clockit.model.AlarmModel;

import java.util.List;

/**
 * Created by andrewpark on 9/8/15.
 */
public class AlarmListAdapter extends BaseAdapter {

    AlarmDBHelper dbHelper;
    private Context mContext;
    public static List<AlarmModel> mAlarms;

    public AlarmListAdapter(Context context, List<AlarmModel> alarms) {
        mContext = context;
        mAlarms = alarms;
    }

    public void setAlarms(List<AlarmModel> alarms) {
        mAlarms = alarms;
    }

    @Override
    public int getCount() {
        if (mAlarms != null) {
            return mAlarms.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mAlarms != null) {
            return mAlarms.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (mAlarms != null) {
            return mAlarms.get(position).id;
        }
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        dbHelper = new AlarmDBHelper(mContext);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alarm_list_item, parent, false);
        }

        final AlarmModel model = (AlarmModel) getItem(position);

        Typeface kg = Typeface.createFromAsset(mContext.getAssets(),"kg.ttf");
        TextView txtTime = (TextView) view.findViewById(R.id.alarm_item_time);
        TextView txtTimeampm = (TextView) view.findViewById(R.id.alarm_item_time_ampm);
        txtTime.setTypeface(kg);
        txtTimeampm.setTypeface(kg);
        setTimeText(txtTime, txtTimeampm, model.timeHour, model.timeMinute);

        TextView txtName = (TextView) view.findViewById(R.id.alarm_item_name);
        txtName.setTypeface(kg);
        if (model.name.isEmpty()) {
            txtName.setVisibility(View.GONE);
        } else {
            txtName.setText(model.name);
        }

        updateTextColor((TextView) view.findViewById(R.id.alarm_item_sunday), model.getRepeatingDay(AlarmModel.SUNDAY));
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_monday), model.getRepeatingDay(AlarmModel.MONDAY));
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_tuesday), model.getRepeatingDay(AlarmModel.TUESDAY));
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_wednesday), model.getRepeatingDay(AlarmModel.WEDNESDAY));
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_thursday), model.getRepeatingDay(AlarmModel.THURSDAY));
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_friday), model.getRepeatingDay(AlarmModel.FRIDAY));
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_saturday), model.getRepeatingDay(AlarmModel.SATURDAY));

        Switch btnToggle = (Switch)view.findViewById(R.id.alarm_item_toggle);
        btnToggle.setChecked(model.isEnabled);
        btnToggle.setTag(Long.valueOf(model.id));
        btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setAlarmEnabled(((Long) buttonView.getTag()).longValue(), isChecked);
                if (isChecked) {
                    Toast.makeText(mContext, "Alarm is set for: " + model.getTimeText(), Toast.LENGTH_LONG).show();
                }
            }
        });

        view.setTag(Long.valueOf(model.id));
        return view;
    }

    private void setTimeText(TextView time, TextView ampm, int hour, int minute) {
        String hour_string = "";
        String minute_string = "";
        String ampm_string = "";
        if (hour >= 12) {
            ampm_string = "PM";
            if (hour == 12) {
                hour_string = "" + 12;
            } else {
                hour_string = "" + (hour - 12);
            }
        } else {
            ampm_string = "AM";
            if (hour == 0) {
                hour_string = ""+12;
            } else {
                hour_string = "" + hour;
            }
        }
        if (minute >= 10) {
            minute_string = "" + minute;
        } else {
            minute_string = "0" + minute;
        }
        time.setText(hour_string + ":" + minute_string + " ");
        ampm.setText(ampm_string);
    }

    private void updateTextColor(TextView view, boolean isOn) {
        if (isOn) {
            view.setTextColor(Color.BLUE);
        } else {
            view.setTextColor(Color.BLACK);
        }
    }

    public void setAlarmEnabled(long id, boolean isEnabled) {
        AlarmManagerHelper.cancelAlarms(mContext);

        AlarmModel model = dbHelper.getAlarm(id);
        model.isEnabled = isEnabled;
        dbHelper.updateAlarm(model);

        AlarmManagerHelper.setAlarms(mContext);
    }
}

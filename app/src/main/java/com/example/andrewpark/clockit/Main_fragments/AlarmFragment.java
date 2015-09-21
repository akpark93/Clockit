package com.example.andrewpark.clockit.Main_fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.andrewpark.clockit.Adapter.AlarmListAdapter;
import com.example.andrewpark.clockit.AlarmDB.AlarmDBHelper;
import com.example.andrewpark.clockit.R;
import com.example.andrewpark.clockit.Service.AlarmManagerHelper;
import com.example.andrewpark.clockit.SingleActivity.AlarmDetailsActivity;
import com.example.andrewpark.clockit.model.AlarmModel;

import java.util.List;


public class AlarmFragment extends android.support.v4.app.Fragment {

    AlarmListAdapter mAdapter;
    private Context mContext;
    AlarmDBHelper dbHelper;

    private ListView alarmListView;
    private Button add_alarm_btn;
    private Button record_btn;
    private List<AlarmModel> alarms = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.alarm_fragment, container, false);

        dbHelper = new AlarmDBHelper(mContext);
        alarms = dbHelper.getAlarms();

        mAdapter = new AlarmListAdapter(mContext, alarms);

        alarmListView = (ListView)view.findViewById(R.id.alarm_listView);
        alarmListView.setAdapter(mAdapter);
        alarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startAlarmDetailsActivity(((Long) view.getTag()).longValue());
            }
        });

        alarmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteAlarm(((Long) view.getTag()).longValue());
                return true;
            }
        });

        add_alarm_btn = (Button)view.findViewById(R.id.add_icon);
        add_alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAlarmDetailsActivity(-1);
            }
        });

        return view;
    }

    public void setAlarmEnabled(long id, boolean isEnabled) {
        AlarmManagerHelper.cancelAlarms(mContext);

        AlarmModel model = dbHelper.getAlarm(id);
        model.isEnabled = isEnabled;
        dbHelper.updateAlarm(model);

        AlarmManagerHelper.setAlarms(mContext);
    }

    public void startAlarmDetailsActivity(long id) {
        Intent intent = new Intent(getActivity(), AlarmDetailsActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 0);
    }

    public void deleteAlarm(long id) {
        final long alarmId = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Please confirm")
                .setTitle("Delete set?")
                .setCancelable(true)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel Alarms
                        AlarmManagerHelper.cancelAlarms(mContext);
                        //Delete alarm from DB by id
                        dbHelper.deleteAlarm(alarmId);
                        //Refresh the list of the alarms in the adaptor
                        mAdapter.setAlarms(dbHelper.getAlarms());
                        //Notify the adapter the data has changed
                        mAdapter.notifyDataSetChanged();
                        //Set the alarms
                        AlarmManagerHelper.setAlarms(mContext);
                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0652) {
            mAdapter.setAlarms(dbHelper.getAlarms());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (alarms!=null) {
            mAdapter.notifyDataSetChanged();
        }
    }
}

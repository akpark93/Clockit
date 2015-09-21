package com.example.andrewpark.clockit.SingleActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.andrewpark.clockit.AlarmDB.AlarmDBHelper;
import com.example.andrewpark.clockit.ChooseSound;
import com.example.andrewpark.clockit.R;
import com.example.andrewpark.clockit.Service.AlarmManagerHelper;
import com.example.andrewpark.clockit.model.AlarmModel;
import com.example.andrewpark.clockit.model.Melody;


public class AlarmDetailsActivity extends Activity {

    private static final String LOG_TAG = AlarmDetailsActivity.class.getSimpleName();

    private AlarmModel alarmDetails;
    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
    private Context mContext;

    private TimePicker timePicker;
    private EditText edtName;
    private CheckBox chkWeekly;

    private ToggleButton chkSunday;
    private ToggleButton chkMonday;
    private ToggleButton chkTuesday;
    private ToggleButton chkWednesday;
    private ToggleButton chkThursday;
    private ToggleButton chkFriday;
    private ToggleButton chkSaturday;

    private TextView txtToneSelection;
    private Button saveButton;
    private Button cancelButton;

    private CheckBox chkVibrate;
    private CheckBox chkFade;

    public static int id;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_details);

        getActionBar().setTitle("Create New Alarm");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = getBaseContext();
        getInit();
        setUpSaveandCancelBtn();

        long id = getIntent().getExtras().getLong("id");

        if (id == -1) {

            alarmDetails = new AlarmModel();
            setUpNew();

        } else {
            alarmDetails = dbHelper.getAlarm(id);

            timePicker.setCurrentMinute(alarmDetails.timeMinute);
            timePicker.setCurrentHour(alarmDetails.timeHour);

            edtName.setText(alarmDetails.name);
            chkWeekly.setChecked(alarmDetails.repeatWeekly);

            setUpButtonColors();

            txtToneSelection.setText(RingtoneManager.getRingtone(this, alarmDetails.alarmTone).getTitle(this));
        }

        final LinearLayout ringToneContainer = (LinearLayout) findViewById(R.id.alarm_ringtone_container);
        ringToneContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmDetailsActivity.this, ChooseSound.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v(LOG_TAG, "onactivity result code: " + resultCode);
        Log.v(LOG_TAG, "onactivity request code: " + requestCode);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: {
                    String melody_name = data.getStringExtra("melody_name");
                    String melody_uri = data.getStringExtra("melody_uri");
                    String melody_path = data.getStringExtra("melody_path");
                    Log.v(LOG_TAG, "entered and info: " + melody_name + melody_uri + melody_path);
                    if (melody_name != null) {
                        txtToneSelection.setText(melody_name);
                    }
                    if (melody_uri != null) {
                        alarmDetails.alarmTone = Uri.parse(melody_uri);
                    }
                    if (melody_path != null) {
                        alarmDetails.alarmTone = Uri.parse(melody_path);
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private void setUpNew() {
        chkSunday.setChecked(true);
        chkMonday.setChecked(true);
        chkTuesday.setChecked(true);
        chkWednesday.setChecked(true);
        chkThursday.setChecked(true);
        chkFriday.setChecked(true);
        chkSaturday.setChecked(true);
        chkVibrate.setChecked(false);
        chkFade.setChecked(false);
    }

    private void getInit() {
        timePicker = (TimePicker) findViewById(R.id.alarm_details_time_picker);
        edtName = (EditText) findViewById(R.id.alarm_details_name);
        chkWeekly = (CheckBox) findViewById(R.id.alarm_details_repeat_weekly);
        chkSunday = (ToggleButton) findViewById(R.id.alarm_details_repeat_sunday);
        chkMonday = (ToggleButton) findViewById(R.id.alarm_details_repeat_monday);
        chkTuesday = (ToggleButton) findViewById(R.id.alarm_details_repeat_tuesday);
        chkWednesday = (ToggleButton) findViewById(R.id.alarm_details_repeat_wednesday);
        chkThursday = (ToggleButton) findViewById(R.id.alarm_details_repeat_thursday);
        chkFriday = (ToggleButton) findViewById(R.id.alarm_details_repeat_friday);
        chkSaturday = (ToggleButton) findViewById(R.id.alarm_details_repeat_saturday);
        txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);
        chkVibrate = (CheckBox) findViewById(R.id.alarm_details_vibrate_chk);
        chkFade = (CheckBox) findViewById(R.id.alarm_details_fade_in_chk);
    }

    private void setUpButtonColors() {
        chkSunday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.SUNDAY));
        chkMonday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.MONDAY));
        chkTuesday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.TUESDAY));
        chkWednesday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.WEDNESDAY));
        chkThursday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.THURSDAY));
        chkFriday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.FRIDAY));
        chkSaturday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.SATURDAY));
    }

    private void setUpSaveandCancelBtn() {
        saveButton = (Button) findViewById(R.id.save_alarm_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateModelFromLayout();
                AlarmManagerHelper.cancelAlarms(mContext);

                if (alarmDetails.id < 0) {
                    dbHelper.createAlarm(alarmDetails);
                } else {
                    dbHelper.updateAlarm(alarmDetails);
                }

                AlarmManagerHelper.setAlarms(mContext);

                setResult(0652);
                Toast.makeText(mContext, "Alarm is set for: " + alarmDetails.getTimeText(), Toast.LENGTH_LONG).show();
                finish();
            }
        });

        cancelButton = (Button) findViewById(R.id.cancel_alarm_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_details, menu);
        return true;
    }

    private void updateModelFromLayout() {
        alarmDetails.timeMinute = timePicker.getCurrentMinute().intValue();
        alarmDetails.timeHour = timePicker.getCurrentHour().intValue();
        alarmDetails.name = edtName.getText().toString();
        alarmDetails.repeatWeekly = chkWeekly.isChecked();
        alarmDetails.setRepeatingDay(AlarmModel.SUNDAY, chkSunday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.MONDAY, chkMonday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.TUESDAY, chkTuesday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.WEDNESDAY, chkWednesday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.THURSDAY, chkThursday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.FRIDAY, chkFriday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.SATURDAY, chkSaturday.isChecked());
        alarmDetails.isEnabled = true;
        alarmDetails.setIsFadingIn(chkFade.isChecked());
        alarmDetails.setIsVibrate(chkVibrate.isChecked());
    }

    public void setMelody(Melody melody) {
        alarmDetails.alarmTone = Uri.parse(melody.getMelody_uri());
    }
}

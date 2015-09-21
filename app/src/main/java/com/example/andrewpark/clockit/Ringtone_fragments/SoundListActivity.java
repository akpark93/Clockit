package com.example.andrewpark.clockit.Ringtone_fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.andrewpark.clockit.Adapter.MelodyListAdapter;
import com.example.andrewpark.clockit.ChooseSound;
import com.example.andrewpark.clockit.R;
import com.example.andrewpark.clockit.model.Melody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrewpark on 9/10/15.
 */
public class SoundListActivity extends Activity {

    private static final String LOG_TAG = SoundListActivity.class.getSimpleName();

    private Context mContext;

    private List<Melody> melodyList;
    public MelodyListAdapter mMelodyAdapter = null;
    private ListView melody_listView;
    private Ringtone ringtone;

    private Melody melody;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_list);
        mContext = getApplicationContext();
        melodyList = new ArrayList<Melody>();

        RingtoneManager alarm_ringtoneManager = new RingtoneManager(mContext);
        alarm_ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarm_ringtone_cursor = alarm_ringtoneManager.getCursor();

        RingtoneManager ringtoneManager = new RingtoneManager(mContext);
        ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor ringtone_cursor = ringtoneManager.getCursor();

        while (alarm_ringtone_cursor.moveToNext()) {
            int currentPosition = alarm_ringtone_cursor.getPosition();
            String ringtone_title = alarm_ringtoneManager.getRingtone(currentPosition).getTitle(mContext);
            Uri ringtone_uri = alarm_ringtoneManager.getRingtoneUri(currentPosition);
            Melody melody = new Melody(ringtone_title, ringtone_uri.toString());
            melodyList.add(melody);
        }

        while (ringtone_cursor.moveToNext()) {
            int currentPosition = ringtone_cursor.getPosition();
            String ringtone_title = ringtoneManager.getRingtone(currentPosition).getTitle(mContext);
            Uri ringtone_uri = ringtoneManager.getRingtoneUri(currentPosition);
            Melody melody = new Melody(ringtone_title, ringtone_uri.toString());
            melodyList.add(melody);
        }

        mMelodyAdapter = new MelodyListAdapter(mContext,R.layout.melody_list_item,R.id.melody_listview,melodyList);
        melody_listView = (ListView)findViewById(R.id.melody_listview);
        melody_listView.setAdapter(mMelodyAdapter);
        melody_listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        final int[] selectedPosition = {0};

        melody_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (ringtone != null) {
                    ringtone.stop();
                }

                melody = mMelodyAdapter.getItem(position);
                Uri currentMelody_uri = Uri.parse(melody.getMelody_uri());

                ringtone = RingtoneManager.getRingtone(mContext,currentMelody_uri);
                ringtone.play();
            }
        });
    }

    @Override
    public void onPause() {
        if (ringtone != null) {
            ringtone.stop();
        }
        if (mMelodyAdapter.melodyIsSelected()) {
            ChooseSound.isSelected = true;
        } else {
            ChooseSound.isSelected = false;
        }
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        if (ChooseSound.isSelected) {
            mMelodyAdapter.setRBOff();
            mMelodyAdapter.notifyDataSetChanged();
        }
        super.onPostResume();
    }

    String chosen_melody_name;
    String chosen_melody_uri;
    String chosen_melody_path;
    boolean last = false;

    @Override
    public void finish() {
        setChosenMelody(mMelodyAdapter.getChosen_melody());
        Intent intent = getIntent();
        intent.putExtra("melody_name", chosen_melody_name);
        intent.putExtra("melody_uri", chosen_melody_uri);
        intent.putExtra("melody_path", chosen_melody_path);
        super.finish();
    }

    public void setChosenMelody(Melody melody) {
        if (melody != null) {
            chosen_melody_name = melody.getMelody_name();
            chosen_melody_uri = melody.getMelody_uri();
        }
    }
}

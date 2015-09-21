package com.example.andrewpark.clockit;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.example.andrewpark.clockit.Ringtone_fragments.MusicListActivity;
import com.example.andrewpark.clockit.Ringtone_fragments.SoundListActivity;
import com.example.andrewpark.clockit.Ringtone_fragments.SoundRecordActivity;


public class ChooseSound extends TabActivity {

    private static final String LOG_TAG = ChooseSound.class.getSimpleName();

    TabHost tabHost;
    Bundle b;
    Intent intentMusic;
    Intent intentSound;

    public static boolean isSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sound);

        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        tabHost = getTabHost();

        // Android tab
        intentMusic = new Intent().setClass(this, MusicListActivity.class);
        TabHost.TabSpec tabSpecMusic = tabHost.newTabSpec("Music").setContent(intentMusic).setIndicator("Music");

        // Apple tab
        intentSound = new Intent().setClass(this, SoundListActivity.class);
        TabHost.TabSpec tabSpecSound = tabHost.newTabSpec("Ringtone").setContent(intentSound).setIndicator("Ringtone");

        // Windows tab
        Intent intentRecord = new Intent().setClass(this, SoundRecordActivity.class);
        TabHost.TabSpec tabSpecRecord = tabHost.newTabSpec("New").setContent(intentRecord).setIndicator("New");

        // add all tabs
        tabHost.addTab(tabSpecSound);
        tabHost.addTab(tabSpecMusic);
        tabHost.addTab(tabSpecRecord);

        //set Windows tab as default (zero based)
        tabHost.setCurrentTab(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_sound, menu);
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

    String chosen_melody_name_sound;
    String chosen_melody_uri_sound;

    String chosen_melody_name_music;
    String chosen_melody_uri_music;

    @Override
    public void finish() {
        //find a way if the melody is from sound or music

        chosen_melody_name_sound = intentSound.getStringExtra("melody_name");
        chosen_melody_uri_sound = intentSound.getStringExtra("melody_uri");

        chosen_melody_name_music = intentMusic.getStringExtra("melody_name");
        chosen_melody_uri_music = intentMusic.getStringExtra("melody_uri");

        Intent intent = new Intent();
        if (chosen_melody_name_sound!=null) {
            intent.putExtra("melody_name", chosen_melody_name_sound);
            intent.putExtra("melody_uri", chosen_melody_uri_sound);
        } else {
            intent.putExtra("melody_name", chosen_melody_name_music);
            intent.putExtra("melody_uri", chosen_melody_uri_music);
        }

        setResult(RESULT_OK,intent);
        super.finish();
    }
}

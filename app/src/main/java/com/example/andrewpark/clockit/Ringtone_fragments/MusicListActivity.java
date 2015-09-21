package com.example.andrewpark.clockit.Ringtone_fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.andrewpark.clockit.Adapter.MelodyListAdapter;
import com.example.andrewpark.clockit.ChooseSound;
import com.example.andrewpark.clockit.R;
import com.example.andrewpark.clockit.model.Melody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrewpark on 9/10/15.
 */
public class MusicListActivity extends Activity {

    private static final String LOG_TAG = MusicListActivity.class.getSimpleName();

    private Context mContext;
    private List<Melody> musicList;
    private String[] mAudioPath;
    private MediaPlayer mediaPlayer;
    public MelodyListAdapter mMusicAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        mContext = getApplicationContext();
        musicList = new ArrayList<Melody>();
        getMusic(musicList);

        mMusicAdapter = new MelodyListAdapter(mContext,R.layout.melody_list_item,R.id.music_listview,musicList);
        mediaPlayer = new MediaPlayer();
        ListView music_listview = (ListView)findViewById(R.id.music_listview);
        music_listview.setAdapter(mMusicAdapter);
        music_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    playSong(mAudioPath[i]);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        music_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
    }

    private void getMusic(List<Melody> musicList) {
        final Cursor mCursor = mContext.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA}, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        int count = mCursor.getCount();
        mAudioPath = new String[count];
        int i=0;
        while(mCursor.moveToNext()) {
            String music_title = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            mAudioPath[i] = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            Melody melody = new Melody(music_title, mAudioPath[i]);
            musicList.add(melody);
            i++;
        }
        mCursor.close();
    }

    private void playSong(String path) throws IllegalArgumentException,
            IllegalStateException, IOException {
        mediaPlayer.reset();
        mediaPlayer.setDataSource(path);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    @Override
    public void onPause() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (mMusicAdapter.melodyIsSelected()) {
            ChooseSound.isSelected=true;
        } else {
            ChooseSound.isSelected=false;
        }
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        if (ChooseSound.isSelected) {
            mMusicAdapter.setRBOff();
            mMusicAdapter.notifyDataSetChanged();
        }
        mMusicAdapter.notifyDataSetChanged();
        super.onPostResume();
    }

    String chosen_melody_name;
    String chosen_melody_uri;

    @Override
    public void finish() {
        setChosenMelody(mMusicAdapter.getChosen_melody());
        Intent intent = getIntent();
        intent.putExtra("melody_name", chosen_melody_name);
        intent.putExtra("melody_uri", chosen_melody_uri);
        Log.v(LOG_TAG, chosen_melody_name + chosen_melody_uri );
        super.finish();
    }

    public void setChosenMelody(Melody melody) {
        if (melody != null) {
            chosen_melody_name = melody.getMelody_name();
            chosen_melody_uri = melody.getMelody_uri();
        }
    }
}

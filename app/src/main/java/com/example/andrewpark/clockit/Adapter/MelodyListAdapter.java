package com.example.andrewpark.clockit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.andrewpark.clockit.R;
import com.example.andrewpark.clockit.model.Melody;

import java.util.List;

/**
 * Created by andrewpark on 9/10/15.
 */
public class MelodyListAdapter extends ArrayAdapter<Melody> {

    final String LOG_TAG = MelodyListAdapter.class.getSimpleName();
    int selectedPosition = 0;
    RadioButton selectedRB;
    boolean selected = false;
    private String melody_name;
    List<Melody> melodylist;

    Melody chosen_melody = null;

    public MelodyListAdapter(Context context, int resource, int textViewResourceId, List<Melody> list) {
        super(context, resource, textViewResourceId, list);
        this.melodylist = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v==null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.melody_list_item, null);
        }

        final Melody melody = getItem(position);
        melody_name = melody.getMelody_name();

        TextView melody_title_textview = (TextView)v.findViewById(R.id.name_melody_listview);
        melody_title_textview.setText(melody_name);

        final RadioButton radioButton = (RadioButton)v.findViewById(R.id.radiobutton_melody_listview);
        if (melody.isSelected()) {
            radioButton.setChecked(true);
        } else {
            radioButton.setChecked(false);
        }
        radioButton.setTag(position);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Melody melody : melodylist) {
                    melody.setSelected(false);
                }
                melody.setSelected(true);
                notifyDataSetChanged();
                selected = true;
                //set new melody
                chosen_melody = melody;
            }
        });
        return v;
    }

    public Melody getChosen_melody() {
        return chosen_melody;
    }

    public boolean melodyIsSelected() {
        if (chosen_melody == null) {
            return false;
        } else {
            return chosen_melody.isSelected();
        }
    }

    public void setRBOff() {
        for (Melody melody : melodylist) {
            melody.setSelected(false);
        }
    }
}
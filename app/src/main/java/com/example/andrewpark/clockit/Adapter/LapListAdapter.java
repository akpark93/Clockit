package com.example.andrewpark.clockit.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andrewpark.clockit.R;

import java.util.List;

/**
 * Created by andrewpark on 9/11/15.
 */
public class LapListAdapter extends ArrayAdapter<String> {


    public LapListAdapter(Context context, int resource, int textViewResourceId, List<String> list) {
        super(context, resource, textViewResourceId, list);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lap_list_item, parent, false);
        }

        Typeface kgp = Typeface.createFromAsset(getContext().getAssets(),"kgp.ttf");
        String text = getItem(position);
        String stopwatch_text = text.substring(0,7);
        String stopwatch_text_ms = text.substring(7,10);

        TextView index = (TextView)view.findViewById(R.id.lap_index);
        index.setText("" + (position+1) + ".");

        TextView lap_time_text = (TextView)view.findViewById(R.id.lap_time_text);
        lap_time_text.setTypeface(kgp);
        lap_time_text.setText(stopwatch_text);

        TextView lap_time_text_ms = (TextView)view.findViewById(R.id.lap_time_text_ms);
        lap_time_text_ms.setTypeface(kgp);
        lap_time_text_ms.setText(stopwatch_text_ms);

        return view;
    }
}

package com.example.andrewpark.clockit.Main_fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.andrewpark.clockit.Adapter.TimerListAdapter;
import com.example.andrewpark.clockit.R;
import com.example.andrewpark.clockit.SingleActivity.TimerScreen;
import com.example.andrewpark.clockit.model.Timer;

import java.util.ArrayList;

/**
 * Created by andrewpark on 9/9/15.
 */
public class TimerFragment extends Fragment {

    private static final String LOG_TAG = TimerFragment.class.getSimpleName();

    private Context mContext;
    private final int REFRESH_RATE = 500;
    private Handler myHandler;

    private TextView timer_text;
    private String temp_input_numbers_str = "";
    private String input_numbers_str = "";

    private Button timer_play_btn;
    private Button timer_text_delete_btn;
    private int counter = 0;

    public static ArrayList<String> timers_string = null;
    public static ArrayList<Timer> timers = null;
    private TimerListAdapter mTimerAdapter;
    private ListView timer_listview;

    Button[] grid_buttons;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_fragment, container, false);

        mContext = getContext();
        myHandler = new Handler();
        if (timers == null) {
            timers = new ArrayList<Timer>();
        }
        getInit(view);
        setUpGrid(view);
        setUpListView(view);

        return view;
    }

    private void setUpListView(View view) {
        if (timers_string==null)
            timers_string = new ArrayList<String>();
        mTimerAdapter = new TimerListAdapter(mContext,R.layout.timer_list_item,R.id.timer_listview,timers_string);
        timer_listview = (ListView)view.findViewById(R.id.timer_listview);
        timer_listview.setAdapter(mTimerAdapter);
        timer_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //alertdialogue
                AlertDialog.Builder delete_timer_dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Delete Timer?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                timers_string.remove(position);
                                timers.remove(position);
                                mTimerAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                delete_timer_dialog.create().show();
                return true;
            }
        });
        timer_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,long l) {
                Intent intent = new Intent(mContext, TimerScreen.class).putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    private void setUpGrid(View view) {
        grid_buttons = new Button[] {
                (Button)view.findViewById(R.id.timer_button_1), (Button)view.findViewById(R.id.timer_button_2),
                (Button)view.findViewById(R.id.timer_button_3), (Button)view.findViewById(R.id.timer_button_4),
                (Button)view.findViewById(R.id.timer_button_5), (Button)view.findViewById(R.id.timer_button_6),
                (Button)view.findViewById(R.id.timer_button_7), (Button)view.findViewById(R.id.timer_button_8),
                (Button)view.findViewById(R.id.timer_button_9), (Button)view.findViewById(R.id.timer_button_0)
        };
        setUpGridButtons(grid_buttons);
    }

    private void setUpGridButtons(Button[] buttons) {
        for (final Button button : buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = Integer.parseInt(button.getText().toString());
                    if (input_numbers_str.length() < 6)
                        setUpTimerTime(position);
                }
            });
        }
    }

    //get initial variables
    private void getInit(View view) {
        Typeface kgp = Typeface.createFromAsset(getContext().getAssets(),"kgp.ttf");
        timer_text = (TextView)view.findViewById(R.id.timer_text);
        timer_text.setTypeface(kgp);

        timer_play_btn = (Button)view.findViewById(R.id.timer_play_btn);
        timer_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timer_text == null) {
                    return;
                }
                addTimer();
                newText();
            }
        });

        timer_text_delete_btn = (Button) view.findViewById(R.id.backspace_btn);
        timer_text_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteText();
            }
        });

    }

    private void newText() {
        input_numbers_str = "";
        counter = 0;
        setText();
    }

    private void deleteText() {
        if (input_numbers_str.isEmpty()) {
            return;
        } else {
            int currentlength = input_numbers_str.length();
            input_numbers_str = input_numbers_str.substring(0, currentlength - 1);
            counter--;
            temp_input_numbers_str = input_numbers_str;
            setText();
        }
    }

    //set up the timer
    private void setUpTimerTime(int position) {
        //add another number
        counter++;
        //create new string
        input_numbers_str += position;
        //temp string to create timer textview
        temp_input_numbers_str = input_numbers_str;
        setText();
    }

    private void setText() {
        for (int i=counter; i<5; i++) {
            temp_input_numbers_str = "0" + temp_input_numbers_str;
        }
        timer_text.setText(temp_input_numbers_str.substring(0, 1) + ":" + temp_input_numbers_str.substring(1, 3) + ":" + temp_input_numbers_str.substring(3, 5));
        temp_input_numbers_str = "";
    }

    private void addTimer() {
        //get hour,min,sec
        temp_input_numbers_str = input_numbers_str;
        for (int i=counter; i<5; i++) {
            temp_input_numbers_str = "0" + temp_input_numbers_str;
        }
        String final_string_timer = timer_text.getText().toString();

        int hours = Integer.parseInt(temp_input_numbers_str.substring(0,1));
        int mins = Integer.parseInt(temp_input_numbers_str.substring(1,3));
        int secs = Integer.parseInt(temp_input_numbers_str.substring(3,5));

        Timer timer = new Timer(mContext,hours,mins,secs,final_string_timer,false);
        timers.add(timer);
        timers_string.add(timer.getTimer_text());
        updateAdapter();
        timer.startTimer();
        myHandler.postDelayed(startTimer,0);
    }

    private void updateAdapter() {
        mTimerAdapter.notifyDataSetChanged();
    }

    private Runnable startTimer = new Runnable() {
        @Override
        public void run() {
            if (timers==null) {
                myHandler.removeCallbacks(startTimer);
                return;
            }
            boolean done = false;
            if (timers != null) {
                for (int i = 0; i < timers.size(); i++) {
                    if (timers.get(i).isDone()) {
                        done = true;
                        timers.remove(i);
                        timers_string.remove(i);
                        mTimerAdapter.notifyDataSetChanged();
                    } else {
                        done = false;
                        break;
                    }
                }
            }
            if (done) {
                myHandler.removeCallbacks(startTimer);
                return;
            }

            for (int position_in_list = 0; position_in_list < timers.size(); position_in_list++) {
                timers_string.set(position_in_list, timers.get(position_in_list).getTimer_text());
            }
            mTimerAdapter.notifyDataSetChanged();
            myHandler.postDelayed(this,REFRESH_RATE);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        myHandler.removeCallbacks(startTimer);
    }

    @Override
    public void onStart() {
        super.onStart();
        myHandler.postDelayed(startTimer,0);
    }
}


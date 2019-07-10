package com.chigbrowsoftware.kgo.fragments;

import static com.chigbrowsoftware.kgo.controller.MainActivity.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import com.chigbrowsoftware.kgo.R;
import com.chigbrowsoftware.kgo.controller.MainActivity;
import com.chigbrowsoftware.kgo.model.Activity;
import java.util.Timer;
import java.util.TimerTask;

//import com.chigbrowsoftware.kgo.PreferenceUtil;

public class TaskFragment extends Fragment {

  Button button1;
  public static int done = 0;

  private static TextView clockDisplay;
  private Timer activityTimer;
  private Timer clockTimer;
  private String activityTimeElapsedKey;
  private String clockFormat;
  private long activityTimerStart;
  private long activityTimeElapsed;

  private int timeLimit;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_task, container, false);

    TextView tv = view.findViewById(R.id.frag_1);
    tv.setText(getArguments().getString("msg"));


    clockDisplay = view.findViewById(R.id.clock_display);
    activityTimeElapsedKey = getString(R.string.activity_time_elapsed_key);
    clockFormat = getString(R.string.clock_format);
    activityTimeElapsed = 0;
    activityTimerStart = System.currentTimeMillis();
    startActivityTimer();
    initActivity();
    updateClock();

    button1 = view.findViewById(R.id.button_1);
    button1.setOnClickListener(view1 -> {
      pager.setCurrentItem(getNextPossibleItemIndex(1), true);
      done += 1;
    });

    return view;
  }

  public static TaskFragment newInstance(String text) {

    TaskFragment frag1 = new TaskFragment();
    Bundle bundle = new Bundle();
    bundle.putString("msg", text);

    frag1.setArguments(bundle);

    return frag1;
  }

  private int getNextPossibleItemIndex(int change) {

    int currentIndex = pager.getCurrentItem();
    int total = MainActivity.NUM_PAGES;

    if (currentIndex + change < 0) {
      return 0;
    }
    return Math.abs((currentIndex + change) % total);
  }

  private class ClockTimerTask extends TimerTask {

    @Override
    public void run() {
      if (getActivity() != null) {
        getActivity().runOnUiThread(() -> updateClock());
      }
    }
  }

    public void updateClock() {
      timeLimit = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("timer", 15);
      long remaining = (timeLimit * 60000) - (System.currentTimeMillis() - activityTimerStart);
      int minutes;
      double seconds;

      if (remaining > 0) {
        minutes = (int) (remaining / 60000);
        seconds = (remaining % 60000) / 1000.0;
      } else {
        minutes = timeLimit;
        seconds = 0;
      }
      clockDisplay.setText(String.format(clockFormat, minutes, seconds));
    }

    //TODO create a timeout task and tie to activity timer
    private void startActivityTimer() {
      activityTimer = new Timer();
      activityTimerStart = System.currentTimeMillis();
      clockTimer = new Timer();
      clockTimer.schedule(new ClockTimerTask(), 0, 100);
      updateClock();
    }

  private void initActivity() {
    activityTimeElapsed = 0;
    activityTimerStart = System.currentTimeMillis();
    startActivityTimer();
    updateClock();
  }


  }


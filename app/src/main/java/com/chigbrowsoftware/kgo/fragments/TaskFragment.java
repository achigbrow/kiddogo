package com.chigbrowsoftware.kgo.fragments;

import static com.chigbrowsoftware.kgo.controller.MainActivity.pager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import com.chigbrowsoftware.kgo.R;
import com.chigbrowsoftware.kgo.controller.MainActivity;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

//import com.chigbrowsoftware.kgo.PreferenceUtil;

public class TaskFragment extends Fragment {

  public static int done = 0;
  private static TextView clockDisplay;
  Button button1;
  private Timer activityTimer;
  private Timer clockTimer;
  private String activityTimeElapsedKey;
  private String clockFormat;
  private long activityTimerStart;
  private long activityTimeElapsed;

  private Context context;

  private int timeLimit;

  public static TaskFragment newInstance(String text) {

    TaskFragment frag1 = new TaskFragment();
    Bundle bundle = new Bundle();
    bundle.putString("msg", text);

    frag1.setArguments(bundle);

    return frag1;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = getContext();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_task, container, false);

    TextView tv = view.findViewById(R.id.frag_1);
    tv.setText(getArguments().getString("msg"));

    clockDisplay = view.findViewById(R.id.clock_display);
    activityTimeElapsedKey = getString(R.string.activity_time_elapsed_key);
    clockFormat = getString(R.string.clock_format);

    button1 = view.findViewById(R.id.button_1);
    button1.setOnClickListener(view1 -> {
      pager.setCurrentItem(getNextPossibleItemIndex(1), true);
      done += 1;
    });

    return view;
  }

  private int getNextPossibleItemIndex(int change) {

    int currentIndex = pager.getCurrentItem();
    int total = MainActivity.NUM_PAGES;

    if (currentIndex + change < 0) {
      return 0;
    }
    return Math.abs((currentIndex + change) % total);
  }

  @Override
  public void onStart() {
    super.onStart();
    startActivityTimer();
  }

  public void updateClock() {
    timeLimit = PreferenceManager.getDefaultSharedPreferences(context).getInt("timer", 15);
    long remaining = (timeLimit * 60000) - (System.currentTimeMillis() - activityTimerStart);
    int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(remaining);
    double seconds = TimeUnit.MILLISECONDS.toSeconds(remaining % 60000);
    clockDisplay.setText(String.format(clockFormat, minutes, seconds));
  }

  //TODO create a timeout task and tie to activity timer
  private void startActivityTimer() {
    activityTimer = new Timer();
    activityTimerStart = System.currentTimeMillis();
    activityTimeElapsed = 0;
    clockTimer = new Timer();
    clockTimer.schedule(new ClockTimerTask(), 0, 100);
    updateClock();
  }

  private class ClockTimerTask extends TimerTask {

    @Override
    public void run() {
      if (isVisible()) {
        getActivity().runOnUiThread(() -> updateClock());
      }
    }
  }

}


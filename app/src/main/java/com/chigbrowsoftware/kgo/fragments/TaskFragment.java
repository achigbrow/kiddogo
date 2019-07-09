package com.chigbrowsoftware.kgo.fragments;
import static com.chigbrowsoftware.kgo.controller.MainActivity.pager;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import com.chigbrowsoftware.kgo.PreferenceUtil;
import com.chigbrowsoftware.kgo.R;
import com.chigbrowsoftware.kgo.controller.MainActivity;
import com.chigbrowsoftware.kgo.model.entity.UserEntity;
import com.chigbrowsoftware.kgo.model.viewmodel.UserViewModel;
import java.util.Timer;
import java.util.TimerTask;

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

  private MainActivity main;
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
    main.updateClock();

    button1 = view.findViewById(R.id.button_1);
    button1.setOnClickListener(view1 -> {
      pager.setCurrentItem(getNextPossibleItemIndex(1), true);
      done += 1;
    });

//    button1 = view.findViewById(R.id.button_1);
//    button1.setOnClickListener(view1 -> done += 1);

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
      getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          main.updateClock();
        }
      });

    }
  }




    //TODO create a timeout task and tie to activity timer
    private void startActivityTimer() {
      activityTimer = new Timer();
      activityTimerStart = System.currentTimeMillis();
      clockTimer = new Timer();
      clockTimer.schedule(new ClockTimerTask(), 0, 100);
    }


  }

package com.chigbrowsoftware.kgo.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.chigbrowsoftware.kgo.R;
import com.chigbrowsoftware.kgo.controller.MainActivity;
import java.util.Timer;

public class TaskFragment extends Fragment {

  Button button1;
  public static int done = 0;

  private Timer activityTimer;
  private Timer clockTimer;

  private MainActivity activity;
  private CompleteFragment completeFragment;

  private long activityTimeElapsed;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_task, container, false);

    TextView tv = view.findViewById(R.id.frag_1);
    tv.setText(getArguments().getString("msg"));

    button1 = view.findViewById(R.id.button_1);
    button1.setOnClickListener(view1 -> {
      if (done < 5) {
        MainActivity.pager.setCurrentItem(getNextPossibleItemIndex(1), true);
        done += 1;
      }else if (done == 5) {
        activityTimeElapsed = activity.getActivityTimeElapsed();
        CompleteFragment.newInstance((Long.toString(activityTimeElapsed)));
        activityComplete();
      }
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

  private int getNextPossibleItemIndex (int change) {

    int currentIndex = MainActivity.pager.getCurrentItem();
    int total = MainActivity.NUM_PAGES;

    if (currentIndex + change < 0) {
      return 0;
    }
    return Math.abs((currentIndex + change) % total);
  }

  public void activityComplete () {
    if (done == 5) {
      activityTimer = activity.getActivityTimer();
      activity.stopActivityTimer();
      clockTimer = activity.getClockTimer();
      activity.stopClockTimer();
    }
  }



}
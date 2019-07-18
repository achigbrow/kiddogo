package com.chigbrowsoftware.kgo.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.chigbrowsoftware.kgo.R;
import com.chigbrowsoftware.kgo.MainActivity;
import com.chigbrowsoftware.kgo.controller.ResultsActivity;
import java.util.Timer;

public class TaskFragment extends Fragment {

  Button button1;
  public static int done;
  int total = MainActivity.NUM_PAGES;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_task, container, false);

    TextView tv = view.findViewById(R.id.frag_1);
    tv.setText(getArguments().getString("msg"));

    button1 = view.findViewById(R.id.button_1);
    button1.setOnClickListener(view1 -> {
      if (MainActivity.pager.getCurrentItem() < total - 1) {
        MainActivity.pager.setCurrentItem(getNextPossibleItemIndex(1), true);
        done = done + 1;
      } else if (MainActivity.pager.getCurrentItem() == total - 1) {
        Intent intent = new Intent(getContext(), ResultsActivity.class);
        intent.putExtra("caller", "TaskFragment");
        startActivity(intent);
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

    if (currentIndex + change < 0) {
      return 0;
    }
    return Math.abs((currentIndex + change) % total);
  }




}
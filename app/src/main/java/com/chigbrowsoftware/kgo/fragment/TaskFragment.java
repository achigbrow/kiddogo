package com.chigbrowsoftware.kgo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.chigbrowsoftware.kgo.R;
import com.chigbrowsoftware.kgo.controller.MainActivity;
import com.chigbrowsoftware.kgo.controller.ResultsActivity;

/**
 * Provides the framework for the getting ready for school task.
 */
public class TaskFragment extends Fragment {

  public static int done;
  Button button1;
  int total = MainActivity.NUM_PAGES;

  /**
   * Provides a way to create an instance of the Task Fragment. Used in the Main Activity.
   */
  public static TaskFragment newInstance(String text) {

    TaskFragment frag1 = new TaskFragment();
    Bundle bundle = new Bundle();
    bundle.putString("msg", text);

    frag1.setArguments(bundle);

    return frag1;
  }

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

  private int getNextPossibleItemIndex(int change) {

    int currentIndex = MainActivity.pager.getCurrentItem();

    if (currentIndex + change < 0) {
      return 0;
    }
    return Math.abs((currentIndex + change) % total);
  }

  //  Copyright [2019] [Alana Chigbrow]
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.

}
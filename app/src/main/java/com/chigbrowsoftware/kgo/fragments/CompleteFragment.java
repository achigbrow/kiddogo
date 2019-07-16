package com.chigbrowsoftware.kgo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.chigbrowsoftware.kgo.R;

public class CompleteFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_complete, container, false);

    TextView tv = view.findViewById(R.id.frag_2);
    tv.setText(getArguments().getString("msg"));

    return view;
  }

  public static TaskFragment newInstance(String text) {

    TaskFragment frag1 = new TaskFragment();
    Bundle bundle = new Bundle();
    bundle.putString("msg", text);

    frag1.setArguments(bundle);

    return frag1;
  }

}

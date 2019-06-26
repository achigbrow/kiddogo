package com.chigbrowsoftware.kgo.screenfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.chigbrowsoftware.kgo.R;

public class ScreenSlidePageFragment extends Fragment {

  private String task;
  private int page;

  // newInstance constructor for creating fragment with arguments
  public static ScreenSlidePageFragment newInstance(int page, String task) {
    ScreenSlidePageFragment fragmentFirst = new ScreenSlidePageFragment();
    Bundle args = new Bundle();
    args.putInt("someInt", page);
    args.putString("someTask", task);
    fragmentFirst.setArguments(args);
    return fragmentFirst;
  }

  // Store instance variables based on arguments passed
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    page = getArguments().getInt("someInt", 0);
    task = getArguments().getString("someTask");
  }

  // Inflate the view for the fragment based on layout XML
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
    //TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
   // tvLabel.setText(page + " -- " + title);
    return view;
  }
}




//  @Nullable
//  @Override
//  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//      @Nullable Bundle savedInstanceState) {
//    ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container,
//        false);
//    return rootView;
//  }


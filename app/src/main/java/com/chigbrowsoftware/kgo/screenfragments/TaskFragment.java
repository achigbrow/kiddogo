package com.chigbrowsoftware.kgo.screenfragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.chigbrowsoftware.kgo.R;

public class TaskFragment extends Fragment {

  Button button1;
  int done =0;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_task, container, false);


    TextView tv = view.findViewById(R.id.frag_1);
    tv.setText(getArguments().getString("msg"));

    button1 = view.findViewById(R.id.button_1);
    button1.setOnClickListener(view1 -> done += 1);

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
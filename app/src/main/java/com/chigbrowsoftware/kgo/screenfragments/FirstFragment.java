package com.chigbrowsoftware.kgo.screenfragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.chigbrowsoftware.kgo.R;

public class FirstFragment extends Fragment {


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_first, container, false);

    TextView tv = view.findViewById(R.id.frag_1);
    tv.setText(getArguments().getString("msg"));

    return view;
  }

  public static FirstFragment newInstance(String text) {

    FirstFragment frag1 = new FirstFragment();
    Bundle bundle = new Bundle();
    bundle.putString("msg", text);

    frag1.setArguments(bundle);

    return frag1;
  }
}
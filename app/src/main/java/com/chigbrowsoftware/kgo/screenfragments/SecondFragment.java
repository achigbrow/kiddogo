package com.chigbrowsoftware.kgo.screenfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.chigbrowsoftware.kgo.R;

public class SecondFragment extends Fragment {


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_second, container, false);

    TextView tv = view.findViewById(R.id.frag_2);
    tv.setText(getArguments().getString("msg"));

    return view;
  }

  public static SecondFragment newInstance(String text) {

    SecondFragment frag2 = new SecondFragment();
    Bundle bundle = new Bundle();
    bundle.putString("msg", text);

    frag2.setArguments(bundle);

    return frag2;
  }
}


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

public class ThirdFragment extends Fragment {


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_third, container, false);

    TextView tv = view.findViewById(R.id.frag_3);
    tv.setText(getArguments().getString("msg"));

    return view;
  }

  public static ThirdFragment newInstance(String text) {

    ThirdFragment frag3 = new ThirdFragment();
    Bundle bundle = new Bundle();
    bundle.putString("msg", text);

    frag3.setArguments(bundle);

    return frag3;
  }
}


package com.chigbrowsoftware.kgo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.chigbrowsoftware.kgo.screenfragments.ScreenSlidePageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  private TextView mTextMessage;

  private SmartFragmentStatePagerAdapter adapterViewPager;

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_play:
          mTextMessage.setText(R.string.title_play);
          return true;
        case R.id.navigation_dashboard:
          mTextMessage.setText(R.string.title_dashboard);
          return true;
        case R.id.navigation_settings:
          mTextMessage.setText(R.string.title_settings);
          return true;
      }
      return false;
    }
  };

  public void onButtonClick(View v) {
    Intent intent = new Intent(this, ScreenSlidePagerActivity.class);
    startActivity(intent);
  }

  public static class MyPagerAdapter extends SmartFragmentStatePagerAdapter {

    private static int NUM_ITEMS = 5;

    public MyPagerAdapter(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
      return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0: // Fragment # 0 - This will show FirstFragment
          return ScreenSlidePageFragment.newInstance(0, "Get dressed.");
        case 1: // Fragment # 0 - This will show FirstFragment different title
          return ScreenSlidePageFragment.newInstance(1, "Brush your teeth.");
        case 2: // Fragment # 1 - This will show SecondFragment
          return ScreenSlidePageFragment.newInstance(2, "Make your bed.");
        case 3:
          return ScreenSlidePageFragment.newInstance(3, "Put your shoes on.");
        case 4:
          return ScreenSlidePageFragment.newInstance(4, "Get your backpack ready.");
        default:
          return ScreenSlidePageFragment.newInstance(5, "Good morning!");
      }
    }
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
  }



}

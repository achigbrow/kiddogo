package com.chigbrowsoftware.kgo.controller;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.chigbrowsoftware.kgo.R;
import com.chigbrowsoftware.kgo.screenfragments.FirstFragment;
import com.chigbrowsoftware.kgo.screenfragments.SecondFragment;
import com.chigbrowsoftware.kgo.screenfragments.ThirdFragment;

public class ScreenSlidePagerActivity extends FragmentActivity {

  private final static int NUM_PAGES = 5;

  private ViewPager pager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_screen_slide);

    pager = findViewById(R.id.viewPager);
    pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
  }


  @Override
  public void onBackPressed() {
    if (pager.getCurrentItem() == 0) {
      super.onBackPressed();
    }else {
      pager.setCurrentItem(pager.getCurrentItem()-1);
    }
  }

  private class MyPagerAdapter extends FragmentStatePagerAdapter {
    public MyPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
      switch (pos) {

        case 0: return FirstFragment.newInstance("Get dressed.");
        case 1: return SecondFragment.newInstance("Brush your teeth.");
        case 2: return ThirdFragment.newInstance("Make your bed.");
        case 3: return FirstFragment.newInstance("Put your shoes on.");
        case 4: return SecondFragment.newInstance("Get your backpack ready.");
        default: return ThirdFragment.newInstance("Good morning!");
      }
    }

    @Override
    public int getCount() {
      return NUM_PAGES;
    }
  }
}
package com.chigbrowsoftware.kgo.controller;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.chigbrowsoftware.kgo.R;
import com.chigbrowsoftware.kgo.fragments.TaskFragment;

public class ViewPagerActivity extends FragmentActivity {

  public final static int NUM_PAGES = 5;

  public static androidx.viewpager.widget.ViewPager pager;

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

//TODO Add Result fragment as case 5.
    @Override
    public Fragment getItem(int pos) {
      switch (pos) {

        case 0: return TaskFragment.newInstance("Get dressed.");
        case 1: return TaskFragment.newInstance("Brush your teeth.");
        case 2: return TaskFragment.newInstance("Make your bed.");
        case 3: return TaskFragment.newInstance("Put your shoes on.");
        case 4: return TaskFragment.newInstance("Get your backpack ready.");
        default: return TaskFragment.newInstance("Good morning!");
      }
    }



    @Override
    public int getCount() {
      return NUM_PAGES;
    }
  }


}
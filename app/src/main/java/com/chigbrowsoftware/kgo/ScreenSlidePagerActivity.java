package com.chigbrowsoftware.kgo;

import android.os.Bundle;
import android.widget.AdapterView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.chigbrowsoftware.kgo.MainActivity.MyPagerAdapter;


public class ScreenSlidePagerActivity extends FragmentActivity {

  private ViewPager mPager;
  //private MyPagerAdapter adapterViewPager;
  private PagerAdapter pagerAdapter;
  MyPagerAdapter adapterViewPager;

  private static final int NUM_PAGES = 5;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_screen_slide);
    ViewPager mPager = findViewById(R.id.task_pager);
    adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
    mPager.setAdapter(adapterViewPager);
  }

  //
//
//
//
//
//
//  @Override
//  protected void onCreate(@Nullable Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//
//    mPager = findViewById(R.id.task_pager);
//    pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
//    mPager.setAdapter(pagerAdapter);
//  }
//
  @Override
  public void onBackPressed() {
    if (mPager.getCurrentItem() == 0) {
      super.onBackPressed();
    } else {
      mPager.setCurrentItem(mPager.getCurrentItem() - 1);
    }
  }

  private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    public ScreenSlidePagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return null;
    }

    @Override
    public int getCount() {
      return NUM_PAGES;
    }
  }
}


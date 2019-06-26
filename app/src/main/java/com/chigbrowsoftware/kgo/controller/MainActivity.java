package com.chigbrowsoftware.kgo.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import com.chigbrowsoftware.kgo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  private String user = "Noam";
  private TextView userDisplay;
  private ViewGroup userContainer;

  private Button btn;

  private TextView mTextMessage;

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    btn = findViewById(R.id.button);
    btn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent pageViewer =
            new Intent(MainActivity.this, ScreenSlidePagerActivity.class);
        startActivity(pageViewer);
      }
    });
//    userContainer = (ViewGroup) userDisplay.getParent();
    mTextMessage = findViewById(R.id.message);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
  }

}

package com.chigbrowsoftware.kgo.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import com.chigbrowsoftware.kgo.R;
import com.chigbrowsoftware.kgo.model.User;
import com.chigbrowsoftware.kgo.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener {


  private Button btn;

  private TextView mTextMessage;
  private SharedPreferences preferences;
  private String userName;
  private int timeLimit;


  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      boolean handled = true;
      Intent intent;

      switch (item.getItemId()) {
        case R.id.navigation_play:
          return true;
        case R.id.navigation_dashboard:
          mTextMessage.setText(R.string.title_dashboard);
          return true;
        case R.id.navigation_settings:
          intent = new Intent(getApplicationContext(), SettingsActivity.class);
          startActivity(intent);
          return true;
      }
      return handled;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    btn = findViewById(R.id.button);
    btn.setText("Noam");
    btn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent pageViewer =
            new Intent(MainActivity.this, ScreenSlidePagerActivity.class);
        startActivity(pageViewer);
      }
    });
    mTextMessage = findViewById(R.id.message);
    preferences = PreferenceManager.getDefaultSharedPreferences(this);
    preferences.registerOnSharedPreferenceChangeListener(this);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    readSettings();
  }

  private void readSettings() {
    Resources res = getResources();
    userName = preferences.getString("username", "username");
    timeLimit = preferences.getInt("timer", 15);
    UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    User user = new User();
    user.setName(userName);
    userViewModel.addUser(user);
  }


}

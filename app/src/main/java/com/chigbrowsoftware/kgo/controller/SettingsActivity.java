package com.chigbrowsoftware.kgo.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SeekBarPreference;
import com.chigbrowsoftware.kgo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

//TODO Add ability to enter up to 4 users.

/**
 * Allows the user to set their kiddo's name, email (for future use potentially), and a time limit
 * for the activity.
 */
public class SettingsActivity extends AppCompatActivity {


  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    /**
     * Sets bottom navigation for Settings Activity.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      boolean handled = true;
      Intent intent;

      switch (item.getItemId()) {
        case R.id.navigation_play:
          intent = new Intent(getApplicationContext(), MainActivity.class);
          startActivity(intent);
          return true;
        case R.id.navigation_dashboard:
          intent = new Intent(getApplicationContext(), ResultsActivity.class);
          startActivity(intent);
          return true;
        case R.id.navigation_settings:
          intent = new Intent(getApplicationContext(), SettingsActivity.class);
          startActivity(intent);
          return true;
      }
      return handled;
    }
  };

  /**
   * Creates Settings Activity.
   */
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.settings_fragment_container, new SettingsFragment())
          .commit();
    }
  }

  /**
   * Sets initial values for Preferences.
   */
  public static class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
      setPreferencesFromResource(R.xml.preferences, rootKey);
      PreferenceScreen screen = getPreferenceScreen();
      SeekBarPreference timer =
          (SeekBarPreference) screen.findPreference("timer");
      timer.setMin(15);
      int timerIncrement = 5;
      timer.setOnPreferenceChangeListener(((preference, newValue) -> {
        int roundedValue = Math.round((Integer) newValue / (float) timerIncrement) * timerIncrement;
        timer.setValue(roundedValue);
        return false;
      }));


    }
  }

  //  Copyright [2019] [Alana Chigbrow]
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
}

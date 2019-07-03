package com.chigbrowsoftware.kgo.controller;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SeekBarPreference;
import com.chigbrowsoftware.kgo.R;

//TODO Add ability to enter up to 4 users.

public class SettingsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.settings_fragment_container, new SettingsFragment())
          .commit();
    }
  }

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
}

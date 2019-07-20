package com.chigbrowsoftware.kgo.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import com.chigbrowsoftware.kgo.R;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.chigbrowsoftware.kgo.model.entity.ActivityEntity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultsActivity extends AppCompatActivity {

  //TODO Set daily view different from week view.

  private TextView mTextMessage;
  private TextView dayOfWeek;
  private String caller;
  private String day;

  private boolean result;
  private boolean isCompleteView;

  private String succeed = "Great job!";
  private String fail = "You can get it!";

  private SharedPreferences preferences;
  private SharedPreferences.Editor editor;


  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @SuppressWarnings("SwitchStatementWithoutDefaultBranch")
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

  //TODO Fix static view
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_results);

    findViewById(R.id.navigation_dashboard).setVisibility(View.INVISIBLE);
    findViewById(R.id.navigation_settings).setVisibility(View.INVISIBLE);

    mTextMessage = (TextView) findViewById(R.id.message);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);
    editor = preferences.edit();

    caller = getIntent().getStringExtra("caller");
    try {
      Class callerClass = Class.forName(caller);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    isCompleteView();
    setResultsViews();
  }

  private boolean isCompleteView() {
    if (caller.equals("TaskFragment")) {
      isCompleteView = true;
    }
    return isCompleteView;
  }

  private String getDay() {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.US);
    day = simpleDateFormat.format(date);
    return day;
  }

  private void setResultsViews() {

    //TODO Use live data to do this.
    Thread t = new Thread(() -> {
      ActivitiesDatabase db = ActivitiesDatabase.getInstance(getApplication());
      ActivityEntity activity = db.activityDao().getRecentActivity();
      this.result = activity.isResult();
    });
    t.start();
    try { //TODO Handle appropriately. Do something.
      t.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    if (isCompleteView) {
      hideDays();
      getDay();

      if (result) {

        switch (day) {
          case "Monday":
            clearResults();
            editor.clear();
            editor.commit();
            completeSuccessView();
            editor.putString("mon_result", succeed);
            editor.commit();
            break;
          case "Tuesday":
            completeSuccessView();
            editor.putString("tue_result", succeed);
            editor.commit();
            setMon();
            break;
          case "Wednesday":
            completeSuccessView();
            editor.putString("wed_result", succeed);
            editor.commit();
            setMon();
            setTue();
            break;
          case "Thursday":
            completeSuccessView();
            editor.putString("thu_result", succeed);
            editor.commit();
            setMon();
            setTue();
            setWed();
            break;
          case "Friday":
            editor.remove("fri_result"); //TODO remove after testing
            editor.commit();
            completeSuccessView();
            editor.putString("fri_result", succeed);
            editor.commit();
            setMon();
            setTue();
            setWed();
            setThu();
            break;
          default:
            setMon();
            setTue();
            setWed();
            setThu();
            setFri();
        }
      } else if (!result) {

        switch (day) {
          case "Monday":
            clearResults();
            editor.clear();
            editor.commit();
            completeFailView();
            editor.putString("mon_result", fail);
            editor.commit();
            break;
          case "Tuesday":
            completeFailView();
            editor.putString("tue_result", fail);
            editor.commit();
            setMon();
            break;
          case "Wednesday":
            completeFailView();
            editor.putString("wed_result", fail);
            editor.commit();
            setMon();
            setTue();
            break;
          case "Thursday":
            completeFailView();
            editor.putString("thu_result", fail);
            editor.commit();
            setMon();
            setTue();
            setWed();
            break;
          case "Friday":
            completeFailView();
            editor.putString("fri_result", fail);
            editor.commit();
            setMon();
            setTue();
            setWed();
            setThu();
            break;
          default:
            setMon();
            setTue();
            setWed();
            setThu();
            setFri();
        }
      }
    } else if (!isCompleteView) {
      setMon();
      setTue();
      setWed();
      setThu();
      setFri();
    }
  }

  private void clearResults() {
    findViewById(R.id.star1).setVisibility(View.INVISIBLE);
    findViewById(R.id.star2).setVisibility(View.INVISIBLE);
    findViewById(R.id.star3).setVisibility(View.INVISIBLE);
    findViewById(R.id.star4).setVisibility(View.INVISIBLE);
    findViewById(R.id.star5).setVisibility(View.INVISIBLE);
    findViewById(R.id.x1).setVisibility(View.INVISIBLE);
    findViewById(R.id.x2).setVisibility(View.INVISIBLE);
    findViewById(R.id.x3).setVisibility(View.INVISIBLE);
    findViewById(R.id.x4).setVisibility(View.INVISIBLE);
    findViewById(R.id.x5).setVisibility(View.INVISIBLE);
  }

  private void setMon() {
    try {
      if (!preferences.getString("mon_result", null).equals(null)
          && preferences.getString("mon_result", null).equals(succeed)) {
        findViewById(R.id.star1).setVisibility(View.VISIBLE);
      } else if (!preferences.getString("mon_result", null).equals(null)
          && preferences.getString("mon_result", null).equals(fail)) {
        findViewById(R.id.x1).setVisibility(View.VISIBLE);
      } else {
        findViewById(R.id.star1).setVisibility(View.INVISIBLE);
        findViewById(R.id.x1).setVisibility(View.INVISIBLE);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setTue() {
    try {
      if (!preferences.getString("tue_result", null).equals(null)
          && preferences.getString("tue_result", null).equals(succeed)) {
        findViewById(R.id.star2).setVisibility(View.VISIBLE);
      } else if (!preferences.getString("tue_result", null).equals(null)
          && preferences.getString("tue_result", null).equals(fail)) {
        findViewById(R.id.x2).setVisibility(View.VISIBLE);
      } else {
        findViewById(R.id.star2).setVisibility(View.INVISIBLE);
        findViewById(R.id.x2).setVisibility(View.INVISIBLE);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setWed() {
    try {
      if (!preferences.getString("wed_result", null).equals(null)
          && preferences.getString("wed_result", null).equals(succeed)) {
        findViewById(R.id.star3).setVisibility(View.VISIBLE);
      } else if (!preferences.getString("wed_result", null).equals(null)
          && preferences.getString("wed_result", null).equals(fail)) {
        findViewById(R.id.x3).setVisibility(View.VISIBLE);
      } else {
        findViewById(R.id.star3).setVisibility(View.INVISIBLE);
        findViewById(R.id.x3).setVisibility(View.INVISIBLE);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setThu() {
    try {
      if (!preferences.getString("thu_result", null).equals(null)
         && preferences.getString("thu_result", null).equals(succeed)) {
        findViewById(R.id.star4).setVisibility(View.VISIBLE);
      } else if (preferences.getString("thu_result", null).equals(fail)) {
        findViewById(R.id.x4).setVisibility(View.VISIBLE);
      } else {
        findViewById(R.id.star4).setVisibility(View.INVISIBLE);
        findViewById(R.id.x4).setVisibility(View.INVISIBLE);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setFri() {
    try {
      if (!preferences.getString("fri_result", null).equals(null)
          && preferences.getString("fri_result", null).equals(succeed)) {
        findViewById(R.id.star5).setVisibility(View.VISIBLE);
      } else if (preferences.getString("fri_result", null).equals(fail)) {
        findViewById(R.id.x5).setVisibility(View.VISIBLE);
      } else {
        findViewById(R.id.star5).setVisibility(View.INVISIBLE);
        findViewById(R.id.x5).setVisibility(View.INVISIBLE);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void completeSuccessView () {
    getDay();
    dayOfWeek = findViewById(R.id.day);
    dayOfWeek.setVisibility(View.VISIBLE);
    dayOfWeek.setText(day);
    findViewById(R.id.complete_star).setVisibility(View.VISIBLE);
    TextView view = findViewById(R.id.message);
    view.setVisibility(View.VISIBLE);
    view.setText(succeed);
  }

  private void completeFailView () {
    getDay();
    dayOfWeek = findViewById(R.id.day);
    dayOfWeek.setVisibility(View.VISIBLE);
    dayOfWeek.setText(day);
    findViewById(R.id.complete_x).setVisibility(View.VISIBLE);
    TextView view = findViewById(R.id.message);
    view.setVisibility(View.VISIBLE);
    view.setText(fail);
  }

  private void  hideDays() {
    findViewById(R.id.monday).setVisibility(View.INVISIBLE);
    findViewById(R.id.tuesday).setVisibility(View.INVISIBLE);
    findViewById(R.id.wednesday).setVisibility(View.INVISIBLE);
    findViewById(R.id.thursday).setVisibility(View.INVISIBLE);
    findViewById(R.id.friday).setVisibility(View.INVISIBLE);
  }
}




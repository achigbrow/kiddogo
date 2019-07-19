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
  private String caller;
  private String day;

  private boolean result;
  private boolean isResultsView;

  private String mon_result;
  private String tue_result;
  private String wed_result;
  private String thu_result;
  private String fri_result;

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

    isResultsView();
    setResultsViews();
  }

  private boolean isResultsView() {
    if (caller.equals("TaskFragment")) {
      isResultsView = true;
    }
    return isResultsView;
  }

  private String getDay() {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E", Locale.US);
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

    if (isResultsView) {
      getDay();

      if (result) {

        switch (day) {
          case "Mon":
            clearResults();
            editor.clear();
            editor.commit();
            findViewById(R.id.star1).setVisibility(View.VISIBLE);
            editor.putString("mon_result", succeed);
            editor.commit();
            break;
          case "Tue":
            findViewById(R.id.star2).setVisibility(View.VISIBLE);
            editor.putString("tue_result", succeed);
            editor.commit();
            setMon();
            break;
          case "Wed":
            findViewById(R.id.star3).setVisibility(View.VISIBLE);
            editor.putString("wed_result", succeed);
            editor.commit();
            setMon();
            setTue();
            break;
          case "Thu":
            findViewById(R.id.star4).setVisibility(View.VISIBLE);
            editor.putString("thu_result", succeed);
            editor.commit();
            setMon();
            setTue();
            setWed();
            break;
          case "Fri":
            editor.remove("fri_result"); //TODO remove after testing
            editor.commit();
            findViewById(R.id.star5).setVisibility(View.VISIBLE);
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
          case "Mon":
            clearResults();
            editor.clear();
            editor.commit();
            findViewById(R.id.x1).setVisibility(View.VISIBLE);
            editor.putString("mon_result", fail);
            editor.commit();
            break;
          case "Tue":
            findViewById(R.id.x2).setVisibility(View.VISIBLE);
            editor.putString("tue_result", fail);
            editor.commit();
            setMon();
            break;
          case "Wed":
            findViewById(R.id.x3).setVisibility(View.VISIBLE);
            editor.putString("wed_result", fail);
            editor.commit();
            setMon();
            setTue();
            break;
          case "Thu":
            findViewById(R.id.x4).setVisibility(View.VISIBLE);
            editor.putString("thu_result", fail);
            editor.commit();
            setMon();
            setTue();
            setWed();
            break;
          case "Fri":
            findViewById(R.id.x5).setVisibility(View.VISIBLE);
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
    } else if (caller == "MainActivity") {
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
    if (preferences.getString("mon_result", null) == succeed) {
      findViewById(R.id.star1).setVisibility(View.VISIBLE);
    } else if (preferences.getString("mon_result", null) == fail) {
      findViewById(R.id.x1).setVisibility(View.VISIBLE);
    } else {
      findViewById(R.id.star1).setVisibility(View.INVISIBLE);
      findViewById(R.id.x1).setVisibility(View.INVISIBLE);
    }
  }

  private void setTue() {
    if (preferences.getString("tue_result", null) == succeed) {
      findViewById(R.id.star2).setVisibility(View.VISIBLE);
    } else if (preferences.getString("tue_result", null) == fail) {
      findViewById(R.id.x2).setVisibility(View.VISIBLE);
    } else {
      findViewById(R.id.star2).setVisibility(View.INVISIBLE);
      findViewById(R.id.x2).setVisibility(View.INVISIBLE);
    }
  }

  private void setWed() {
    if (preferences.getString("wed_result", null) == succeed) {
      findViewById(R.id.star3).setVisibility(View.VISIBLE);
    } else if (preferences.getString("wed_result", null) == fail) {
      findViewById(R.id.x3).setVisibility(View.VISIBLE);
    } else {
      findViewById(R.id.star3).setVisibility(View.INVISIBLE);
      findViewById(R.id.x3).setVisibility(View.INVISIBLE);
    }
  }

  private void setThu() {
    if (preferences.getString("thu_result", null) == succeed) {
      findViewById(R.id.star4).setVisibility(View.VISIBLE);
    } else if (preferences.getString("thu_result", null) == fail) {
      findViewById(R.id.x4).setVisibility(View.VISIBLE);
    } else {
      findViewById(R.id.star4).setVisibility(View.INVISIBLE);
      findViewById(R.id.x4).setVisibility(View.INVISIBLE);
    }
  }

  private void setFri() {
    if (preferences.getString("fri_result", null) == succeed) {
      findViewById(R.id.star5).setVisibility(View.VISIBLE);
    } else if (preferences.getString("fri_result", null) == fail) {
      findViewById(R.id.x5).setVisibility(View.VISIBLE);
    } else {
      findViewById(R.id.star5).setVisibility(View.INVISIBLE);
      findViewById(R.id.x5).setVisibility(View.INVISIBLE);
    }
  }
}




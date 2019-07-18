package com.chigbrowsoftware.kgo.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import com.chigbrowsoftware.kgo.MainActivity;
import com.chigbrowsoftware.kgo.R;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.chigbrowsoftware.kgo.model.entity.ActivityEntity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public class ResultsActivity extends AppCompatActivity {

  private TextView mTextMessage;
  private boolean isResultsView;
  private String caller;
  private String day;
  boolean result;


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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_results);

    mTextMessage = (TextView) findViewById(R.id.message);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    caller = getIntent().getStringExtra("caller");
    try {
      Class callerClass = Class.forName(caller);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    isResultsView();
    setViews();
  }

  private boolean isResultsView() {
    if (caller.equals("TaskFragment")) {
      isResultsView = true;
    }
    return isResultsView;
  }

  private String getDay () {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E", Locale.US);
    day = simpleDateFormat.format(date);
    return day;
  }


  private void setViews () {

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
          case "Mon" :
            clearResults();
            findViewById(R.id.star1).setVisibility(View.VISIBLE);
            break;
          case "Tues" :
            findViewById(R.id.star2).setVisibility(View.VISIBLE);
            break;
          case "Wed" :
            findViewById(R.id.star3).setVisibility(View.VISIBLE);
            break;
          case "Thu" :
            findViewById(R.id.star4).setVisibility(View.VISIBLE);
            break;
          case "Fri" :
            findViewById(R.id.star5).setVisibility(View.VISIBLE);
            break;
            default:
              //do nothing
        }
      } else if (!result) {

        switch (day) {
          case "Mon" :
            clearResults();
            findViewById(R.id.x1).setVisibility(View.VISIBLE);
            break;
          case "Tues" :
            findViewById(R.id.x2).setVisibility(View.VISIBLE);
            break;
          case "Wed" :
            findViewById(R.id.x3).setVisibility(View.VISIBLE);
            break;
          case "Thu" :
            findViewById(R.id.x4).setVisibility(View.VISIBLE);
            break;
          case "Fri" :
            findViewById(R.id.x5).setVisibility(View.VISIBLE);
            break;
          default:
            //do nothing
        }
      }
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

}

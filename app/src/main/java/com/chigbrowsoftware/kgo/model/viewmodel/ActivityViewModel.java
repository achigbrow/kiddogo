package com.chigbrowsoftware.kgo.model.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.chigbrowsoftware.kgo.model.entity.ActivityEntity;
import java.util.List;

public class ActivityViewModel extends AndroidViewModel {

  private LiveData<List<ActivityEntity>> activities;

  public ActivityViewModel(@NonNull Application application) {
    super(application);
    ActivitiesDatabase db = ActivitiesDatabase.getInstance(application);
    activities = db.activityDao().getAll();
  }

  public LiveData<List<ActivityEntity>> getAcitivites() {
    return activities;
  }

  public void addActivity(final ActivityEntity activity) {
    new Thread((Runnable) () -> {
      ActivitiesDatabase db = ActivitiesDatabase.getInstance(getApplication());
      db.activityDao().insert(activity);
    }).start();
  }
}

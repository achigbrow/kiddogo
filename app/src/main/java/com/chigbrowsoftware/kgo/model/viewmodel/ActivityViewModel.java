package com.chigbrowsoftware.kgo.model.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.chigbrowsoftware.kgo.model.entity.Activity;
import java.util.List;

/**
 * Provides access to the Activity Entity.
 */
public class ActivityViewModel extends AndroidViewModel {

  private LiveData<List<Activity>> activities;

  /**
   * Provides access to the Activity Dao.
   */
  public ActivityViewModel(@NonNull Application application) {
    super(application);
    ActivitiesDatabase db = ActivitiesDatabase.getInstance(application);
    activities = db.activityDao().getAll();
  }

  /**
   * Returns a LiveData list of all activities.
   */
  public LiveData<List<Activity>> getActivities() {
    return activities;
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

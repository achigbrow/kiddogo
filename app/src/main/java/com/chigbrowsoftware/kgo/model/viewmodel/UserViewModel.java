package com.chigbrowsoftware.kgo.model.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.chigbrowsoftware.kgo.model.entity.UserEntity;
import java.util.List;

/**
 * Provides access to the UserEntity Entity.
 */
public class UserViewModel extends AndroidViewModel {

  private final LiveData<List<UserEntity>> users;

  /**
   * Provides access to the UserEntity Dao.
   */
  public UserViewModel(@NonNull Application application) {
    super(application);
    ActivitiesDatabase db = ActivitiesDatabase.getInstance(application);
    users = db.userDao().getAll();
  }

  /**
   * Returns a LiveData List of all Users.
   */
  public LiveData<List<UserEntity>> getUsersLiveData() {
    return users;
  }

  /**
   * Adds a new user.
   */
  public void addUser(final UserEntity user) {
    new Thread((Runnable) () -> {
      ActivitiesDatabase db = ActivitiesDatabase.getInstance(getApplication());
      db.userDao().insert(user);
    }).start();
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
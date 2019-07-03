package com.chigbrowsoftware.kgo.model.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.chigbrowsoftware.kgo.model.entity.UserEntity;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import java.util.List;

public class UserViewModel extends AndroidViewModel {

  private final LiveData<List<UserEntity>> users;

  public UserViewModel(@NonNull Application application) {
    super(application);
    ActivitiesDatabase db = ActivitiesDatabase.getInstance(application);
    users = db.userDao().getAll();
  }

  public LiveData<List<UserEntity>> getUsersLiveData() {
    return users;
  }

  public void addUser(final UserEntity user) {
    new Thread((Runnable) () -> {
      ActivitiesDatabase db = ActivitiesDatabase.getInstance(getApplication());
      db.userDao().insert(user);
    }).start();
  }



}
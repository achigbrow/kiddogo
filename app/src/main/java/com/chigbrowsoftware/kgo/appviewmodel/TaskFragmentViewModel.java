package com.chigbrowsoftware.kgo.appviewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.chigbrowsoftware.kgo.fragments.TaskFragment;

public class TaskFragmentViewModel extends AndroidViewModel {

  public boolean taskComplete;
// TODO Add condition if timer <= time limit to true requirement.

  public TaskFragmentViewModel(@NonNull Application application) {
    super(application);
    if (TaskFragment.done == 5) {
      taskComplete = true;

    }
    taskComplete = false;
  }



}

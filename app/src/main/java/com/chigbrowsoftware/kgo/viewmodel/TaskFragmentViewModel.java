package com.chigbrowsoftware.kgo.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.chigbrowsoftware.kgo.screenfragments.TaskFragment;

public class TaskFragmentViewModel extends AndroidViewModel {

  int taskComplete;

  public TaskFragmentViewModel(@NonNull Application application) {
    super(application);
  }

}

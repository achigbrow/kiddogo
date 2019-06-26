package com.chigbrowsoftware.kgo.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.chigbrowsoftware.kgo.model.Activity;
import java.util.List;


@Dao
public interface ActivityDao {

  @Insert
  long insert(Activity activity);

  @Query("SELECT * FROM activity")
  LiveData<List<Activity>> getAll();

}

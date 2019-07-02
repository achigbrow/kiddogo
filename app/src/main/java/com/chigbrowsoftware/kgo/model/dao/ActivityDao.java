package com.chigbrowsoftware.kgo.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.chigbrowsoftware.kgo.model.entity.ActivityEntity;
import java.util.List;


@Dao
public interface ActivityDao {

  @Insert
  long insert(ActivityEntity activity);

  @Query("SELECT * FROM ActivityEntity")
  LiveData<List<ActivityEntity>> getAll();

}

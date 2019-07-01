package com.chigbrowsoftware.kgo.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.chigbrowsoftware.kgo.model.User;
import java.util.List;

@Dao
public interface UserDao {

  @Insert
  long insert(User user);

  @Query("SELECT * FROM user")
  LiveData<List<User>> getAll();

  @Query("SELECT * FROM user WHERE user_id = :id")
  LiveData<User> findById(Long id);

  @Query("SELECT user_id, MAX(user_id) as maxId FROM user")
  long getMaxId();



  @Delete
  int delete(User... user);

}

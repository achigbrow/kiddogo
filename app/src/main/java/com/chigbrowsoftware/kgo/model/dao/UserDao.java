package com.chigbrowsoftware.kgo.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.chigbrowsoftware.kgo.model.entity.UserEntity;
import java.util.List;

@Dao
public interface UserDao {

  @Insert
  long insert(UserEntity user);

  @Query("SELECT * FROM UserEntity")
  LiveData<List<UserEntity>> getAll();

  @Query("SELECT * FROM UserEntity WHERE user_id = :id")
  LiveData<UserEntity> findById(Long id);

  @Query("SELECT * FROM UserEntity ORDER BY user_id desc LIMIT 1")
  UserEntity getLastUser();

  @Query("SELECT * FROM UserEntity ORDER BY user_id desc LIMIT 1")
  LiveData<UserEntity> getButtonLastUser();

  @Delete
  int delete(UserEntity... user);

}

package com.chigbrowsoftware.kgo.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.chigbrowsoftware.kgo.model.entity.UserEntity;
import java.util.List;

/** Sets insert for adding user, delete for removing user, and a variety of queries.*/
@Dao
public interface UserDao {

  @Insert
  long insert(UserEntity user);

  @Query("SELECT * FROM UserEntity")
  LiveData<List<UserEntity>> getAll();

  @Query("SELECT * FROM UserEntity WHERE user_id = :id")
  UserEntity findById(Long id);

  @Query("SELECT * FROM UserEntity ORDER BY user_id desc LIMIT 1")
  UserEntity getLastUser();

  @Query("SELECT * FROM UserEntity ORDER BY user_id desc LIMIT 1")
  LiveData<UserEntity> getButtonLastUser();

  @Delete
  int delete(UserEntity... user);

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

package com.chigbrowsoftware.kgo.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.chigbrowsoftware.kgo.model.entity.Activity;
import java.util.List;

/**Sets insert for adding and activity and queries to select an activity.*/
@Dao
public interface ActivityDao {

  @Insert
  long insert(Activity activity);

  @Query("SELECT * FROM Activity")
  LiveData<List<Activity>> getAll();

  @Query("SELECT * FROM Activity ORDER BY timestamp desc LIMIT 1")
  Activity getRecentActivity();

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

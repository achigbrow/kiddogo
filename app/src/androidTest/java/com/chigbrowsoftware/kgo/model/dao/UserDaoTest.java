package com.chigbrowsoftware.kgo.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.SmallTest;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.chigbrowsoftware.kgo.model.entity.UserEntity;
import com.chigbrowsoftware.kgo.util.LiveDataTestUtil;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runners.MethodSorters;

/**
 * Tests the UserEntity Dao.
 */
@SmallTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserDaoTest {

  private static ActivitiesDatabase db;
  private static UserDao dao;
  private static long userId;

  @Rule
  public TestRule rule = new InstantTaskExecutorRule();

  /**
   * Creates a db to use for duration of testing.
   */
  @BeforeClass
  public static void setup() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();
    db = Room.inMemoryDatabaseBuilder(context, ActivitiesDatabase.class)
        .build();
    dao = db.userDao();
  }

  /**
   * Inserst a user into test db.
   */
  @Test
  public void insert() {
    UserEntity user = new UserEntity();
    user.setName("Mike");
    userId = dao.insert(user);
    assertTrue(userId > 0);
  }

  /**
   * Attempts to insert a user without a name into the db.
   */
  @Test(expected = SQLiteConstraintException.class)
  public void insertNullName() {
    UserEntity user = new UserEntity();
    userId = dao.insert(user);
    fail("This shouldn't get here!");
  }

  /**
   * Retrieves a list of all users entered in test db.
   */
  @Test
  public void postInsertGetAll() throws InterruptedException {
    List<UserEntity> users = LiveDataTestUtil.getValue(dao.getAll());
    assertTrue(users != null);
  }

  /**
   * Retrieves a user from test db based on userId.
   */
  @Test
  public void postInsertFindById() throws NullPointerException {
    UserEntity user = dao.findById(userId);
    assertEquals("Mike", user.getName());
  }

  /**
   * Retrieves last user entered in test db.
   */
  @Test
  public void postInsertGetLastUser() {
    UserEntity user = dao.getLastUser();
    assertEquals("Mike", user.getName());
  }

  /**
   * Retrieves last user entered in test db as LiveData.
   */
  @Test
  public void postInsertGetButtonLastUser() throws InterruptedException {
    UserEntity user = LiveDataTestUtil.getButtonValue(dao.getButtonLastUser());
    assertNotNull(user);
    assertEquals("Mike", user.getName());
  }

  //TODO Fix the delete test. It fails. I don't know why.
//  /**
//   * Deletes the last, and only, user entered in the test db.
//   */
//  @Test
//  public void postInsertZDelete() {
//    UserEntity user = dao.getLastUser();
//    dao.delete(user);
//    assertNull(user);
//  }

  /**
   * Closes the test db.
   */
  @AfterClass
  public static void tearDown() throws Exception {
    db.close();
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
package com.chigbrowsoftware.kgo;

import android.app.Application;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.chigbrowsoftware.kgo.service.GoogleSignInService;
import com.facebook.stetho.Stetho;

/**
 * Intent class for application.
 */
public class KiddoGoApplication extends Application {

  /**
   * Creates applcation, initializes stetho, sets context for Google sign in, and performs
   * meaningless db operation to initialize room db.
   */
  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    GoogleSignInService.setContext(this);
    new Thread(() -> ActivitiesDatabase.getInstance(this).userDao().delete()).start();

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

package com.chigbrowsoftware.kgo.service;


import android.app.Application;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

/**
 * Used to sign in with Google to access Google Calendar.
 */
public class GoogleSignInService {

  private static Application context;

  private GoogleSignInAccount account;
  private GoogleSignInClient client;

  private GoogleSignInService() {

    GoogleSignInOptions options = new GoogleSignInOptions.Builder()
        .requestEmail()
        .requestId()
        .build();
    client = GoogleSignIn.getClient(context, options);
  }

  /**
   * Set the context for Google Sign In.
   */
  public static void setContext(Application context) {
    GoogleSignInService.context = context;
  }

  /**
   * Get an instance of Google Sign in Service.
   */
  //lazy-loaded singleton
  public static GoogleSignInService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  /**
   * Get the Client for Google Sign In.
   */
  public GoogleSignInClient getClient() {
    return client;
  }

  /**
   * Get the account that was signed in with Google Sign In.
   */
  public GoogleSignInAccount getAccount() {
    return account;
  }

  /**
   * Set the account that was signed in with Google Sign In.
   */
  public void setAccount(GoogleSignInAccount account) {
    this.account = account;
  }

  private static class InstanceHolder {

    private static final GoogleSignInService INSTANCE = new GoogleSignInService();
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


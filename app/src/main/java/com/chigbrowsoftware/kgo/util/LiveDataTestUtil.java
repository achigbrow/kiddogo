package com.chigbrowsoftware.kgo.util;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

/**
 * Provides the ability to test LiveData for the UserDaoTest.
 */
public class LiveDataTestUtil {

  /**
   * Assists to pull a LiveData UserEntity Entity for the UserDaoTest.
   */
  public static <T> T getButtonValue(final LiveData<T> liveData) throws InterruptedException {

    final Object[] data = new Object[1];
    final CountDownLatch latch = new CountDownLatch(1);
    final Observer<T> observer = new Observer<T>() {
      @Override
      public void onChanged(@Nullable T o) {
        data[0] = o;
        latch.countDown();
        liveData.removeObserver(this);
      }
    };
    liveData.observeForever(observer);
    latch.await(2, TimeUnit.SECONDS);

    return (T) data[0];

  }

  /**
   * Assists to pull a LiveData List of UserEntity for the UserDaoTest.
   */
  public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {

    final Object[] data = new Object[1];
    final CountDownLatch latch = new CountDownLatch(1);
    final Observer<T> observer = new Observer<T>() {
      @Override
      public void onChanged(@Nullable T o) {
        data[0] = o;
        latch.countDown();
        liveData.removeObserver(this);
      }
    };
    liveData.observeForever(observer);
    latch.await(2, TimeUnit.SECONDS);

    return (T) data[0];

  }
}

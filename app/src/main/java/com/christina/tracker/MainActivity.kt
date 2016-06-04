package com.christina.tracker

import com.christina.tracker.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import timber.log.Timber

/**
 * Created by christina on 2/7/16.
 */

public class MainActivity: Activity() {
  override fun onCreate(savedInstanceBundle: Bundle?) {
    super.onCreate(savedInstanceBundle)

    setContentView(R.layout.activity_sign_in)

    val signInButton = findViewById(R.id.signInButton)
    signInButton.setOnClickListener {
      Timber.i("logging in!")
      NavigationUtils.goToSwipeableActivity(this)
    }
  }
}

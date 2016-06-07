package com.christina.tracker.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import com.christina.tracker.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveId
import com.google.android.gms.drive.OpenFileActivityBuilder
import timber.log.Timber
import java.util.*
import rx.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by christina on 6/5/16.
 */

class ChooseSheetActivity: BaseFragmentActivity(), GoogleApiClient.OnConnectionFailedListener {
  var googleApiClient: GoogleApiClient? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_choose_spreadsheet)

    val button = this.findViewById(R.id.createSheetButton) as Button
    button.setOnClickListener {
      chooseSheet()
    }

    googleApiClient = GoogleApiClient.Builder(this)
        .enableAutoManage(this, this)
        .addApi(Drive.API)
        .addOnConnectionFailedListener(this)
        .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
          override fun onConnected(p0: Bundle?) {
            Timber.i("connected!")
          }

          override fun onConnectionSuspended(p0: Int) {
            Timber.i("suspended")
          }

        })
        .addScope(Scope("https://www.googleapis.com/auth/drive"))
        .build()
  }

  override fun onResume() {
    super.onResume()
    googleApiClient?.connect()
  }

  override fun onPause() {
    super.onPause()

    googleApiClient?.disconnect()
  }

  private fun chooseSheet() {
    getPicker()
  }

  private fun getPicker() {
    Timber.i("is connected ${googleApiClient?.isConnected}")
    val intentSender =  Drive.DriveApi.newOpenFileActivityBuilder()
        .setMimeType(arrayOf("application/vnd.google-apps.spreadsheet"))
        .build(googleApiClient)

    startIntentSenderForResult(intentSender, 4, null, 0, 0, 0)
  }


  override public fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...)
    if (requestCode == 4 && resultCode == RESULT_OK) {
      val driveId = data?.getParcelableExtra<DriveId>(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID)
      Timber.d("Selected file's id: $driveId")
    }
  }

  override fun onConnectionFailed(p0: ConnectionResult) {
    val result = p0.errorMessage
    Timber.e("connection error $result")
  }
}
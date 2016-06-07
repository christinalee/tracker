package com.christina.tracker.activity

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import com.christina.tracker.R

/**
 * Created by christina on 6/4/16.
 */

open class BaseActivity: AppCompatActivity() {
  private var progressDialog: ProgressDialog? = null

  fun showProgressDialog() {
    if (progressDialog == null) {
      progressDialog = ProgressDialog(this)
      progressDialog?.let {
        it.setMessage(getString(R.string.loading))
        it.setIndeterminate(true)
      }
    }

    progressDialog?.show()
  }

  fun hideProgressDialog() {
    progressDialog?.let {
      if (it.isShowing) {
        it.hide()
      }
    }
  }

  override public fun onDestroy() {
    super.onDestroy()
    hideProgressDialog()
  }

}
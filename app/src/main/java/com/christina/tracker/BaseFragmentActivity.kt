package com.christina.tracker

import android.app.ProgressDialog
import android.support.v4.app.FragmentActivity

/**
 * Created by christina on 6/4/16.
 */

open class BaseFragmentActivity: FragmentActivity() {

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
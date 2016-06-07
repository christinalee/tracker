package com.christina.tracker.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.christina.tracker.activity.BaseFragmentActivity
import com.christina.tracker.BuildConfig
import com.christina.tracker.NavigationUtils
import com.christina.tracker.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.drive.Drive
import com.google.firebase.auth.*
import timber.log.Timber

/**
 * Created by christina on 2/7/16.
 */

class MainActivity: BaseFragmentActivity(), GoogleApiClient.OnConnectionFailedListener {
  companion object {
    const val RC_SIGN_IN = 9001
  }

  var googleApiClient: GoogleApiClient? = null
  var auth: FirebaseAuth? = null
  var authListener: FirebaseAuth.AuthStateListener? = null

  override fun onCreate(savedInstanceBundle: Bundle?) {
    super.onCreate(savedInstanceBundle)

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }

    auth = FirebaseAuth.getInstance()

    //If already auth'd, go to main screen
    if (auth?.currentUser != null) {
      //todo: should also check that a sheet has been chosen
      auth?.currentUser?.let { NavigationUtils.goToChooseSheetActivity(this@MainActivity) }
      return
    }

    setContentView(R.layout.activity_sign_in)

    val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestScopes(Scope("https://www.googleapis.com/auth/spreadsheets"), Scope("https://www.googleapis.com/auth/drive"))
        .build()


    googleApiClient = GoogleApiClient.Builder(this)
        .enableAutoManage(this, this)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .addScope(Scope("https://www.googleapis.com/auth/drive"))
        .build()



    authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
      val user: FirebaseUser? = firebaseAuth.currentUser
      if (user != null) {
        // User is signed in
        Timber.d("onAuthStateChanged:signed_in:" + user.uid);
        NavigationUtils.goToChooseSheetActivity(this@MainActivity)
      } else {
        // User is signed out
        Timber.d("onAuthStateChanged:signed_out");
      }
    }

    val signInButton = findViewById(R.id.signInButton)
    signInButton.setOnClickListener {
      signIn()
    }
  }

  override fun onStart() {
    super.onStart()
    authListener?.let { auth?.addAuthStateListener(it) }
  }

  override fun onStop() {
    super.onStop()
    authListener?.let { auth?.removeAuthStateListener(it) }
  }

  override fun onConnectionFailed(result: ConnectionResult) {
    Timber.e("Connection failed $result")
  }

  private fun signIn() {
    val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
    startActivityForResult(signInIntent, RC_SIGN_IN)
  }

  override public fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    super.onActivityResult(requestCode, resultCode, data)

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...)
    if (requestCode == RC_SIGN_IN) {
      val result: GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
      handleSignInResult(result);
    }
  }

  private fun handleSignInResult(result: GoogleSignInResult) {
    Timber.d("handleSignInResult:" + result.isSuccess);
    if (result.isSuccess) {
      // Signed in successfully, show authenticated UI.
      val acct: GoogleSignInAccount? = result.signInAccount
      Timber.d("account display name is ${acct?.displayName}")
      acct?.let { firebaseAuthWithGoogle(it) }
    } else {
      // Signed out, show unauthenticated UI.
      Timber.d("result was not a success $result")
    }
  }

  private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
    Timber.d("firebaseAuthWithGoogle:" + acct.id)
    showProgressDialog();

    val credential: AuthCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
    auth?.let{
      it.signInWithCredential(credential)
          .addOnCompleteListener(this) { task ->
            Timber.d("signInWithCredential:onComplete:" + task.isSuccessful)

            // If sign in fails, display a message to the user. If sign in succeeds
            // the auth state listener will be notified and logic to handle the
            // signed in user can be handled in the listener.
            if (!task.isSuccessful) {
              Timber.w("signInWithCredential", task.exception)
              Toast.makeText(this@MainActivity, "Authentication failed.",
                  Toast.LENGTH_SHORT).show()
            }
            hideProgressDialog()
          }
    }

  }
}

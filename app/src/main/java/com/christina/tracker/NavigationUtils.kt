package com.christina.tracker

import android.content.Context
import android.content.Intent

/**
 * Created by christina on 6/3/16.
 */

public class NavigationUtils {
    companion object {
        public fun goToSwipeableActivity(context: Context) {
            val i = Intent(context, SwipeableActivity::class.java)
            context.startActivity(i)
        }
    }
}

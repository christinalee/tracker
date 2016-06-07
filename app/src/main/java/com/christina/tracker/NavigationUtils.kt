package com.christina.tracker

import android.content.Context
import android.content.Intent
import com.christina.tracker.activity.ChooseSheetActivity
import com.christina.tracker.activity.SwipeableActivity

/**
 * Created by christina on 6/3/16.
 */

public class NavigationUtils {
    companion object {
        public fun goToChooseSheetActivity(context: Context) {
            val i = Intent(context, ChooseSheetActivity::class.java)
            context.startActivity(i)
        }

        public fun goToSwipeableActivity(context: Context) {
            val i = Intent(context, SwipeableActivity::class.java)
            context.startActivity(i)
        }
    }
}

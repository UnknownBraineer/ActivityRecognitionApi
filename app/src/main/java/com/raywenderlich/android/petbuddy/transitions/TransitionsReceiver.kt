package com.raywenderlich.android.petbuddy.transitions

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.ActivityTransitionResult
import com.raywenderlich.android.petbuddy.BuildConfig
import com.raywenderlich.android.petbuddy.SupportedActivity

const val TRANSITIONS_RECEIVER_ACTION = "${BuildConfig.APPLICATION_ID}_transitions_receiver_action"
private const val TRANSITION_PENDING_INTENT_REQUEST_CODE = 200

class TransitionsReceiver: BroadcastReceiver() {

  var action: ((SupportedActivity) -> Unit)? = null

  companion object {

    fun getPendingIntent(context: Context): PendingIntent {
      val intent = Intent(TRANSITIONS_RECEIVER_ACTION)
      return PendingIntent.getBroadcast(context, TRANSITION_PENDING_INTENT_REQUEST_CODE, intent,
          PendingIntent.FLAG_UPDATE_CURRENT)
    }
  }

  override fun onReceive(context: Context, intent: Intent) {
    Toast.makeText(context, "Transitions intent $intent", Toast.LENGTH_SHORT).show()
    // 1
    if (ActivityTransitionResult.hasResult(intent)) {
      // 2
      val result = ActivityTransitionResult.extractResult(intent)
      result?.let { handleTransitionEvents(it.transitionEvents) }
    }

  }

  private fun handleTransitionEvents(transitionEvents: List<ActivityTransitionEvent>) {
    transitionEvents
      // 3
      .filter { it.transitionType == ActivityTransition.ACTIVITY_TRANSITION_ENTER }
      // 4
      .forEach { action?.invoke(SupportedActivity.fromActivityType(it.activityType)) }
  }
}

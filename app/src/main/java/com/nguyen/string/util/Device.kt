package com.nguyen.string.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics




object Device {


    fun getDeviceWidth(activity: Activity): Int{
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }
}
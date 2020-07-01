package com.nguyen.string

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.nguyen.string.util.SavedSharedPreferences

class MainApplication : Application() {

    companion object{
        var token: String? = null
        var context: Context? = null
    }


    override fun onCreate() {
        super.onCreate()
        generateMsgToken()
        context = this
        SavedSharedPreferences.init(this)
//        FacebookSdk.sdkInitialize(applicationContext);
//        AppEventsLogger.activateApp(this);
    }

    private fun generateMsgToken(){
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("Get token", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                token = task.result?.token
            })
    }







}
package com.nguyen.string.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nguyen.string.R
import com.nguyen.string.ui.LoggedActivity
import com.nguyen.string.util.SavedSharedPreferences

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(SavedSharedPreferences.isLogin){
            val intent = Intent(this, LoggedActivity::class.java)
            startActivity(intent)
        } else {
            setContentView(R.layout.activity_main)
        }




    }





}
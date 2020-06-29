package com.nguyen.string.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.nguyen.string.R
import kotlinx.android.synthetic.main.activity_logged.*


class LoggedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged)

        setUpNavigation()


    }


    private fun setUpNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.logged_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(bottom_nav, navHostFragment.navController)
    }

    fun hideBottomNav(){
        bottom_nav.visibility = View.GONE
    }

    fun showBottomNav(){
        bottom_nav.visibility = View.VISIBLE
    }
}
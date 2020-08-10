package com.nguyen.string.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nguyen.string.R
import com.nguyen.string.ui.add.AddPostActivity
import com.nguyen.string.util.AddPostItineraryMenu
import kotlinx.android.synthetic.main.activity_logged.*


class LoggedActivity : AppCompatActivity() {

    private var bottomNav: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged)

        setUpNavigation()
        bottomNav = bottom_nav

        bottomNav?.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.add_fragment -> createAndShowAddMenu(layoutInflater)
                R.id.profile_fragment -> findNavController(R.id.logged_fragment).navigate(R.id.profile_fragment)
                R.id.feed_fragment -> findNavController(R.id.logged_fragment).navigate(R.id.feed_fragment)
            }

            true
        }



    }

    private fun createAndShowAddMenu(layoutInflater: LayoutInflater){
        AddPostItineraryMenu.create(this, layoutInflater)
        AddPostItineraryMenu.show()
        AddPostItineraryMenu.onClickPost {
            startActivity(Intent(this, AddPostActivity::class.java))
        }

        AddPostItineraryMenu.onClickItinerary {

        }
    }


    private fun setUpNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.logged_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(bottom_nav, navHostFragment.navController)
    }

    fun hideBottomNav(){
        bottomNav?.visibility = View.GONE
    }

    fun showBottomNav(){
        bottomNav?.visibility = View.VISIBLE
    }
}
package com.nguyen.string.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nguyen.string.ui.main.profilePagerFragments.ItinerariesFragment
import com.nguyen.string.ui.main.profilePagerFragments.PostsFragment
import com.nguyen.string.ui.main.profilePagerFragments.SavesFragment

class ProfilePagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment){

    private val postsFragment = PostsFragment()
    private val itinerariesFragment = ItinerariesFragment()
    private val savesFragment = SavesFragment()

    companion object{
        private const val ARG_OBJECT = "object"
    }


    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> postsFragment
            1 -> itinerariesFragment
            2 -> savesFragment
            else -> postsFragment
        }
    }

}
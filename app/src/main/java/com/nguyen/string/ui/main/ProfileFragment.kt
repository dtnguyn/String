package com.nguyen.string.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.nguyen.string.databinding.FragmentProfileCollapsingBinding
import com.nguyen.string.di.Injection
import com.nguyen.string.ui.main.adapter.ProfilePagerAdapter
import com.nguyen.string.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile_collapsing.view.*


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profilePagerAdapter: ProfilePagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProfileCollapsingBinding.inflate(inflater, container, false)


        profileViewModel = profileViewModel()
        profileViewModel.userProfile.observe(viewLifecycleOwner, Observer {
            binding.user = it
        })

        profileViewModel.getUserProfile()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profilePagerAdapter = ProfilePagerAdapter(this)
        viewPager = view.profile_pager
        viewPager.adapter = profilePagerAdapter
        val tabLayout = view.tab_layout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position){
                0 -> tab.text = "POSTS"
                1 -> tab.text = "ITINERARIES"
                2 -> tab.text = "SAVES"
            }
        }.attach()
    }

    private fun profileViewModel(): ProfileViewModel {
        val factory = Injection.provideProfileViewModel()
        return ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }


}
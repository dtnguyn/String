package com.nguyen.string.ui.main

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.nguyen.string.R
import com.nguyen.string.data.Blog
import com.nguyen.string.data.Comment
import com.nguyen.string.databinding.FragmentCommentBinding
import com.nguyen.string.databinding.FragmentFeedBinding
import com.nguyen.string.databinding.FragmentProfileBinding
import com.nguyen.string.databinding.FragmentProfileCollapsingBinding
import com.nguyen.string.di.Injection
import com.nguyen.string.ui.main.adapter.CommentsAdapter
import com.nguyen.string.ui.main.adapter.FeedAdapter
import com.nguyen.string.ui.main.adapter.ProfilePagerAdapter
import com.nguyen.string.util.BottomMenuSettings
import com.nguyen.string.util.SavedSharedPreferences
import com.nguyen.string.viewmodel.FeedViewModel
import com.nguyen.string.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.view.*


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

    class ScrollListener(val loadMore : (recyclerView: RecyclerView) -> Unit) : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            loadMore(recyclerView)
        }
    }


}
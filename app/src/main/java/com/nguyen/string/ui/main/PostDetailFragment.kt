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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.nguyen.string.R
import com.nguyen.string.data.Blog
import com.nguyen.string.data.Comment
import com.nguyen.string.databinding.FragmentCommentBinding
import com.nguyen.string.databinding.FragmentFeedBinding
import com.nguyen.string.databinding.FragmentPostDetailBinding
import com.nguyen.string.databinding.FragmentProfileBinding
import com.nguyen.string.di.Injection
import com.nguyen.string.ui.main.adapter.CommentsAdapter
import com.nguyen.string.ui.main.adapter.FeedAdapter
import com.nguyen.string.ui.main.adapter.ProfilePagerAdapter
import com.nguyen.string.util.BottomMenuSettings
import com.nguyen.string.util.SavedSharedPreferences
import com.nguyen.string.viewmodel.FeedViewModel
import com.nguyen.string.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.view.*


class PostDetailFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPostDetailBinding.inflate(inflater, container, false)

        binding.actionBarPostDetail.backVisibility = true
        binding.actionBarPostDetail.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        profileViewModel = profileViewModel()

        profileViewModel.postDetail.observe(viewLifecycleOwner, Observer {
            when (it.photos?.size) {
                1 -> {
                    binding.type = 1
                    binding.post1.post = it
                }
                2 -> {
                    binding.type = 2
                    binding.post2.post = it
                }
                else -> {
                    binding.type = 3
                    binding.post3.post = it
                }
            }
        })

        val id = arguments?.getInt("id")
        id?.let {
            profileViewModel.getPostDetail(id)
        }


        return binding.root
    }


    private fun profileViewModel(): ProfileViewModel {
        val factory = Injection.provideProfileViewModel()
        return ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }




}
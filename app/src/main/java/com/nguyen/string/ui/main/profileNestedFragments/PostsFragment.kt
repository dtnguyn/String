package com.nguyen.string.ui.main.profileNestedFragments

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
import com.nguyen.string.R
import com.nguyen.string.data.Blog
import com.nguyen.string.data.Comment
import com.nguyen.string.databinding.FragmentCommentBinding
import com.nguyen.string.databinding.FragmentFeedBinding
import com.nguyen.string.databinding.FragmentPostsBinding
import com.nguyen.string.databinding.FragmentProfileBinding
import com.nguyen.string.di.Injection
import com.nguyen.string.ui.main.adapter.CommentsAdapter
import com.nguyen.string.ui.main.adapter.FeedAdapter
import com.nguyen.string.util.BottomMenuSettings
import com.nguyen.string.util.SavedSharedPreferences
import com.nguyen.string.viewmodel.FeedViewModel
import com.nguyen.string.viewmodel.ProfileViewModel


class PostsFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPostsBinding.inflate(inflater, container, false)

        profileViewModel = profileViewModel()

        profileViewModel.userPosts.observe(viewLifecycleOwner, Observer {

        })

        profileViewModel.getUserProfilePosts()

        return binding.root
    }

    private fun profileViewModel(): ProfileViewModel {
        val factory = Injection.provideProfileViewModel()
        return ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }


}
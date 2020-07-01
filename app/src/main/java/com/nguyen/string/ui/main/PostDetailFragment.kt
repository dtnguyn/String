package com.nguyen.string.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nguyen.string.databinding.FragmentPostDetailBinding
import com.nguyen.string.di.Injection
import com.nguyen.string.viewmodel.ProfileViewModel


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
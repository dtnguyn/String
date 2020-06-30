package com.nguyen.string.ui.main.profilePagerFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.string.R
import com.nguyen.string.data.Blog
import com.nguyen.string.databinding.FragmentItinerariesBinding
import com.nguyen.string.di.Injection
import com.nguyen.string.ui.main.adapter.ProfileItineraryAdapter
import com.nguyen.string.viewmodel.ProfileViewModel
import java.util.*


class ItinerariesFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentItinerariesBinding.inflate(inflater, container, false)

        profileViewModel = profileViewModel()

        val recyclerView = binding.itinerariesRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        var adapter : ProfileItineraryAdapter? = null

        profileViewModel.userItineraries.observe(viewLifecycleOwner, Observer {
            adapter = ProfileItineraryAdapter(it as ArrayList<Blog>)
            recyclerView.adapter = adapter
            if(it.isEmpty()) {
                binding.isEmpty = true
                binding.noItineraries.errorBackground.setImageResource(R.drawable.empty_post_icon)
                binding.noItineraries.errorTitle.text = "No Itineraries"
                binding.noItineraries.errorDescription.text = "Use itineraries to add and organise places or interest for upcoming travel plans or curation purposes"
            } else binding.isEmpty = false
        })

        profileViewModel.moreUserItineraries.observe(viewLifecycleOwner, Observer {
            adapter?.addItineraries(it)
        })

        recyclerView.addOnScrollListener(ScrollListener(fun(recyclerView: RecyclerView) {
            if (!recyclerView.canScrollVertically(1)) {
                profileViewModel.getMoreUserProfileItineraries()
            }
        }))

        profileViewModel.getUserProfileItineraries()

        return binding.root
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
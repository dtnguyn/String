package com.nguyen.string.ui.main.profilePagerFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.string.R
import com.nguyen.string.data.Blog
import com.nguyen.string.databinding.FragmentPostsBinding
import com.nguyen.string.di.Injection
import com.nguyen.string.ui.main.CommentFragment
import com.nguyen.string.ui.main.adapter.ProfilePostAdapter
import com.nguyen.string.util.Device
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

        val recyclerView = binding.postRecyclerview
        val width = Device.getDeviceWidth(requireActivity())
        var adapter: ProfilePostAdapter? = null

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        profileViewModel.userPosts.observe(viewLifecycleOwner, Observer {
            adapter = ProfilePostAdapter(this, it as ArrayList<Blog>, width/3 - 5)
            if(it.isEmpty()) {
                binding.isEmpty = true
                binding.noPost.errorBackground.setImageResource(R.drawable.empty_post_icon)
                binding.noPost.errorTitle.text = "NO POSTS"
                binding.noPost.errorDescription.text = "Post photos or videos to share your trip experiences"
            } else binding.isEmpty = false
            recyclerView.adapter = adapter
        })

        profileViewModel.moreUserPosts.observe(viewLifecycleOwner, Observer {
            adapter?.addPosts(it)
        })

        recyclerView.addOnScrollListener(ScrollListener(fun(recyclerView: RecyclerView) {
            if (!recyclerView.canScrollVertically(1)) {
                profileViewModel.getMoreUserProfilePosts()
            }
        }))


        profileViewModel.getUserProfilePosts()

        return binding.root
    }

    private fun profileViewModel(): ProfileViewModel {
        val factory = Injection.provideProfileViewModel()
        return ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }

    fun moveToPostDetail(postId: Int){
        val bundle = bundleOf("id" to postId)
        findNavController().navigate(R.id.post_detail_fragment, bundle)
    }

    class ScrollListener(val loadMore : (recyclerView: RecyclerView) -> Unit) : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            loadMore(recyclerView)
        }
    }


}
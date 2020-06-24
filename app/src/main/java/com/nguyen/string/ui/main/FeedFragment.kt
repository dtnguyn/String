package com.nguyen.string.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.string.data.Blog
import com.nguyen.string.databinding.FragmentFeedBinding
import com.nguyen.string.di.Injection
import com.nguyen.string.ui.main.adapter.FeedAdapter
import com.nguyen.string.viewmodel.FeedViewModel

class FeedFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        binding.actionBarMain.backVisibility = false
        binding.actionBarMain.nextVisibility = false
        binding.actionBarMain.actionBarTitle = "Feed"

        feedViewModel = feedViewModel()


        val recyclerView = binding.feedRecyclerview
        var feedAdapter: FeedAdapter? = null
        recyclerView.layoutManager = LinearLayoutManager(context)


        feedViewModel.blogList.observe(viewLifecycleOwner, Observer {
            feedAdapter = FeedAdapter(it as ArrayList<Blog>, requireContext())
            recyclerView.adapter = feedAdapter
        })

        feedViewModel.moreBlogList.observe(viewLifecycleOwner, Observer {
            feedAdapter?.addBlog(it)
        })


        feedViewModel.getFeed()

        recyclerView.addOnScrollListener(ScrollListener(fun(recyclerView : RecyclerView){
            if (!recyclerView.canScrollVertically(1)) {
                feedViewModel.loadMoreFeed()
            }
        }))



        return binding.root
    }


    private fun feedViewModel(): FeedViewModel {
        val factory = Injection.provideFeedViewModel()
        return ViewModelProvider(this, factory)[FeedViewModel::class.java]
    }

    class ScrollListener(val loadMore : (recyclerView: RecyclerView) -> Unit) : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            loadMore(recyclerView)
        }
    }

}
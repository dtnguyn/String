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
import com.nguyen.string.data.Blog
import com.nguyen.string.data.Comment
import com.nguyen.string.databinding.FragmentCommentBinding
import com.nguyen.string.databinding.FragmentFeedBinding
import com.nguyen.string.di.Injection
import com.nguyen.string.ui.main.adapter.CommentsAdapter
import com.nguyen.string.ui.main.adapter.FeedAdapter
import com.nguyen.string.util.BottomMenuSettings
import com.nguyen.string.viewmodel.FeedViewModel


class CommentFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCommentBinding.inflate(inflater, container, false)
        binding.actionBarComment.actionBarTitle = "Comments"
        binding.actionBarComment.backVisibility = true
        binding.actionBarComment.nextVisibility = false

        feedViewModel = feedViewModel()

        val id = arguments?.getInt("id")
        id?.let {
            feedViewModel.getComments(it)
        }

        val recyclerView = binding.commentsRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        var adapter: CommentsAdapter? = null

        feedViewModel.commentList.observe(viewLifecycleOwner, Observer {
            adapter = CommentsAdapter(it as ArrayList<Comment>)
            recyclerView.adapter = adapter
        })

        feedViewModel.moreCommentList.observe(viewLifecycleOwner, Observer {
            adapter?.addBlog(it)
        })

        recyclerView.addOnScrollListener(ScrollListener(fun(recyclerView: RecyclerView) {
            if (!recyclerView.canScrollVertically(1)) {
                id?.let {
                    feedViewModel.getMoreComments(id)
                }
            }
        }))

        binding.postCommentButton.setOnClickListener {
            val comment = binding.editText.text.toString()
            if(comment.isBlank()){
                Toast.makeText(requireContext(), "You have to type something first", Toast.LENGTH_SHORT).show()
            } else {
                feedViewModel.addComment(id!!, comment, fun(result: Boolean){
                    if(result) {
                        binding.editText.text.clear()
                        feedViewModel.getComments(id)
                    }
                })

            }
        }

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
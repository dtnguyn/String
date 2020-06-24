package com.nguyen.string.ui.main.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.nguyen.string.R
import com.nguyen.string.data.Blog
import com.nguyen.string.databinding.*


class FeedAdapter(private val blogs: ArrayList<Blog>, private val context: Context): RecyclerView.Adapter<FeedAdapter.BaseViewHolder>() {

    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract fun bind(blog: Blog, context: Context)
    }

    class PostViewHolder(private val binding: PostItemBinding) : BaseViewHolder(binding.root) {
        private val videoPlayer = binding.videoPlayerPost

        override fun bind(post: Blog, context: Context) {
            binding.post = post
            post.videos?.url?.let {
                initializePlayer(it, context)
            }


        }

        private fun initializePlayer(url: String, context: Context){
            val player : SimpleExoPlayer = SimpleExoPlayer.Builder(context).build()
            videoPlayer.player = player


            // Produces DataSource instances through which media data is loaded.
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, "string")
            )
            // This is the MediaSource representing the media to be played.
            // This is the MediaSource representing the media to be played.
            val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(url))
            // Prepare the player with the source.
            // Prepare the player with the source.
            player.prepare(videoSource)
        }

    }

    class Post2ViewHolder(private val binding: Post2ItemBinding) : BaseViewHolder(binding.root) {

        override fun bind(post: Blog, context: Context) {
            binding.post = post
        }

    }

    class Post3ViewHolder(private val binding: Post3ItemBinding) : BaseViewHolder(binding.root) {

        override fun bind(post: Blog, context: Context) {
            binding.post = post
        }

    }

    class PoiViewHolder(private val binding: PoiItemBinding) : BaseViewHolder(binding.root){
        override fun bind(poi: Blog, context: Context) {
            binding.poi = poi
        }

    }

    class ItineraryViewHolder(private val binding: ItinaryItemBinding) : BaseViewHolder(binding.root){
        override fun bind(itinerary: Blog, context: Context) {
            binding.itinerary = itinerary
        }

    }

    override fun getItemViewType(position: Int): Int {
        var layout = R.layout.post_item
        when(blogs[position].type){
            "post" -> {
                if(blogs[position].photos == null || blogs[position].photos?.size == 1){
                    Log.d("Debugging", "${blogs[position].photos?.size} items")
                    layout = R.layout.post_item
                }

                else layout = if(blogs[position].photos?.size == 2){
                    Log.d("Debugging", "2 items")
                    R.layout.post_2_item

                }

                else R.layout.post_3_item
            }
            "poi" -> layout =  R.layout.poi_item
            "itinerary" -> layout = R.layout.itinary_item
        }
        return layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        var holder : BaseViewHolder? = null

        when(viewType){
            R.layout.post_item -> holder = PostViewHolder(
                PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            R.layout.post_2_item -> {
                holder = Post2ViewHolder(
                    Post2ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            R.layout.post_3_item -> holder = Post3ViewHolder(
                Post3ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            R.layout.poi_item -> holder = PoiViewHolder(
                PoiItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            R.layout.itinary_item -> holder = ItineraryViewHolder(
                ItinaryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
        return holder!!
    }

    override fun getItemCount(): Int {
        Log.d("Feed", "size ${blogs.size}")
        return blogs.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(blogs[position], context)
    }

    fun addBlog(moreBlogList: List<Blog>){
        moreBlogList.forEach {
            blogs.add(it)
            notifyItemInserted(blogs.size - 1)
        }
    }
}
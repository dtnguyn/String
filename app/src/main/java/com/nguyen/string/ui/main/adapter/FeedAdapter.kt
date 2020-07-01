package com.nguyen.string.ui.main.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.nguyen.string.util.BottomMenuSettings
import com.nguyen.string.viewmodel.FeedViewModel


class FeedAdapter(private val blogs: ArrayList<Blog>,
                  private val context: Context,
                  private val viewModel: FeedViewModel,
                  private val moveToCommentsFragment : (id: Int) -> Unit): RecyclerView.Adapter<FeedAdapter.BaseViewHolder>() {

    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract fun bind(blog: Blog, context: Context, viewModel: FeedViewModel, moveToCommentsFragment: (id: Int) -> Unit, notifyItemChange: () -> Unit)
    }

    class PostViewHolder(private val binding: PostItemBinding) : BaseViewHolder(binding.root) {
        private val videoPlayer = binding.videoPlayerPost
        private val likeButton = binding.likeButtonPost
        private val saveButton = binding.saveButton
        private val saveCounter = binding.saveCounterPost
        private val menuButton = binding.menuButton
        private val commentButton = binding.commentButtonPost

        override fun bind(post: Blog, context: Context, viewModel: FeedViewModel, moveToCommentsFragment: (id: Int) -> Unit, notifyItemChange: () -> Unit) {
            binding.post = post

            post.videos?.url?.let {
                initializePlayer(it, context)
            }

            updateLikeButton(post.isLiked)
            updateSaveButton(post.isSaved)

            likeButton.setOnClickListener {
                updateLikeButton(post.isLiked?.not())
                viewModel.like(post.id!!)
                post.isLiked = post.isLiked?.not()

                if(post.isLiked!!) post.likeCounter = post.likeCounter?.plus(1)
                else post.likeCounter = post.likeCounter?.minus(1)

                binding.likeCounterPost.text = post.likeCounter.toString()
            }

            saveButton.setOnClickListener {
                updateSaveButton(post.isSaved?.not())
                viewModel.save(post.id!!)
                post.isSaved = post.isSaved?.not()

                if(post.isSaved!!) post.saveCounter = post.saveCounter?.plus(1)
                else post.saveCounter = post.saveCounter?.minus(1)

                saveCounter.text = post.saveCounter.toString()
            }

            menuButton.setOnClickListener {
                BottomMenuSettings.show(post.websiteUrl)
            }

            commentButton.setOnClickListener {
                moveToCommentsFragment.invoke(post.id!!)
            }

        }

        private fun updateLikeButton(status: Boolean?){
            status?.let {
                if(!it){
                    likeButton.setImageResource(R.drawable.heart_icon)
                } else {
                    likeButton.setImageResource(R.drawable.red_heart_icon)
                }
            }
        }

        private fun updateSaveButton(status: Boolean?){
            status?.let{
                if(!it){
                    saveButton.setCardBackgroundColor(Color.parseColor("#ffffff"))
                    saveCounter.setTextColor(Color.parseColor("#8853E8"))
                } else {
                    saveButton.setCardBackgroundColor(Color.parseColor("#8853E8"))
                    saveCounter.setTextColor(Color.parseColor("#ffffff"))
                }
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

        private val likeButton = binding.likeButtonPost2

        private val saveButton = binding.saveButton
        private val saveCounter = binding.saveCounterPost2

        private val menuButton = binding.menuButton


        override fun bind(post: Blog, context: Context, viewModel: FeedViewModel, moveToCommentsFragment: (id: Int) -> Unit, notifyItemChange: () -> Unit) {
            binding.post = post

            updateLikeButton(post.isLiked)
            updateSaveButton(post.isSaved)

            likeButton.setOnClickListener {
                updateLikeButton(post.isLiked?.not())
                moveToCommentsFragment(post.id!!)

                post.isLiked = post.isLiked?.not()

                if(post.isLiked!!) post.likeCounter = post.likeCounter?.plus(1)
                else post.likeCounter = post.likeCounter?.minus(1)

                binding.likeCounterPost.text = post.likeCounter.toString()
            }

            saveButton.setOnClickListener {
                updateSaveButton(post.isSaved?.not())
                viewModel.save(post.id!!)
                post.isSaved = post.isSaved?.not()

                if(post.isSaved!!) post.saveCounter = post.saveCounter?.plus(1)
                else post.saveCounter = post.saveCounter?.minus(1)

                saveCounter.text = post.saveCounter.toString()
            }

            menuButton.setOnClickListener {
                BottomMenuSettings.show(post.websiteUrl)
            }
        }

        private fun updateLikeButton(status: Boolean?){
            status?.let {
                if(!it){
                    likeButton.setImageResource(R.drawable.heart_icon)
                } else {
                    likeButton.setImageResource(R.drawable.red_heart_icon)
                }
            }
        }

        private fun updateSaveButton(status: Boolean?){
            status?.let{
                if(!it){
                    saveButton.setCardBackgroundColor(Color.parseColor("#ffffff"))
                    saveCounter.setTextColor(Color.parseColor("#8853E8"))
                } else {
                    saveButton.setCardBackgroundColor(Color.parseColor("#8853E8"))
                    saveCounter.setTextColor(Color.parseColor("#ffffff"))
                }
            }
        }
    }

    class Post3ViewHolder(private val binding: Post3ItemBinding) : BaseViewHolder(binding.root) {

        private val likeButton = binding.likeButtonPost3

        private val saveButton = binding.saveButton
        private val saveCounter = binding.saveCounterPost3

        private val menuButton = binding.menuButton

        override fun bind(post: Blog, context: Context, viewModel: FeedViewModel, moveToCommentsFragment: (id: Int) -> Unit, notifyItemChange: () -> Unit) {
            binding.post = post


            updateLikeButton(post.isLiked)
            updateSaveButton(post.isSaved)

            likeButton.setOnClickListener {
                updateLikeButton(post.isLiked?.not())
                moveToCommentsFragment(post.id!!)

                post.isLiked = post.isLiked?.not()

                if(post.isLiked!!) post.likeCounter = post.likeCounter?.plus(1)
                else post.likeCounter = post.likeCounter?.minus(1)

                binding.likeCounterPost.text = post.likeCounter.toString()
            }

            saveButton.setOnClickListener {
                updateSaveButton(post.isSaved?.not())
                viewModel.save(post.id!!)
                post.isSaved = post.isSaved?.not()

                if(post.isSaved!!) post.saveCounter = post.saveCounter?.plus(1)
                else post.saveCounter = post.saveCounter?.minus(1)

                saveCounter.text = post.saveCounter.toString()

            }

            menuButton.setOnClickListener {
                BottomMenuSettings.show(post.websiteUrl)
            }
        }

        private fun updateLikeButton(status: Boolean?){
            status?.let {
                if(!it){
                    likeButton.setImageResource(R.drawable.heart_icon)
                } else {
                    likeButton.setImageResource(R.drawable.red_heart_icon)
                }
            }
        }

        private fun updateSaveButton(status: Boolean?){
            status?.let{
                if(!it){
                    saveButton.setCardBackgroundColor(Color.parseColor("#ffffff"))
                    saveCounter.setTextColor(Color.parseColor("#8853E8"))
                } else {
                    saveButton.setCardBackgroundColor(Color.parseColor("#8853E8"))
                    saveCounter.setTextColor(Color.parseColor("#ffffff"))
                }
            }
        }

    }

    class PoiViewHolder(private val binding: PoiItemBinding) : BaseViewHolder(binding.root){

        private val likeButton = binding.likeButtonPoi

        private val menuButton = binding.menuButton

        override fun bind(poi: Blog, context: Context, viewModel: FeedViewModel, moveToCommentsFragment: (id: Int) -> Unit, notifyItemChange: () -> Unit) {
            binding.poi = poi

            updateLikeButton(poi.isLiked)

            likeButton.setOnClickListener {
                updateLikeButton(poi.isLiked?.not())
                moveToCommentsFragment(poi.id!!)

                poi.isLiked = poi.isLiked?.not()

                if(poi.isLiked!!) poi.likeCounter = poi.likeCounter?.plus(1)
                else poi.likeCounter = poi.likeCounter?.minus(1)

                binding.likeCounterPoi.text = poi.likeCounter.toString()
            }

            menuButton.setOnClickListener {
                BottomMenuSettings.show(poi.websiteUrl)
            }

        }

        private fun updateLikeButton(status: Boolean?){
            status?.let {
                if(!it){
                    likeButton.setImageResource(R.drawable.heart_icon)
                } else {
                    likeButton.setImageResource(R.drawable.red_heart_icon)
                }
            }
        }
    }

    class ItineraryViewHolder(private val binding: ItinaryItemBinding) : BaseViewHolder(binding.root){

        private val likeButton = binding.likeButtonItinerary
        private val horizontalRecyclerView = binding.itineraryItemRecyclerview

        private val menuButton = binding.menuButton

        override fun bind(itinerary: Blog, context: Context, viewModel: FeedViewModel, moveToCommentsFragment: (id: Int) -> Unit, notifyItemChange: () -> Unit) {
            binding.itinerary = itinerary

            itinerary.itineraries?.let {
                horizontalRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
                horizontalRecyclerView.adapter = ItineraryAdapter(it)
            }


            updateLikeButton(itinerary.isLiked)

            likeButton.setOnClickListener {
                updateLikeButton(itinerary.isLiked?.not())
                moveToCommentsFragment(itinerary.id!!)

                itinerary.isLiked = itinerary.isLiked?.not()



                if(itinerary.isLiked!!) itinerary.likeCounter = itinerary.likeCounter?.plus(1)
                else itinerary.likeCounter = itinerary.likeCounter?.minus(1)

                binding.likeCounterItinerary.text = itinerary.likeCounter.toString()
            }

            menuButton.setOnClickListener {
                BottomMenuSettings.show(itinerary.websiteUrl)
            }
        }

        private fun updateLikeButton(status: Boolean?){
            status?.let {
                if(!it){
                    likeButton.setImageResource(R.drawable.heart_icon)
                } else {
                    likeButton.setImageResource(R.drawable.red_heart_icon)
                }
            }
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
        holder.bind(blogs[position], context, viewModel, moveToCommentsFragment, fun(){
            notifyItemChanged(position)
        })
    }

    fun addBlog(moreBlogList: List<Blog>){
        moreBlogList.forEach {
            blogs.add(it)
            notifyDataSetChanged()
        }
    }


}
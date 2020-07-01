package com.nguyen.string.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.string.data.Blog
import com.nguyen.string.databinding.ProfilePostItemBinding
import com.nguyen.string.ui.main.profilePagerFragments.PostsFragment

class ProfilePostAdapter(private val fragment: PostsFragment, private val posts: ArrayList<Blog>, private val postLength: Int): RecyclerView.Adapter<ProfilePostAdapter.BaseViewHolder>() {

    abstract class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(fragment: PostsFragment, post: Blog, postLength: Int)
    }

    class PostViewHolder(private val binding: ProfilePostItemBinding) : BaseViewHolder(binding.root){
        override fun bind(fragment: PostsFragment, post: Blog, postLength: Int) {
            binding.post = post
            binding.executePendingBindings()
            post.photos?.let {
                binding.isMultiple = it.size > 1
            }

            binding.profilePostContainer.layoutParams = LinearLayout.LayoutParams(postLength, postLength)

            binding.profilePostContainer.setOnClickListener{
                post.id?.let { it1 -> fragment.moveToPostDetail(it1) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return PostViewHolder(ProfilePostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(fragment, posts[position], postLength)
    }

    fun addPosts(_posts: List<Blog>?) {
        _posts?.let{ _posts ->
            if(_posts.isEmpty()) return
            _posts.forEach {
                posts.add(it)
                notifyItemInserted(posts.size - 1)
            }
        }
    }
}
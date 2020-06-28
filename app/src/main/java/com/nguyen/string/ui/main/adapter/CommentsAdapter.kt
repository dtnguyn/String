package com.nguyen.string.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.string.data.Comment
import com.nguyen.string.databinding.CommentItemBinding

class CommentsAdapter(private val comments: ArrayList<Comment>) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    class CommentViewHolder(private val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(comment: Comment){
            binding.comment = comment
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    fun addBlog(_comments: List<Comment>?) {
        if(_comments.isNullOrEmpty()) return
        _comments.forEach {
            comments.add(it)
            notifyItemInserted(comments.size - 1)
        }
    }

    fun addComment(_comment: Comment){
        comments.add(0, _comment)
        notifyItemInserted(0)
    }
}
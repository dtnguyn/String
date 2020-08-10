package com.nguyen.string.ui.add.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nguyen.string.databinding.PreviewItemBinding
import java.util.ArrayList

class PreviewAdapter(private val previews: ArrayList<String>, private val context: Context) : RecyclerView.Adapter<PreviewAdapter.PreviewViewHolder>() {

    class PreviewViewHolder(private val binding : PreviewItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(_preview: Uri, context: Context){
            Glide.with(context)
                .load(_preview)
                .into(binding.previewImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        return PreviewViewHolder(PreviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return previews.size
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.bind(Uri.parse(previews[position]), context)
    }
}
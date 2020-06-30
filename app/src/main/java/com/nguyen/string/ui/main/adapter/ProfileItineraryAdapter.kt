package com.nguyen.string.ui.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.string.data.Blog
import com.nguyen.string.databinding.ProfileItineraryItemBinding

class ProfileItineraryAdapter(private val itineraries: ArrayList<Blog>) : RecyclerView.Adapter<ProfileItineraryAdapter.BaseViewHolder>() {


    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract fun bind(itinerary: Blog)
    }

    class ItineraryViewHolder(private val binding: ProfileItineraryItemBinding): BaseViewHolder(binding.root){
        override fun bind(itinerary: Blog) {
            binding.itinerary = itinerary
            Log.d("Profile", "${itinerary.title}")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ItineraryViewHolder(ProfileItineraryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return itineraries.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(itineraries[position])
    }

    fun addItineraries(_itineraries: List<Blog>?) {
        _itineraries?.let{_itineraries ->
            _itineraries.forEach {
                itineraries.add(it)
                notifyItemInserted(itineraries.size - 1)
            }
        }
    }

}
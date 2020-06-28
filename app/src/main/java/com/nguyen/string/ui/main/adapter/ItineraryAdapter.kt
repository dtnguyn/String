package com.nguyen.string.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.string.data.Itinerary
import com.nguyen.string.databinding.ItineraryPlaceItemBinding

class ItineraryAdapter(private val itineraries: List<Itinerary>): RecyclerView.Adapter<ItineraryAdapter.ItineraryViewHolder>() {


    class ItineraryViewHolder(private val binding: ItineraryPlaceItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(itinerary: Itinerary){
            binding.itinerary = itinerary
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItineraryViewHolder {
        return ItineraryViewHolder(ItineraryPlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return itineraries.size
    }

    override fun onBindViewHolder(holder: ItineraryViewHolder, position: Int) {
        holder.bind(itineraries[position])
    }
}
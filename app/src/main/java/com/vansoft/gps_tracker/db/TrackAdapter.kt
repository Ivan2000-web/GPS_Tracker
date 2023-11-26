package com.vansoft.gps_tracker.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vansoft.gps_tracker.R
import com.vansoft.gps_tracker.databinding.TrackItemBinding
class TrackAdapter : ListAdapter<TrackItem, TrackAdapter.Holder>(Comparator()) {
    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = TrackItemBinding.bind(view)
        fun bind(track: TrackItem) = with(binding) {
            val speed = "${track.averageSpeed} km/h"
            val time = "${track.time} s"
            val distance = "${track.distance} m"
            tvDate.text = track.date
            tvSpeed.text = speed
            tvTime.text = time
            tvDistance.text = distance
        }
    }

    //Comparator определяет: нужно ли перерисовывать элемент в списке или нет
    class Comparator : DiffUtil.ItemCallback<TrackItem>() {
        override fun areItemsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }


}
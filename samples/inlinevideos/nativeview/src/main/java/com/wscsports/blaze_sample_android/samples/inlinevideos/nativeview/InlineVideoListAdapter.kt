package com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wscsports.blaze_sample_android.core.ui.databinding.SampleListItemBinding

/**
 * Adapter for displaying inline video implementation options in a RecyclerView.
 * Each item represents a different approach to implementing inline videos.
 */
class InlineVideoListAdapter(
    private val onItemClick: (InlineVideoScreenType) -> Unit
) : ListAdapter<InlineVideoScreenType, InlineVideoListAdapter.InlineVideoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InlineVideoViewHolder {
        val binding = SampleListItemBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return InlineVideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InlineVideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class InlineVideoViewHolder(
        private val binding: SampleListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(screenType: InlineVideoScreenType) {
            with(binding) {
                // Set the title and description
                textViewTitle.text = screenType.title
                textViewSubtitle.text = screenType.description
                
                // Set a generic icon (you can customize this per type)
                imageSampleIcon.setImageResource(com.wscsports.blaze_sample_android.core.ui.R.drawable.ic_play)
                
                // Set click listener
                containerView.setOnClickListener { 
                    onItemClick(screenType) 
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<InlineVideoScreenType>() {
            override fun areItemsTheSame(oldItem: InlineVideoScreenType, newItem: InlineVideoScreenType): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: InlineVideoScreenType, newItem: InlineVideoScreenType): Boolean {
                return oldItem == newItem
            }
        }
    }
}

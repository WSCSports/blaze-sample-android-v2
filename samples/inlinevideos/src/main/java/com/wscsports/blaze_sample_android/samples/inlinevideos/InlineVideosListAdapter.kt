package com.wscsports.blaze_sample_android.samples.inlinevideos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wscsports.blaze_sample_android.core.ui.databinding.SampleListItemBinding

/**
 * Adapter for displaying inline videos implementation options in a RecyclerView.
 * Each item represents a different approach to implementing inline videos (Native vs Compose).
 */
class InlineVideosListAdapter(
    private val onItemClick: (InlineVideosType) -> Unit
) : ListAdapter<InlineVideosType, InlineVideosListAdapter.InlineVideosViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InlineVideosViewHolder {
        val binding = SampleListItemBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return InlineVideosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InlineVideosViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class InlineVideosViewHolder(
        private val binding: SampleListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(inlineVideosType: InlineVideosType) {
            with(binding) {
                // Set the title and description
                textViewTitle.text = inlineVideosType.title
                textViewSubtitle.text = inlineVideosType.description
                
                // Set the icon
                imageSampleIcon.setImageResource(inlineVideosType.imageResourceId)
                
                // Set click listener
                containerView.setOnClickListener { 
                    onItemClick(inlineVideosType) 
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<InlineVideosType>() {
            override fun areItemsTheSame(oldItem: InlineVideosType, newItem: InlineVideosType): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: InlineVideosType, newItem: InlineVideosType): Boolean {
                return oldItem == newItem
            }
        }
    }
}

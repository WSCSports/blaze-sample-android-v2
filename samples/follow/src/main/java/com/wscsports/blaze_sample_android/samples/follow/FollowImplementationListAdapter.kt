package com.wscsports.blaze_sample_android.samples.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wscsports.blaze_sample_android.core.ui.databinding.SampleListItemBinding

/**
 * Adapter for displaying the Follow Entities implementation options
 * (Native View vs Compose) on the sample's entry screen.
 */
class FollowImplementationListAdapter(
    private val onItemClick: (FollowImplementationType) -> Unit
) : ListAdapter<FollowImplementationType, FollowImplementationListAdapter.FollowImplementationViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowImplementationViewHolder {
        val binding = SampleListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FollowImplementationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowImplementationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FollowImplementationViewHolder(
        private val binding: SampleListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(implementationType: FollowImplementationType) {
            with(binding) {
                textViewTitle.text = implementationType.title
                textViewSubtitle.text = implementationType.description
                imageSampleIcon.setImageResource(implementationType.imageResourceId)
                containerView.setOnClickListener {
                    onItemClick(implementationType)
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<FollowImplementationType>() {
            override fun areItemsTheSame(oldItem: FollowImplementationType, newItem: FollowImplementationType): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FollowImplementationType, newItem: FollowImplementationType): Boolean {
                return oldItem == newItem
            }
        }
    }
}

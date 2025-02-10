package com.wscsports.blaze_sample_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wscsports.blaze_sample_android.databinding.SampleListItemBinding

class SampleListAdapter(
    private val onItemClicked: (SampleItem) -> Unit
) : ListAdapter<SampleItem, SampleListAdapter.SampleItemViewHolder>(SampleItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleItemViewHolder {
        val binding = SampleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SampleItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SampleItemViewHolder, position: Int) {
        val widget = getItem(position)
        holder.bind(widget)
    }

    inner class SampleItemViewHolder(
        private val binding: SampleListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currItem: SampleItem? = null

        init {
            binding.containerView.setOnClickListener {
                currItem?.let { onItemClicked.invoke(it) }
            }
        }

        fun bind(item: SampleItem) {
            currItem = item
            with(binding) {
                textViewTitle.text = item.title
                textViewSubtitle.text = item.subTitle
                imageSampleIcon.setImageResource(item.imageResourceId)
            }
        }
    }
}

class SampleItemComparator : DiffUtil.ItemCallback<SampleItem>() {
    override fun areItemsTheSame(oldItem: SampleItem, newItem: SampleItem): Boolean {
        // Replace with your own logic
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SampleItem, newItem: SampleItem): Boolean {
        // Replace with your own logic
        return oldItem.name == newItem.name
    }
}
package com.wscsports.blaze_sample_android.samples.widgets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wscsports.blaze_sample_android.samples.widgets.databinding.WidgetListItemBinding

class WidgetListAdapter(
    private val onItemClicked: (WidgetScreenType) -> Unit
) : ListAdapter<WidgetScreenType, WidgetListAdapter.WidgetItemViewHolder>(WidgetsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetItemViewHolder {
        val binding = WidgetListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WidgetItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WidgetItemViewHolder, position: Int) {
        val widget = getItem(position)
        holder.bind(widget)
    }

    inner class WidgetItemViewHolder(
        private val binding: WidgetListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var widgetItem: WidgetScreenType? = null

        init {
            binding.containerView.setOnClickListener {
                widgetItem?.let { onItemClicked.invoke(it) }
            }
        }

        fun bind(widget: WidgetScreenType?) {
            this.widgetItem = widget
            with(binding) {
                textViewName.text = widget?.title
                textViewDescription.text = widget?.description
            }
        }
    }
}

class WidgetsComparator : DiffUtil.ItemCallback<WidgetScreenType>() {
    override fun areItemsTheSame(oldItem: WidgetScreenType, newItem: WidgetScreenType): Boolean {
        // Replace with your own logic
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WidgetScreenType, newItem: WidgetScreenType): Boolean {
        // Replace with your own logic
        return oldItem.name == newItem.name
    }
}
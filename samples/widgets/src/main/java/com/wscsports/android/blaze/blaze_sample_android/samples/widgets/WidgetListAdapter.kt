package com.wscsports.android.blaze.blaze_sample_android.samples.widgets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wscsports.android.blaze.blaze_sample_android.samples.widgets.databinding.WidgetListItemBinding

class WidgetListAdapter(
    private val onItemClicked: (WidgetType) -> Unit
) : ListAdapter<WidgetType, WidgetListAdapter.WidgetItemViewHolder>(WidgetsComparator()) {

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

        private var widgetItem: WidgetType? = null

        init {
            binding.containerView.setOnClickListener {
                widgetItem?.let { onItemClicked.invoke(it) }
            }
        }

        fun bind(widget: WidgetType?) {
            this.widgetItem = widget
            with(binding) {
                textViewName.text = widget?.title
                textViewDescription.text = widget?.description
            }
        }
    }
}

class WidgetsComparator : DiffUtil.ItemCallback<WidgetType>() {
    override fun areItemsTheSame(oldItem: WidgetType, newItem: WidgetType): Boolean {
        // Replace with your own logic
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WidgetType, newItem: WidgetType): Boolean {
        // Replace with your own logic
        return oldItem.name == newItem.name
    }
}
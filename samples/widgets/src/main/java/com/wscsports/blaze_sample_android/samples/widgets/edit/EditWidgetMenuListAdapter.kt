package com.wscsports.blaze_sample_android.samples.widgets.edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wscsports.blaze_sample_android.samples.widgets.databinding.EditWidgetMenuListItemBinding
import com.wscsports.blaze_sample_android.samples.widgets.edit.EditWidgetMenuListAdapter.EditWidgetMenuItemViewHolder

/**
 * [EditWidgetMenuListAdapter] is a RecyclerView adapter that displays a list of widget's edit menu.
 */
class EditWidgetMenuListAdapter(
    private val onItemClicked: (EditMenuItem) -> Unit
) : ListAdapter<EditMenuItem, EditWidgetMenuItemViewHolder>(EditWidgetMenuItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditWidgetMenuItemViewHolder {
        val binding = EditWidgetMenuListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EditWidgetMenuItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditWidgetMenuItemViewHolder, position: Int) {
        val widget = getItem(position)
        holder.bind(widget)
    }

    inner class EditWidgetMenuItemViewHolder(
        private val binding: EditWidgetMenuListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var menuItem: EditMenuItem? = null

        init {
            binding.root.setOnClickListener {
                menuItem?.let { onItemClicked.invoke(it) }
            }
        }

        fun bind(menuItem: EditMenuItem?) {
            this.menuItem = menuItem
            binding.apply {
                textViewTitle.text = menuItem?.title
                textViewDescription.text = menuItem?.description
            }
        }
    }
}

class EditWidgetMenuItemComparator : DiffUtil.ItemCallback<EditMenuItem>() {
    override fun areItemsTheSame(oldItem: EditMenuItem, newItem: EditMenuItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: EditMenuItem, newItem: EditMenuItem): Boolean {
        return oldItem.name == newItem.name
    }
}
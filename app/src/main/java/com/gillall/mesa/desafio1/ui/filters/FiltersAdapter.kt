package com.gillall.mesa.desafio1.ui.filters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillall.mesa.desafio1.databinding.FeedNewsViewBinding
import com.gillall.mesa.desafio1.databinding.FiltersNewsViewBinding
import com.gillall.mesa.desafio1.mesa.NewsData

class FiltersAdapter(private val clickListener: FilterListener):
        ListAdapter<NewsData, FiltersAdapter.ViewHolder>(NewsDiffCallback()) {

    class ViewHolder private constructor(private val binding: FiltersNewsViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NewsData, clickListener: FilterListener) {
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FiltersNewsViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }
}

class NewsDiffCallback : DiffUtil.ItemCallback<NewsData>() {
    override fun areItemsTheSame(oldItem: NewsData, newItem: NewsData): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: NewsData, newItem: NewsData): Boolean {
        return oldItem == newItem
    }
}

class FilterListener(val clickListener: (news: NewsData, view: View) -> Unit) {
    fun onClick(news: NewsData, view: View) = clickListener(news, view)
}

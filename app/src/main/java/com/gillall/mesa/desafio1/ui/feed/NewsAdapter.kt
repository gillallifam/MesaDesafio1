package com.gillall.mesa.desafio1.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gillall.mesa.desafio1.databinding.FeedNewsViewBinding
import com.gillall.mesa.desafio1.mesa.NewsData

class NewsAdapter(private val clickListener: NewsListener):
        ListAdapter<NewsData, NewsAdapter.ViewHolder>(NewsDiffCallback()) {

    class ViewHolder private constructor(private val binding: FeedNewsViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NewsData, clickListener: NewsListener) {
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FeedNewsViewBinding.inflate(layoutInflater, parent, false)
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

class NewsListener(val clickListener: (news: NewsData, view: View) -> Unit) {
    fun onClick(news: NewsData, view: View) = clickListener(news, view)
}

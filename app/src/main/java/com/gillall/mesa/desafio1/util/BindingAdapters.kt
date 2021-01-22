package com.gillall.mesa.desafio1.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gillall.mesa.desafio1.R
import com.gillall.mesa.desafio1.mesa.NewsData

/**
 * Binding adapter used to display images from URL using Glide
 */
@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String) {
    Glide.with(imageView.context).load(url)
        .apply(RequestOptions().override(763, 400))
        .into(imageView)
}

@BindingAdapter("isFavorite")
fun ImageView.setFavorite(item: NewsData) {
    setImageResource(when (item.favorite) {
        true -> R.drawable.ic_star_foreground
        false -> R.drawable.ic_star_faded_foreground
        else -> R.drawable.ic_star_faded_foreground
    })
}
package com.gillall.mesa.desafio1.mesa

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class NewsData(
    val title: String,
    val description: String,
    val content: String,
    val author: String,
    val published_at: String,
    val highlight: Boolean,
    val url: String,
    val image_url: String,
    var favorite: Boolean?
):Parcelable{
    fun tst(){}
}
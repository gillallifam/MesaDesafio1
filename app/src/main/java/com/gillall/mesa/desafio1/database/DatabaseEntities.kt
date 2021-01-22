package com.gillall.mesa.desafio1.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseNews constructor(
    @PrimaryKey val title: String,
    val description: String,
    val content: String,
    val author: String,
    val published_at: String,
    val highlight: Boolean,
    val url: String,
    val image_url: String,
    val favorite: Boolean?
)

@Entity
data class DatabaseNewsUpdate constructor(
    @PrimaryKey val title: String,
    val description: String,
    val content: String,
    val author: String,
    val published_at: String,
    val highlight: Boolean,
    val url: String,
    val image_url: String
)
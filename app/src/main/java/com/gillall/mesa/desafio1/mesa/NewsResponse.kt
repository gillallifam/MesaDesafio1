package com.gillall.mesa.desafio1.mesa

import com.gillall.mesa.desafio1.mesa.NewsData
import com.gillall.mesa.desafio1.mesa.NewsPagination

data class NewsResponse(val pagination: NewsPagination, val data: List<NewsData>)

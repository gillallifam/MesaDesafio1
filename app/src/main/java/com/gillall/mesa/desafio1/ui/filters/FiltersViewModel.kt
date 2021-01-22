package com.gillall.mesa.desafio1.ui.filters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gillall.mesa.desafio1.repository.NewsRepository

class FiltersViewModel : ViewModel() {
    val newsRepository = NewsRepository.getInstance(null,null)
    val newsAndHighlights = newsRepository.newsAndHighlights
    init {
        println("init")
    }
}
package com.gillall.mesa.desafio1.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.gillall.mesa.desafio1.mesa.NewsData
import com.gillall.mesa.desafio1.repository.NewsRepository
import org.br.gillall.mesa1.work.RefreshDataWorker.Companion.token

class DetailsViewModel : ViewModel() {

    private val newsRepository = NewsRepository.getInstance(null, token)

    init {

    }

    fun fav(news: NewsData) {
        viewModelScope.launch {
            println("favoriting")
            newsRepository.updateSingle(news)
        }
    }
}
package com.gillall.mesa.desafio1.ui.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.gillall.mesa.desafio1.database.getDatabase
import com.gillall.mesa.desafio1.mesa.HighlightsResponse
import com.gillall.mesa.desafio1.mesa.NewsData
import com.gillall.mesa.desafio1.mesa.NewsResponse
import com.gillall.mesa.desafio1.repository.NewsRepository
import java.lang.Exception

class FeedViewModel(application: Application, token: String) : AndroidViewModel(application) {

    var newsData = MutableLiveData<NewsResponse>()
    var highlightsData = MutableLiveData<HighlightsResponse>()

    private val db = getDatabase(getApplication<Application>().applicationContext)
    private val newsRepository = NewsRepository.getInstance(db, token)
    val newsAndHighlights = newsRepository.newsAndHighlights

    init {
        viewModelScope.launch {
            try {
                if (newsRepository.newsCount() == 0) {
                    newsRepository.fillNews()
                    newsRepository.fillHighlights()
                } else {
                    newsRepository.updateNews()
                    newsRepository.updateHighlights()
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun highlightClicked(news: NewsData) {
        viewModelScope.launch {
            newsRepository.updateSingle(news)
        }
    }

    fun newsClicked(newsId: String) {
        println(newsId)
    }
}
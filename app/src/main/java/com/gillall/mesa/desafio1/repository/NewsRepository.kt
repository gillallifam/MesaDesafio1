package com.gillall.mesa.desafio1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.gillall.mesa.desafio1.database.DatabaseNews
import com.gillall.mesa.desafio1.database.DatabaseNewsUpdate
import com.gillall.mesa.desafio1.database.NewsDatabase
import com.gillall.mesa.desafio1.mesa.MesaApi
import com.gillall.mesa.desafio1.mesa.NewsData

/**
 * Repository for fetching news from the network and storing them on disk
 * Use dependency injection in the future
 */
class NewsRepository(private val database: NewsDatabase?, private val token: String?) {//remove db, only repository connect to db
    private val typeJSON: String = "application/json"

    val newsAndHighlights: LiveData<List<NewsData>> =
        Transformations.map(database!!.newsDao.getAllNews()) {
            return@map toModelObject(it)
        }

    init {
        println(newsAndHighlights)
    }

    suspend fun newsCount(): Int {
        return withContext(Dispatchers.IO) {
            return@withContext database!!.newsDao.getCount()
        }
    }

    suspend fun fillNews() {
        withContext(Dispatchers.IO) {
            val newsList = MesaApi.service.getNews(token!!, typeJSON).data
            database!!.newsDao.insertAll(toModelCreate(newsList))
        }
    }

    suspend fun fillHighlights() {
        withContext(Dispatchers.IO) {
            val newsList = MesaApi.service.getHighlights(token!!, typeJSON).data
            database!!.newsDao.insertAll(toModelCreate(newsList))
        }
    }

    suspend fun updateNews() {
        withContext(Dispatchers.IO) {
            val newsList = MesaApi.service.getNews(token!!, typeJSON).data
            database!!.newsDao.updateAll(toModelUpdate(newsList))
        }
    }

    suspend fun updateHighlights() {
        withContext(Dispatchers.IO) {
            val newsList = MesaApi.service.getHighlights(token!!, typeJSON).data
            database!!.newsDao.updateAll(toModelUpdate(newsList))
        }
    }

    suspend fun updateSingle(news: NewsData) {
        withContext(Dispatchers.IO) {
            val toDB = DatabaseNews(
                news.title,
                news.description,
                news.content,
                news.author,
                news.published_at,
                news.highlight,
                news.url,
                news.image_url,
                !news.favorite!!
            )
            database!!.newsDao.updateSingle(toDB)
        }
    }

    private fun toModelObject(list: List<DatabaseNews>): List<NewsData> {
        return list.map {
            NewsData(
                title = it.title,
                description = it.description,
                content = it.content,
                author = it.author,
                published_at = it.published_at,
                highlight = it.highlight,
                url = it.url,
                image_url = it.image_url,
                favorite = it.favorite
            )
        }
    }

    private fun toModelCreate(list: List<NewsData>): List<DatabaseNews> {
        return list.map {
            DatabaseNews(
                title = it.title,
                description = it.description,
                content = it.content,
                author = it.author,
                published_at = it.published_at,
                highlight = it.highlight,
                url = it.url,
                image_url = it.image_url,
                favorite = false
            )
        }
    }

    private fun toModelUpdate(list: List<NewsData>): List<DatabaseNewsUpdate> {
        return list.map {
            DatabaseNewsUpdate(
                title = it.title,
                description = it.description,
                content = it.content,
                author = it.author,
                published_at = it.published_at,
                highlight = it.highlight,
                url = it.url,
                image_url = it.image_url
            )
        }
    }

    companion object {

        @Volatile private var INSTANCE: NewsRepository? = null

        fun getInstance(database: NewsDatabase?, token1: String?): NewsRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NewsRepository(database, token1).also { INSTANCE = it }
            }
        }

    }

}

package com.gillall.mesa.desafio1.mesa

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

enum class NewsFilter(val value: String) {
    SHOW_FAV("fav"),
    SHOW_TITLE("title"),
    SHOW_ALL("all")
}

private const val BASE_URL = "https://mesa-news-api.herokuapp.com/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MesaApiService {

    @GET("client/news")
    suspend fun getNews(
        @Header("Authorization") auth: String,
        @Header("Content-Type") type: String
    ): NewsResponse

    @GET("client/news/highlights")
    suspend fun getHighlights(
        @Header("Authorization") auth: String,
        @Header("Content-Type") type: String
    ): HighlightsResponse

    @Headers("Content-Type: application/json")
    @POST("client/auth/signin")
    suspend fun signIn(@Body body: UserSignIn): Token

    @Headers("Content-Type: application/json")
    @POST("client/auth/signup")
    suspend fun signUp(@Body user: UserSignUp): Token
}

object MesaApi {
    val service: MesaApiService by lazy {
        retrofit.create(MesaApiService::class.java)
    }
}

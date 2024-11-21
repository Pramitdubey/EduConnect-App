package com.example.educonnect.retrofit

import com.example.educonnect.dataclass.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getTechNews(
        @Query("category") category: String = "technology",
        @Query("apiKey") apiKey: String,
        @Query("country") country: String = "us"
    ): Response<NewsResponse>

}
package com.newsletter.app.network

import com.newsletter.app.network.responses.ArticlesApiResponse
import com.newsletter.app.network.responses.SourcesApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/v2/sources")
    fun callGetSourcesApi(): Single<SourcesApiResponse>

    @GET("/v2/everything")
    fun callGetArticlesApiByPublisher(@Query("sources") sources: String): Single<ArticlesApiResponse>

    @GET("/v2/top-headlines")
    fun callGetArticlesApiByCountry(@Query("country") country: String): Single<ArticlesApiResponse>

    @GET("/v2/top-headlines")
    fun callGetArticlesApiByCategory(@Query("category") category: String): Single<ArticlesApiResponse>
    
}

package com.newsletter.app.repositories.remote

import com.newsletter.app.network.responses.ArticlesApiResponse
import com.newsletter.app.network.responses.SourcesApiResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Query

interface NewsApiRepository {

    fun callGetSourcesApi(): Single<SourcesApiResponse>

    fun callGetArticlesApiByPublisher(@Query("sources") sources: String): Single<ArticlesApiResponse>

    fun callGetArticlesApiByCountry(@Query("country") country: String): Single<ArticlesApiResponse>

    fun callGetArticlesApiByCategory(@Query("category") category: String): Single<ArticlesApiResponse>
}
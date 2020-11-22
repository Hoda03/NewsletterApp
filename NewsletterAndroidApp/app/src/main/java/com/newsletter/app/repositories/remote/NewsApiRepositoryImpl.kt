package com.newsletter.app.repositories.remote

import com.newsletter.app.network.ServiceBuilder
import com.newsletter.app.network.responses.ArticlesApiResponse
import com.newsletter.app.network.responses.SourcesApiResponse
import io.reactivex.Single

class NewsApiRepositoryImpl private constructor() : NewsApiRepository {

    companion object {
        private var instance: NewsApiRepositoryImpl? = null

        fun initialize() {
            if (instance == null) {
                instance = NewsApiRepositoryImpl()
            }
        }

        fun get(): NewsApiRepositoryImpl {
            return instance
                ?: throw IllegalStateException("RemoteRepositoryImpl must be initialized first.")
        }
    }

    override fun callGetSourcesApi(): Single<SourcesApiResponse> {
        return ServiceBuilder.get().callGetSourcesApi()
    }

    override fun callGetArticlesApiByPublisher(sources: String): Single<ArticlesApiResponse> {
        return ServiceBuilder.get().callGetArticlesApiByPublisher(sources)
    }

    override fun callGetArticlesApiByCountry(country: String): Single<ArticlesApiResponse> {
        return ServiceBuilder.get().callGetArticlesApiByCountry(country)
    }

    override fun callGetArticlesApiByCategory(category: String): Single<ArticlesApiResponse> {
        return ServiceBuilder.get().callGetArticlesApiByCategory(category)
    }

}
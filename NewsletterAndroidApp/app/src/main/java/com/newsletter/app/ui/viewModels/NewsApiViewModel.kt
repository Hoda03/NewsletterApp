package com.newsletter.app.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newsletter.app.dataModels.ArticleModel
import com.newsletter.app.network.enums.Endpoint
import com.newsletter.app.network.responses.ArticlesApiResponse
import com.newsletter.app.network.responses.GenericResponse
import com.newsletter.app.network.responses.SourcesApiResponse
import com.newsletter.app.repositories.local.LocalRepositoryDao
import com.newsletter.app.repositories.local.LocalRepositoryImpl
import com.newsletter.app.repositories.remote.NewsApiRepository
import com.newsletter.app.repositories.remote.NewsApiRepositoryImpl
import com.newsletter.app.ui.common.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class NewsApiViewModel : ViewModel() {

    private val localRepository: LocalRepositoryDao = LocalRepositoryImpl.get()
    private val remoteRepo: NewsApiRepository = NewsApiRepositoryImpl.get()
    private val apiResponse: MutableLiveData<GenericResponse> = MutableLiveData()
    private val disposable = CompositeDisposable()

    fun apiResponse(): LiveData<GenericResponse> {
        return apiResponse
    }

    fun getAllNewsSourcesFromLocalDatabase() = localRepository.getAllNewsSources()

    fun getFavoritesArticles() = localRepository.getFavoritesArticles(true)

    fun getArticlesFromLocalDatabase(sourceId: String) = localRepository.getAllArticles(sourceId)

    fun getSingleArticle(url: String, publishedAt: String, sourceId: String) =
        localRepository.getSingleArticle(url, publishedAt, sourceId)

    fun updateArticleFavoriteStatus(
        status: Boolean,
        url: String,
        publishedAt: String,
        sourceId: String
    ) {
        localRepository.updateArticleFavoriteStatus(status, url, publishedAt, sourceId)
    }

    fun getAllNewsSourcesFromRemoteDatabase() {
        val endpoint = Endpoint.SOURCES
        apiResponse.postValue(GenericResponse.loading(endpoint))
        disposable.add(
            remoteRepo.callGetSourcesApi().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SourcesApiResponse>() {
                    override fun onSuccess(response: SourcesApiResponse) {
                        if (response.status.trim().equals("ok", true)) {
                            response.sources?.let {
                                localRepository.addNewsSources(it)
                            }
                        }
                        apiResponse.value = GenericResponse.success(response, endpoint)
                    }

                    override fun onError(e: Throwable) {
                        apiResponse.value = GenericResponse.error(e, endpoint)
                    }
                })
        )
    }

    fun getArticlesByPublisher(sources: String) {
        val endpoint = Endpoint.TOP_HEADLINES
        apiResponse.postValue(GenericResponse.loading(endpoint))
        disposable.add(
            remoteRepo.callGetArticlesApiByPublisher(sources).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<ArticlesApiResponse>() {
                    override fun onSuccess(response: ArticlesApiResponse) {
                        val listToSave = ArrayList<ArticleModel>()
                        if (response.status.trim().equals("ok", true)) {
                            response.articles?.let { list ->
                                list.forEach {
                                    it.sourceId = sources

                                    val previousModel = localRepository.isArticleAlreadyPresent(
                                        it.url,
                                        it.publishedAt,
                                        it.sourceId
                                    )

                                    Logger.info("incoming --- $it")
                                    Logger.info("previousModel --- $previousModel")
                                    Logger.info("-----------------------------------")

                                    if (previousModel == null) {
                                        listToSave.add(it)
                                    }

                                }
                                localRepository.addArticles(listToSave)

                            }
                        }
                        apiResponse.postValue(GenericResponse.success(response, endpoint))
                    }

                    override fun onError(e: Throwable) {
                        apiResponse.postValue(GenericResponse.error(e, endpoint))
                    }
                })
        )
    }

    fun getArticlesByCountry(country: String) {
        val endpoint = Endpoint.EVERYTHING
        apiResponse.postValue(GenericResponse.loading(endpoint))
        disposable.add(
            remoteRepo.callGetArticlesApiByCountry(country).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<ArticlesApiResponse>() {
                    override fun onSuccess(response: ArticlesApiResponse) {
                        val listToSave = ArrayList<ArticleModel>()
                        if (response.status.trim().equals("ok", true)) {
                            response.articles?.let { list ->
                                list.forEach {
                                    it.sourceId = country

                                    val previousModel = localRepository.isArticleAlreadyPresent(
                                        it.url,
                                        it.publishedAt,
                                        it.sourceId
                                    )

                                    if (previousModel == null) {
                                        listToSave.add(it)
                                    }

                                }
                                localRepository.addArticles(listToSave)

                            }
                        }
                        apiResponse.postValue(GenericResponse.success(response, endpoint))
                    }

                    override fun onError(e: Throwable) {
                        apiResponse.postValue(GenericResponse.error(e, endpoint))
                    }
                })
        )
    }

    fun getArticlesByCategory(category: String) {
        val endpoint = Endpoint.TOP_HEADLINES
        apiResponse.postValue(GenericResponse.loading(endpoint))
        disposable.add(
            remoteRepo.callGetArticlesApiByCategory(category).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<ArticlesApiResponse>() {
                    override fun onSuccess(response: ArticlesApiResponse) {
                        val listToSave = ArrayList<ArticleModel>()
                        if (response.status.trim().equals("ok", true)) {
                            response.articles?.let { list ->
                                list.forEach {
                                    it.sourceId = category

                                    val previousModel = localRepository.isArticleAlreadyPresent(
                                        it.url,
                                        it.publishedAt,
                                        it.sourceId
                                    )

                                    if (previousModel == null) {
                                        listToSave.add(it)
                                    }

                                }
                                localRepository.addArticles(listToSave)

                            }
                        }
                        apiResponse.postValue(GenericResponse.success(response, endpoint))
                    }

                    override fun onError(e: Throwable) {
                        apiResponse.postValue(GenericResponse.error(e, endpoint))
                    }
                })
        )
    }

    override fun onCleared() {
        disposable.clear()
        
    }

}

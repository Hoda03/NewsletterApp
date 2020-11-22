package com.newsletter.app.repositories.local

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.newsletter.app.dataModels.ArticleModel
import com.newsletter.app.dataModels.SourcesModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LocalRepositoryImpl private constructor() : LocalRepositoryDao {

    companion object {

        private var INSTANCE: LocalRepositoryImpl? = null
        private lateinit var database: LocalDatabase
        private lateinit var executor: ExecutorService
        private lateinit var localRepositoryDao: LocalRepositoryDao

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = LocalRepositoryImpl()
                database = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    DatabaseConstants.databaseName
                ).fallbackToDestructiveMigration().build()
                localRepositoryDao = database.getDao()
                executor = Executors.newSingleThreadExecutor()
            }
        }

        fun get(): LocalRepositoryImpl {
            return INSTANCE
                ?: throw IllegalStateException("LocalRepositoryImpl must be initialized first.")
        }
    }

    override fun addNewsSources(models: List<SourcesModel>) {
        executor.execute {
            localRepositoryDao.deleteAllNewsSources()
            localRepositoryDao.addNewsSources(models)
        }
    }

    override fun getAllNewsSources(): LiveData<List<SourcesModel>> {
        return localRepositoryDao.getAllNewsSources()
    }

    override fun deleteAllNewsSources() {
        executor.execute {
            localRepositoryDao.deleteAllNewsSources()
        }
    }

    override fun addArticles(models: List<ArticleModel>) {
        executor.execute {
            localRepositoryDao.addArticles(models)
        }
    }

    override fun getAllArticles(sourceId: String): LiveData<List<ArticleModel>> {
        return localRepositoryDao.getAllArticles(sourceId)
    }

    override fun getFavoritesArticles(isFavorite: Boolean): LiveData<List<ArticleModel>> {
        return localRepositoryDao.getFavoritesArticles(isFavorite)
    }

    override fun deleteAllArticles() {
        executor.execute {
            localRepositoryDao.deleteAllArticles()
        }
    }

    override fun isArticleAlreadyPresent(
        url: String,
        publishedAt: String,
        sourceId: String
    ): ArticleModel? {
        return localRepositoryDao.isArticleAlreadyPresent(url, publishedAt, sourceId)
    }

    override fun getSingleArticle(
        url: String,
        publishedAt: String,
        sourceId: String
    ): LiveData<ArticleModel?> {
        return localRepositoryDao.getSingleArticle(url, publishedAt, sourceId)
    }

    override fun updateArticleFavoriteStatus(
        isFavorite: Boolean,
        url: String,
        publishedAt: String,
        sourceId: String
    ) {
        executor.execute {
            localRepositoryDao.updateArticleFavoriteStatus(isFavorite, url, publishedAt, sourceId)
        }
    }

}
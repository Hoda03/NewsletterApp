package com.newsletter.app.repositories.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.newsletter.app.dataModels.ArticleModel
import com.newsletter.app.dataModels.SourcesModel

@Dao
interface LocalRepositoryDao {

    /********* News Sources *********/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewsSources(models: List<SourcesModel>)

    @Query("Select * from ${DatabaseConstants.sourcesTable}")
    fun getAllNewsSources(): LiveData<List<SourcesModel>>

    @Query("Delete from " + DatabaseConstants.sourcesTable)
    fun deleteAllNewsSources()

    /********* Articles *********/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addArticles(models: List<ArticleModel>)

    @Query("Select * from ${DatabaseConstants.articlesTable} where sourceId = :sourceId")
    fun getAllArticles(sourceId: String): LiveData<List<ArticleModel>>

    @Query("Select * from ${DatabaseConstants.articlesTable} where isFavorite = :isFavorite ")
    fun getFavoritesArticles(isFavorite: Boolean): LiveData<List<ArticleModel>>

    @Query("Delete from " + DatabaseConstants.articlesTable)
    fun deleteAllArticles()

    @Query("Select * from ${DatabaseConstants.articlesTable} where url == :url and publishedAt = :publishedAt and sourceId = :sourceId")
    fun isArticleAlreadyPresent(url: String, publishedAt: String, sourceId: String): ArticleModel?

    @Query("Select * from ${DatabaseConstants.articlesTable} where url == :url and publishedAt = :publishedAt and sourceId = :sourceId")
    fun getSingleArticle(
        url: String,
        publishedAt: String,
        sourceId: String
    ): LiveData<ArticleModel?>

    @Query("update " + DatabaseConstants.articlesTable + " set isFavorite = :isFavorite where url == :url and publishedAt = :publishedAt and sourceId = :sourceId")
    fun updateArticleFavoriteStatus(
        isFavorite: Boolean,
        url: String,
        publishedAt: String,
        sourceId: String
    )
}

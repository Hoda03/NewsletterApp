package com.newsletter.app.dataModels

import androidx.room.Entity
import com.newsletter.app.repositories.local.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.articlesTable,
    primaryKeys = ["url", "publishedAt", "sourceId"]
)
data class ArticleModel(
    val url: String,
    val author: String? = "",
    val title: String? = "",
    val description: String? = "",
    val urlToImage: String? = "",
    val publishedAt: String = "",
    val content: String? = "",
    val isFavorite: Boolean = false,
    var sourceId: String = ""
    
)

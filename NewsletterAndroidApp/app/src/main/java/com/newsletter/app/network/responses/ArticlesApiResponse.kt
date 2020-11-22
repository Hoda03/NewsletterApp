package com.newsletter.app.network.responses

import com.newsletter.app.dataModels.ArticleModel

data class ArticlesApiResponse(
    val status: String,
    val message: String?,
    val articles: List<ArticleModel>?
)
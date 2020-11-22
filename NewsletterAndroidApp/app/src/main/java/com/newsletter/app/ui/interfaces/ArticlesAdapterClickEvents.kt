package com.newsletter.app.ui.interfaces

import com.newsletter.app.dataModels.ArticleModel

interface ArticlesAdapterClickEvents {
    fun onItemViewClick(model: ArticleModel)
    fun onFavoriteClick(model: ArticleModel)
}
package com.newsletter.app.network.responses

import com.newsletter.app.dataModels.SourcesModel

data class SourcesApiResponse(
    val status: String,
    val message: String?,
    val sources: List<SourcesModel>?
    
)

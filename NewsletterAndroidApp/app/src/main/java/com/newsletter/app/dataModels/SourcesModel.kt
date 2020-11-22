package com.newsletter.app.dataModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.newsletter.app.repositories.local.DatabaseConstants

@Entity(tableName = DatabaseConstants.sourcesTable)
data class SourcesModel(
    @PrimaryKey val id: String,
    val name: String? = "",
    val description: String? = "",
)
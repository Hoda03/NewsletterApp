package com.newsletter.app.repositories.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.newsletter.app.dataModels.ArticleModel
import com.newsletter.app.dataModels.SourcesModel

@Database(entities = [SourcesModel::class,ArticleModel::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getDao(): LocalRepositoryDao
}
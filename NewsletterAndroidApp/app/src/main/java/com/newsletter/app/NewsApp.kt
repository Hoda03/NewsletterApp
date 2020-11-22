package com.newsletter.app

import android.app.Application
import com.newsletter.app.network.ServiceBuilder
import com.newsletter.app.repositories.local.LocalRepositoryImpl
import com.newsletter.app.repositories.remote.NewsApiRepositoryImpl

class NewsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        ServiceBuilder.initialize(this)
        LocalRepositoryImpl.initialize(this)
        NewsApiRepositoryImpl.initialize()
    }

}

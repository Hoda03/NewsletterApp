package com.newsletter.app.network

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceBuilder {

    companion object {
        private var instance: ServiceBuilder? = null
        private var retrofit: ApiService? = null
        private var context: Context? = null
        private const val baseURL = "https://newsapi.org"

        fun initialize(context: Context) {
            if (instance == null) {
                instance = ServiceBuilder()
                Companion.context = context
                instance?.initRetro()
            }
        }

        fun get(): ApiService {
            return retrofit
                ?: throw IllegalStateException("ServiceBuilder must be initialized first.")
        }

    }

    private fun initRetro() {
        retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(getOkHttpClient())
            .build()
            .create(ApiService::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("apikey", "ec49c0f590c34f1ba82e766e36a11dfe")
                    .build()
                val requestBuilder = original.newBuilder()
                    .url(url)
                val request = requestBuilder.build()
                chain.proceed(request)
            }.build()
    }

}
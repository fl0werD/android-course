package com.example.fl0wer.androidApp.di

import com.example.fl0wer.BuildConfig
import com.example.fl0wer.androidApp.data.core.network.GoogleApi
import com.example.fl0wer.androidApp.data.core.network.RequestInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @[Provides Singleton]
    fun provideGson(): Gson = GsonBuilder()
        .create()

    @[Provides Singleton]
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(RequestInterceptor())
        .build()

    @[Provides Singleton]
    fun provideGeocodeApi(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): GoogleApi = Retrofit.Builder()
        .baseUrl(BuildConfig.GOOGLE_API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(GoogleApi::class.java)
}

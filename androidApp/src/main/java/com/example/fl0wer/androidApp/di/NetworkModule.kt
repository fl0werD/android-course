package com.example.fl0wer.androidApp.di

import com.example.fl0wer.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {
    @[Provides Singleton]
    fun provideGson(): Gson = GsonBuilder()
        .create()

    @[Provides Singleton]
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .build()

    /*@[Provides Singleton]
    fun provideApi(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): MapsApi = Retrofit.Builder()
        .baseUrl(BuildConfig.GOOGLE_GEOCODING_URI)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(MapsApi::class.java)*/
}

package com.example.elson.popmovies.dagger.app

import com.example.elson.popmovies.network.gson.getNetworkGson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
open class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(buildOkHttpClient())
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create(getNetworkGson()))
            .build()
    }

    internal open fun buildOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .build()
}

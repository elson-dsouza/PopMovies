package com.example.elson.popmovies.dagger.app

import android.content.Context
import com.example.elson.popmovies.data.SecurePrefs
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
    fun providePrefs(context: Context) = SecurePrefs(context)

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .client(buildOkHttpClient())
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    internal open fun buildOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .build()
}
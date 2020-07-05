package com.example.elson.popmovies.dagger.app

import android.content.Context
import com.example.elson.popmovies.BuildConfig
import com.example.elson.popmovies.data.SecurePrefs
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun providePrefs(context: Context) = SecurePrefs(context)

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val okHttpClient= if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                    .addNetworkInterceptor(StethoInterceptor())
                    .build()
        } else {
            OkHttpClient.Builder()
                    .build()
        }
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}
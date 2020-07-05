package com.example.elson.popmovies.dagger.app

import android.content.Context
import com.example.elson.popmovies.data.SecurePrefs
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PrefsModule {

    @Singleton
    @Provides
    fun providePrefs(context: Context) = SecurePrefs(context)
}
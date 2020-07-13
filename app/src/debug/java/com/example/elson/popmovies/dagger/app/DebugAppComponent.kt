package com.example.elson.popmovies.dagger.app

import com.example.elson.popmovies.MoviesApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    BindingModule::class,
    DebugAppModule::class,
    ActivityModule::class
])
interface DebugAppComponent: AppComponent {

    @Component.Factory
    interface Factory: AndroidInjector.Factory<MoviesApplication> {
        override fun create(@BindsInstance instance: MoviesApplication): AppComponent
    }
}
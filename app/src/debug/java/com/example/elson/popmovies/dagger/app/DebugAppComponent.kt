package com.example.elson.popmovies.dagger.app

import com.example.elson.popmovies.MoviesApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    BindingModule::class,
    DebugAppModule::class
])
interface DebugAppComponent: AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MoviesApplication): Builder
        fun build(): AppComponent
    }
}
package com.example.elson.popmovies.dagger.app

import com.example.elson.popmovies.MoviesApplication
import com.example.elson.popmovies.dagger.authentication.AuthenticationSubComponent
import com.example.elson.popmovies.ui.splash.SplashActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    BindingModule::class,
    AppModule::class
])
interface AppComponent: AndroidInjector<MoviesApplication> {

    override fun inject(application: MoviesApplication)
    val authenticationSubcomponentBuilder: AuthenticationSubComponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MoviesApplication): Builder
        fun build(): AppComponent
    }
}
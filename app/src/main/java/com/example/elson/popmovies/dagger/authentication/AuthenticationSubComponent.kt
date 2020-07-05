package com.example.elson.popmovies.dagger.authentication

import com.example.elson.popmovies.ui.login.LoginViewModel
import com.example.elson.popmovies.ui.splash.SplashActivity
import dagger.Subcomponent

@AuthenticationScope
@Subcomponent(modules = [
    AuthenticationModule::class
])
interface AuthenticationSubComponent {

    fun inject(viewModel: LoginViewModel)
    fun inject(splashActivity: SplashActivity)

    @Subcomponent.Builder
    interface Builder {
        fun build(): AuthenticationSubComponent
    }

}
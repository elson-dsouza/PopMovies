package com.example.elson.popmovies.dagger.authentication

import com.example.elson.popmovies.data.repository.AuthenticationRepository
import com.example.elson.popmovies.ui.login.LoginViewModel
import dagger.Subcomponent

@AuthenticationScope
@Subcomponent(modules = [
    AuthenticationModule::class
])
interface AuthenticationComponent {

    fun authenticationRepository(): AuthenticationRepository
    fun inject(viewModel: LoginViewModel)

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthenticationComponent
    }

}
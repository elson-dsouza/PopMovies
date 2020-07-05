package com.example.elson.popmovies.dagger.authentication

import com.example.elson.popmovies.ui.login.LoginViewModel
import dagger.Subcomponent

@AuthenticationScope
@Subcomponent(modules = [
    AuthenticationModule::class
])
interface AuthenticationSubComponent {

    fun inject(viewModel: LoginViewModel)

    @Subcomponent.Builder
    interface Builder {
        fun build(): AuthenticationSubComponent
    }

}
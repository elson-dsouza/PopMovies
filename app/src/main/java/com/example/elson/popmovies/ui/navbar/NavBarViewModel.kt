package com.example.elson.popmovies.ui.navbar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.data.Result
import com.example.elson.popmovies.data.repository.AuthenticationRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NavBarViewModel : ViewModel() {

    @Inject
    lateinit var authenticationRepository: AuthenticationRepository

    private val _navBarItems = MutableLiveData<List<String>>()
    val navBarItems: LiveData<List<String>> = _navBarItems

    private val _logoutResult = MutableSharedFlow<Result<JsonObject>>(replay = 1)
    val logoutResult: SharedFlow<Result<JsonObject>> = _logoutResult

    init {
        AppInjector.getAuthenticationComponent().inject(this)
        refreshNavBarItems()
    }

    fun refreshNavBarItems() {
        val items = mutableListOf<String>()
        items.add("Movies")
        if (authenticationRepository.isUserLoggedIn()) {
            items.add("Logout")
        } else {
            items.add("Login")
        }
        _navBarItems.postValue(items)
    }

    fun performLogOut() {
        viewModelScope.launch {
            val result = authenticationRepository.logoutAsync().await()
            _logoutResult.tryEmit(result)
            refreshNavBarItems()
        }
    }
}

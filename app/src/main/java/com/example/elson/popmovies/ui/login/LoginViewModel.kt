package com.example.elson.popmovies.ui.login

import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elson.popmovies.R
import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.data.AuthenticationRepository
import com.example.elson.popmovies.data.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class LoginViewModel : ViewModel() {

    @Inject lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var generateRequestTokenJob: Job
    private val responseDomain = "https://popmovies.elson.com/"

    private lateinit var clientId: String
    private lateinit var requestToken: String
    private val _loginUrl = MutableLiveData<String>()
    val loginUrl: LiveData<String> = _loginUrl

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    init {
        AppInjector.buildLoginSubComponent().inject(this)
    }

    fun startRequestTokenGeneration() {
        generateRequestTokenJob = viewModelScope.launch {
            while (true) {
                clientId = UUID.randomUUID().toString()
                val responseUri = "$responseDomain$clientId"
                val result = authenticationRepository.generateRequestTokenAsync(
                        responseUri).await()
                if (result is Result.Success) {
                    requestToken = result.data.requestToken
                    _loginUrl.value = "https://www.themoviedb.org/auth/access?request_token=" +
                            requestToken
                    delay(13 * DateUtils.MINUTE_IN_MILLIS)
                } else {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
            }
        }
    }

    fun stopRequestTokenGeneration() {
        generateRequestTokenJob.cancel()
    }

    fun handleOAuthRedirect(url: String?) =
        if (url != null && url.equals("$responseDomain$clientId", true)) {
            requestAccessToken(requestToken)
            true
        } else if (url != null && url.startsWith(responseDomain, true)) {
            _loginResult.value = LoginResult(error = R.string.login_failed)
            true
        } else {
            false
        }

    private fun requestAccessToken(requestToken: String) {
        viewModelScope.launch {
            val result = authenticationRepository
                    .requestAccessTokenAsync(requestToken).await()
            if (result is Result.Success) {
                authenticationRepository.setLoggedInUser(result.data)
                _loginResult.value = LoginResult(success = true)
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }
}
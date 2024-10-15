package com.betan.betanstoktakip.presentation.auth.login

import com.betan.betanstoktakip.core.base.BaseViewModel
import com.betan.betanstoktakip.data.local.LocalRepository
import com.betan.betanstoktakip.domain.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val localRepository: LocalRepository
) : BaseViewModel() {

    private val _eventState = Channel<LoginContract.Event>()
    val eventState: Flow<LoginContract.Event>
        get() = _eventState.receiveAsFlow()

    private val _uiState = MutableStateFlow(LoginContract.UiState())
    val uiState: StateFlow<LoginContract.UiState>
        get() = _uiState

    init {
        getInit()
    }

    fun invoke(action: LoginContract.Action) {
        when (action) {
            is LoginContract.Action.Login -> login(action)
            LoginContract.Action.GetInit -> getInit()
        }
    }

    private fun getInit() {
        _uiState.update {
            it.copy(
                email = localRepository.userEmail.orEmpty()
            )
        }
    }

    private fun login(action: LoginContract.Action.Login) {
        val params = LoginUseCase.Params(
            email = action.email,
            password = action.password
        )
        if (action.email.isEmpty()){
            _failState.trySend("Email alanı boş bırakılamaz")
            return
        }
        if (action.password.isEmpty()){
            _failState.trySend("Şifre alanı boş bırakılamaz")
            return
        }
        loginUseCase.action(params) { result ->
            if (result) {
                _eventState.trySend(LoginContract.Event.GoToNextScreen)
            }
        }
    }

}
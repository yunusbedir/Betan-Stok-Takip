package com.betan.betanstoktakip.presentation.main

import com.betan.betanstoktakip.core.base.BaseViewModel
import com.betan.betanstoktakip.domain.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel() {

    private val _eventState = Channel<MainContract.Event>()
    val eventState: Flow<MainContract.Event>
        get() = _eventState.receiveAsFlow()

    fun invoke(action: MainContract.Action) {
        when (action) {
            is MainContract.Action.Logout -> logout()
        }
    }

    private fun logout() {
        logoutUseCase.action(Unit) {

        }
    }

}
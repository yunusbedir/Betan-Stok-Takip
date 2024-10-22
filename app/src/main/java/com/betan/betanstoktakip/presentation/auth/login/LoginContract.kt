package com.betan.betanstoktakip.presentation.auth.login

object LoginContract {

    sealed interface Action {
        data class Login(
            val email: String,
            val password: String
        ) : Action

        data object GetInit : Action
    }

    sealed interface Event {
        data object GoToNextScreen : Event
    }

    data class UiState(
        val email: String = ""
    )
}
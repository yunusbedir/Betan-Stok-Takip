package com.betan.betanstoktakip.presentation.main

object MainContract {

    sealed interface Action {
        data object Logout : Action
    }

    sealed interface Event {
        data object GoToLogin : Event
    }
}
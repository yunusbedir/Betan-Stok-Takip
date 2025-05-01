package com.betan.betanstoktakip.presentation.showAllProduct

object ShowAllProductContract {
    sealed interface Action {
        data object GetProduct : Action

    }
}
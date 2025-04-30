package com.betan.betanstoktakip.presentation.showstock.showAllProduct

object ShowAllProductContract {
    sealed interface Action {
        data object GetProduct : Action

    }
}
package com.betan.betanstoktakip.presentation.showstock

object ShowStockContract {

    sealed interface Action {
        data object GetStock : Action
    }
}
package com.betan.betanstoktakip.presentation.showstock

object ShowStockContract {

    sealed interface Action {
        data object GetStock : Action
        data object GetBrands: Action
        data object GetProducts: Action
        data object GetSalesExport : Action
        }



}
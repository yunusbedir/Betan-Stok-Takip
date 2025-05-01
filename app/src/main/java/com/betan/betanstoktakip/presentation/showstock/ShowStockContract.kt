package com.betan.betanstoktakip.presentation.showstock

import com.betan.betanstoktakip.domain.model.ProductModel
import com.betan.betanstoktakip.presentation.showproduct.ShowProductContract.Action

object ShowStockContract {

    sealed interface Action {
        data object GetStock : Action
        data object GetBrands: Action
        data object GetSales : Action
        data class GetProduct(val barcode: String) : Action
        }
    data class UiState(
        val barcode: String = "",
        val name: String = "",
        val brandName: String = "",
        val stockAmount: Int = 0,
        val oneAmountPrice: Double = 0.0,
        val purchasePrice: Double = 0.0,
        val amount: Int = 1,
        val productModel: ProductModel? = null
    )



}
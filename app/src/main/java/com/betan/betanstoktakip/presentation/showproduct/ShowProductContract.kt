package com.betan.betanstoktakip.presentation.showproduct

object ShowProductContract {

    sealed interface Action {
        data class GetProduct(
            val barcode: String
        ) : Action

        data class ChangedAmount(
            val amount: Int?
        ) : Action

        data object AddToCart : Action
    }

    data class UiState(
        val barcode: String = "",
        val name: String = "",
        val stockAmount: Int = 0,
        val oneAmountPrice: Double = 0.0,
        val amount: Int = 1,
        val totalPrice: Double = 0.0
    )

}
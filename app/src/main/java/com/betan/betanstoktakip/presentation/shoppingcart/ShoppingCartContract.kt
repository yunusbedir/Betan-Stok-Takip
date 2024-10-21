package com.betan.betanstoktakip.presentation.shoppingcart

import com.betan.betanstoktakip.domain.model.CartProductModel

object ShoppingCartContract {

    sealed interface Action {
        data object GetCartProducts : Action
        data class PlusCartProduct(
            val item: CartProductModel,
        ) : Action

        data class MinusCartProduct(
            val item: CartProductModel,
        ) : Action

        data class DiscountCode(
            val code: String,
        ) : Action

        data class RemoveCartProduct(
            val barcode: String,
        ) : Action

        data object Sell : Action
    }

    sealed interface Event {
        data class RemoveCartProduct(
            val barcode: String,
        ) : Event
    }

    data class UiState(
        val cartProductModels: List<CartProductModel> = listOf(),
        val discountCode: String = "",
        val discountRate: Double = 0.0,
    ) {
        val totalPrice: Double
            get() = cartProductModels.sumOf { it.totalPrice }

        val paidPrice: Double
            get() = totalPrice - totalPrice * discountRate
    }
}
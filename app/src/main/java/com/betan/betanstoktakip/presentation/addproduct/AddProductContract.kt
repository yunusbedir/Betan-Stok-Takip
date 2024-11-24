package com.betan.betanstoktakip.presentation.addproduct

object AddProductContract {

    sealed interface Action {
        data class AddProduct(
            val barcode: String,
            val name: String,
            val brandName: String,
            val stockAmount: Int,
            val purchasePrice: Double,
            val salePrice: Double,
        ) : Action
    }

    sealed interface Event {
        data object AddedProduct : Event
    }
}
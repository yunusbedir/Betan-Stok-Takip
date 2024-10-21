package com.betan.betanstoktakip.domain.model

data class CartProductModel(
    val barcode: String,
    val name: String,
    val amount: Int,
    val salePrice: Double,
    val stockAmount: Int,
) {
    val totalPrice: Double
        get() = salePrice * amount
}
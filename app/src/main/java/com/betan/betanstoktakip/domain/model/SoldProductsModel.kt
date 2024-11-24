package com.betan.betanstoktakip.domain.model

import com.google.firebase.Timestamp

data class SoldProductsModel(
    val date: Timestamp,
    val totalPrice: Double,
    val paidPrice: Double,
    val discountCode: String,
    val discountRate: Double,
    val items: List<Item>,
) {
    data class Item(
        val barcode: String,
        val name: String,
        val brandName: String,
        val amount: Int,
        val salePrice: Double,
        val totalPrice: Double,
        val paidPrice: Double,
    )
}
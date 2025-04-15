package com.betan.betanstoktakip.domain.model

import com.google.firebase.Timestamp

data class SoldProductsModel(
    var date: Timestamp? = null,
    var totalPrice: Double = 0.0,
    var paidPrice: Double = 0.0,
    var discountCode: String = "",
    var discountRate: Double = 0.0,
    var items: List<Item> = emptyList()
) {
    data class Item(
        var barcode: String = "",
        var name: String = "",
        var brandName: String = "",
        var amount: Int = 0,
        var salePrice: Double = 0.0,
        var totalPrice: Double = 0.0,
        var paidPrice: Double = 0.0
    )
}
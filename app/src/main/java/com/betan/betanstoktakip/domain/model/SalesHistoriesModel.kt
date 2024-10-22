package com.betan.betanstoktakip.domain.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp

@Keep
data class SalesHistoriesModel(
    val date: Timestamp? = null,
    val discountCode: String? = null,
    val discountRate: Double? = null,
    val paidPrice: Double? = null,
    val totalPrice: Double? = null,
    val items: List<ItemSalesHistoriesModel> = listOf(),
) {
    data class ItemSalesHistoriesModel(
        val amount: Int? = null,
        val barcode: String? = null,
        val name: String? = null,
        val paidPrice: Double? = null,
        val salePrice: Double? = null,
        val totalPrice: Double? = null,
    )
}
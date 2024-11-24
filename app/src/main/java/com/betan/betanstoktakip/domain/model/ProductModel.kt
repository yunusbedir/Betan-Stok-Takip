package com.betan.betanstoktakip.domain.model

import androidx.annotation.Keep

@Keep
data class ProductModel(
    val barcode: String = "",
    val name: String = "",
    val brandName: String = "",
    val stockAmount: Int = 0,
    val purchasePrice: Double = 0.0,
    val salePrice: Double = 0.0
)
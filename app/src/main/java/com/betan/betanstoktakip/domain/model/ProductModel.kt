package com.betan.betanstoktakip.domain.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class ProductModel(
    var barcode: String? = "",
    var name: String? = "",
    var brandName: String? = "",
    var stockAmount: Int? = 0,
    var purchasePrice: Double? = 0.0,
    var salePrice: Double? = 0.0
): Serializable
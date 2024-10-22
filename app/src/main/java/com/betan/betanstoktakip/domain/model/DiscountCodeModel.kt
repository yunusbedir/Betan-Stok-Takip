package com.betan.betanstoktakip.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DiscountCodeModel(
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("rate")
    val rate: Double? = null,
)
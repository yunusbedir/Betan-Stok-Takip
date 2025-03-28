package com.betan.betanstoktakip.domain.model

import com.google.firebase.Timestamp

data class ShowSellModel (
    val description : String,
    val date: Timestamp,
    val totalPrice: Double,
    val paidPrice: Double,
    val discountCode: String,
    val discountRate: Double,)
{

}
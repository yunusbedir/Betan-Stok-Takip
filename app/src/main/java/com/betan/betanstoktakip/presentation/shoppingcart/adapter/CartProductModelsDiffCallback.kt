package com.betan.betanstoktakip.presentation.shoppingcart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.betan.betanstoktakip.domain.model.CartProductModel

class CartProductModelsDiffCallback :
    DiffUtil.ItemCallback<CartProductModel>() {

    override fun areItemsTheSame(
        oldItem: CartProductModel,
        newItem: CartProductModel
    ): Boolean {
        return oldItem.barcode == newItem.barcode
    }

    override fun areContentsTheSame(
        oldItem: CartProductModel,
        newItem: CartProductModel
    ): Boolean {
        return oldItem.barcode == newItem.barcode && oldItem.amount == newItem.amount && oldItem.totalPrice == newItem.totalPrice
    }
}
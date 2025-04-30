package com.betan.betanstoktakip.presentation.showproduct.updateproduct

import com.betan.betanstoktakip.domain.model.ProductModel


object UpdateProductContract {
    sealed interface Action{
        data class SetInitialProduct(val product: ProductModel) : Action
        data class UpdateProduct(val updatedProduct: ProductModel , val oldBarcode: String? = null ) : Action
        data class LoadProduct(val barcode: String) : Action
    }
    data class UiState(
        val barcode: String = "",
        val name: String = "",
        val brandName: String = "",
        val stockAmount: Int = 0,
        val salePrice: Double = 0.0,
        val purchasePrice: Double = 0.0,
        val productModel: ProductModel? = null
    )

}
package com.betan.betanstoktakip.domain.usecases.product

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.data.local.LocalRepository
import com.betan.betanstoktakip.domain.model.CartProductModel
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val localRepository: LocalRepository
) : UseCase<AddToCartUseCase.Params, Unit>() {

    override suspend fun run(params: Params) {
        val newList = arrayListOf<CartProductModel>()
        val list = localRepository.cartProductList.orEmpty()
        newList.addAll(list)
        if (newList.find { it.barcode == params.barcode } == null) {
            newList.add(
                CartProductModel(
                    barcode = params.barcode,
                    name = params.name,
                    brandName = params.brandName,
                    amount = params.amount,
                    salePrice = params.salePrice,
                    stockAmount = params.stockAmount,
                )
            )
            localRepository.cartProductList = newList
        } else
            throw Exception("Bu ürün zaten sepette bulunmaktadır.")
    }

    data class Params(
        val barcode: String,
        val name: String,
        val brandName: String,
        val amount: Int,
        val salePrice: Double,
        val totalPrice: Double,
        val stockAmount: Int,
    )
}
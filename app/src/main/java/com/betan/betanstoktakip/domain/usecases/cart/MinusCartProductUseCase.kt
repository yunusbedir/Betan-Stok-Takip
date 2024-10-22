package com.betan.betanstoktakip.domain.usecases.cart

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.data.local.LocalRepository
import com.betan.betanstoktakip.domain.model.CartProductModel
import javax.inject.Inject

class MinusCartProductUseCase @Inject constructor(
    private val localRepository: LocalRepository
) : UseCase<MinusCartProductUseCase.Params, List<CartProductModel>>() {

    override suspend fun run(params: Params): List<CartProductModel> {
        val newList = localRepository.cartProductList.orEmpty().map { item ->
            var amount = item.amount
            if (item.barcode == params.barcode)
                amount--

            item.copy(
                amount = amount,
            )
        }
        localRepository.cartProductList = newList
        return newList
    }

    data class Params(
        val barcode: String
    )
}
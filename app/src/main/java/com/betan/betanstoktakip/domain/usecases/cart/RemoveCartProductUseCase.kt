package com.betan.betanstoktakip.domain.usecases.cart

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.data.local.LocalRepository
import com.betan.betanstoktakip.domain.model.CartProductModel
import javax.inject.Inject

class RemoveCartProductUseCase @Inject constructor(
    private val localRepository: LocalRepository
) : UseCase<RemoveCartProductUseCase.Params, List<CartProductModel>>() {

    override suspend fun run(params: Params): List<CartProductModel> {
        val newList = localRepository.cartProductList.orEmpty().filter { item ->
            item.barcode != params.barcode
        }
        localRepository.cartProductList = newList
        return newList
    }

    data class Params(
        val barcode: String
    )
}
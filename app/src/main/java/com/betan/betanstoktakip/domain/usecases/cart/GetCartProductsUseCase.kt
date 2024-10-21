package com.betan.betanstoktakip.domain.usecases.cart

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.data.local.LocalRepository
import com.betan.betanstoktakip.domain.model.CartProductModel
import javax.inject.Inject

class GetCartProductsUseCase @Inject constructor(
    private val localRepository: LocalRepository
) : UseCase<Unit, List<CartProductModel>>() {

    override suspend fun run(params: Unit): List<CartProductModel> {
        return localRepository.cartProductList.orEmpty()
    }
}
package com.betan.betanstoktakip.domain.usecases.cart

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.core.extensions.orZero
import com.betan.betanstoktakip.domain.model.DiscountCodeModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetDiscountRateUseCase @Inject constructor(
) : UseCase<GetDiscountRateUseCase.Params, Double>() {

    override suspend fun run(params: Params): Double {
        val response =
            Firebase.firestore.collection("Codes").document(params.discountCode).get()
                .await()

        return response.toObject<DiscountCodeModel>()?.rate.orZero()
    }

    data class Params(
        val discountCode: String
    )
}
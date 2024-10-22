package com.betan.betanstoktakip.domain.usecases.product

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.domain.model.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetProductUseCase @Inject constructor() : UseCase<GetProductUseCase.Params, ProductModel>() {
    override suspend fun run(params: Params): ProductModel {
        val response =
            Firebase.firestore.collection("Products").document(params.barcode).get().await()
        return response.toObject<ProductModel>()!!
    }

    data class Params(
        val barcode: String
    )
}
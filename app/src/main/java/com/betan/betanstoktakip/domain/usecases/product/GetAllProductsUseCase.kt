package com.betan.betanstoktakip.domain.usecases.product

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.domain.firebase.FirebaseCollections
import com.betan.betanstoktakip.domain.model.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
) : UseCase<Unit, List<ProductModel>>() {

    override suspend fun run(params: Unit): List<ProductModel> {
        val response = Firebase.firestore.collection(FirebaseCollections.PRODUCTS)
            .get().await()

        return response.toObjects<ProductModel>()
    }
}
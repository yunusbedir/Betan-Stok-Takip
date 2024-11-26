package com.betan.betanstoktakip.domain.usecases.product

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.domain.firebase.FirebaseCollections
import com.betan.betanstoktakip.domain.firebase.FirebaseFields
import com.betan.betanstoktakip.domain.model.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
) : UseCase<GetProductsUseCase.Params, List<ProductModel>>() {

    override suspend fun run(params: Params): List<ProductModel> {
        val response = Firebase.firestore.collection(FirebaseCollections.PRODUCTS)
            .whereEqualTo(params.field.fieldName, params.query)
            .get().await()

        return response.toObjects<ProductModel>()
    }

    data class Params(val field: Field, val query: String) {

        enum class Field(val fieldName: String) {
            BRAND(FirebaseFields.BRAND_NAME),
            BARCODE(FirebaseFields.BARCODE),
        }
    }
}
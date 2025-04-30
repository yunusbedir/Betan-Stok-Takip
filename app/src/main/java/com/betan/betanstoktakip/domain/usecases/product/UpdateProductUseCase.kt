package com.betan.betanstoktakip.domain.usecases.product

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.domain.firebase.FirebaseCollections
import com.betan.betanstoktakip.domain.model.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class UpdateProductUseCase @Inject constructor() : UseCase<UpdateProductUseCase.Params, Unit>() {
    val firestore = Firebase.firestore
    override suspend fun run(params: Params) {
        Firebase.firestore.collection(FirebaseCollections.PRODUCTS)
            .document(params.product.barcode.orEmpty())
            .set(params.product)
            .await()
        params.oldProductBarcodeToDelete?.let { barcode ->
            if (barcode != params.product.barcode) {
                firestore.collection(FirebaseCollections.PRODUCTS)
                    .document(barcode)
                    .delete()
                    .await()
            }
        }
    }



    data class Params(
        val product: ProductModel,
        val oldProductBarcodeToDelete: String? = null
    )
}
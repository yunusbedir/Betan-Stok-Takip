package com.betan.betanstoktakip.domain.usecases.product

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.domain.firebase.FirebaseCollections
import com.betan.betanstoktakip.domain.model.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddProductUseCase @Inject constructor(

) : UseCase<AddProductUseCase.Params, Unit>() {
    override suspend fun run(params: Params) {
        val productInFirebase = Firebase.firestore.collection(FirebaseCollections.PRODUCTS)
            .document(params.barcode)
            .get()
            .await()

        if (productInFirebase.exists().not()) {
            Firebase.firestore.collection(FirebaseCollections.PRODUCTS)
                .document(params.barcode)
                .set(params.toRequestModel())
                .await()
        } else {
            throw Exception("Ürün zaten var.")
        }
    }

    data class Params(
        val barcode: String,
        val name: String,
        val brandName: String,
        val stockAmount: Int,
        val purchasePrice: Double,
        val salePrice: Double,
    )

    companion object {
        private fun Params.toRequestModel(): ProductModel {
            return ProductModel(
                barcode = barcode,
                name = name,
                brandName = brandName,
                stockAmount = stockAmount,
                purchasePrice = purchasePrice,
                salePrice = salePrice,
            )
        }
    }
}
package com.betan.betanstoktakip.domain.usecases.cart

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.core.extensions.orZero
import com.betan.betanstoktakip.data.local.LocalRepository
import com.betan.betanstoktakip.domain.firebase.FirebaseCollections
import com.betan.betanstoktakip.domain.firebase.FirebaseFields
import com.betan.betanstoktakip.domain.model.CartProductModel
import com.betan.betanstoktakip.domain.model.ProductModel
import com.betan.betanstoktakip.domain.model.SoldProductsModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class SellCartProductsUseCase @Inject constructor(
    private val localRepository: LocalRepository
) : UseCase<SellCartProductsUseCase.Params, Unit>() {

    override suspend fun run(params: Params) {
        val db = Firebase.firestore
        val batch = db.batch()

        db.collection(FirebaseCollections.SOLD_PRODUCTS)
            .add(params.toRequestModel())
            .await()

        db.collection(FirebaseCollections.PRODUCTS)
            .whereIn(FirebaseFields.BARCODE, params.items.map { it.barcode })
            .get()
            .await().documents.onEach { snapsShot ->
                val item = snapsShot.toObject<ProductModel>()
                item?.copy(
                    stockAmount = item.stockAmount - params.items.filter {
                        it.barcode == item.barcode
                    }.sumOf { it.amount }.orZero()
                )?.let {
                    val ref = db.collection(FirebaseCollections.PRODUCTS).document(it.barcode)
                    batch.set(ref, it)
                }
            }

        batch.commit().await()
        localRepository.cartProductList = listOf()
    }

    data class Params(
        val items: List<CartProductModel>,
        val discountCode: String,
        val discountRate: Double
    ) {
        val totalPrice: Double
            get() = items.sumOf { it.totalPrice }

        val paidPrice: Double
            get() = totalPrice - totalPrice * discountRate
    }

    companion object {
        fun Params.toRequestModel(): SoldProductsModel {
            return SoldProductsModel(
                date = Timestamp(Date()),
                totalPrice = totalPrice,
                paidPrice = paidPrice,
                discountCode = discountCode,
                discountRate = discountRate,
                items = items.map { item ->
                    val itemPaidPrice = item.totalPrice - item.totalPrice * discountRate
                    SoldProductsModel.Item(
                        barcode = item.barcode,
                        name = item.name,
                        brandName = item.brandName,
                        amount = item.amount,
                        salePrice = item.salePrice,
                        totalPrice = item.totalPrice,
                        paidPrice = itemPaidPrice,
                    )
                },
            )
        }
    }

}
package com.betan.betanstoktakip.domain.usecases.product

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.domain.firebase.FirebaseCollections
import com.betan.betanstoktakip.domain.model.SoldProductsModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetShowSellUseCase @Inject constructor(): UseCase<Unit, List<SoldProductsModel>>(){




    override suspend fun run(params: Unit): List<SoldProductsModel> {
        val response = Firebase.firestore.collection(FirebaseCollections.SOLD_PRODUCTS)
            .orderBy("date",Query.Direction.DESCENDING)
            .get()
            .await()

        return response.toObjects<SoldProductsModel>()
    }
}

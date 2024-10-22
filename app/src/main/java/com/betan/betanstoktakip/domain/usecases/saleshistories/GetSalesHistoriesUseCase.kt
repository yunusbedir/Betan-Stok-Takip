package com.betan.betanstoktakip.domain.usecases.saleshistories

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.domain.firebase.FirebaseCollections
import com.betan.betanstoktakip.domain.model.SalesHistoriesModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetSalesHistoriesUseCase @Inject constructor(

) : UseCase<Unit, List<SalesHistoriesModel>>() {

    override suspend fun run(params: Unit): List<SalesHistoriesModel> {
        val response = Firebase.firestore.collection(FirebaseCollections.SALES_HISTORIES)
            .get()
            .await()
        return response.toObjects<SalesHistoriesModel>()
    }
}
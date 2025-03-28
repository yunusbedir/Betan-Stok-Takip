package com.betan.betanstoktakip.domain.usecases.product

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.domain.firebase.FirebaseCollections
import com.betan.betanstoktakip.domain.model.ShowSellModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetShowSellUseCase @Inject constructor(): UseCase<Unit, List<ShowSellModel>>(){




    override suspend fun run(params: Unit): List<ShowSellModel> {
        val response = Firebase.firestore.collection(FirebaseCollections.SALES_EXPORT)
            .get()
            .await()
        return response.toObjects<ShowSellModel>()
    }
}

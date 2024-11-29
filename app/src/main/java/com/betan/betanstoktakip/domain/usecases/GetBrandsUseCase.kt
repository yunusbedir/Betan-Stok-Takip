package com.betan.betanstoktakip.domain.usecases

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.domain.firebase.FirebaseCollections
import com.betan.betanstoktakip.domain.model.BrandModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetBrandsUseCase @Inject constructor(

) : UseCase<Unit, List<BrandModel>>() {

    override suspend fun run(params: Unit): List<BrandModel> {
        val response = Firebase.firestore.collection(FirebaseCollections.BRANDS)
            .get()
            .await()
        return response.toObjects<BrandModel>()
    }
}

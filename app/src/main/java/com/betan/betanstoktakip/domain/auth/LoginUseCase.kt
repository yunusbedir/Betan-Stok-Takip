package com.betan.betanstoktakip.domain.auth

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.data.local.LocalRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val localRepository: LocalRepository
) : UseCase<LoginUseCase.Params, Boolean>() {

    override suspend fun run(params: Params): Boolean {
        val result = Firebase.auth.signInWithEmailAndPassword(
            params.email,
            params.password
        ).await()
        if (result.user != null) {
            localRepository.userEmail = params.email
        }
        return result.user != null
    }

    data class Params(
        val email: String,
        val password: String
    )

}
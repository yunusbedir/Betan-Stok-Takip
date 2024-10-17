package com.betan.betanstoktakip.domain.auth

import com.betan.betanstoktakip.core.base.domain.UseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class LogoutUseCase @Inject constructor() : UseCase<Unit, Unit>() {

    override suspend fun run(params: Unit) {
        Firebase.auth.signOut()
    }

}
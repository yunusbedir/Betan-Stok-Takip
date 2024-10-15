package com.betan.betanstoktakip.data.local

import android.content.SharedPreferences
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : LocalRepository {

    override var userEmail: String?
        set(value) {
            sharedPreferences.edit()
                .putString("USER_EMAIL", value)
                .apply()
        }
        get() {
           return sharedPreferences.getString("USER_EMAIL", null)
        }
}
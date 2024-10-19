package com.betan.betanstoktakip.data.local

import android.content.SharedPreferences
import com.betan.betanstoktakip.domain.model.CartProductModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : LocalRepository {

    override var userEmail: String?
        get() = sharedPreferences.getString("USER_EMAIL", null)
        set(value) {
            sharedPreferences.edit()
                .putString("USER_EMAIL", value)
                .apply()
        }

    override var cartProductList: List<CartProductModel>?
        get() = sharedPreferences.getFromJsonToList<CartProductModel>("CART_TO_PRODUCTS")
        set(value) {
            sharedPreferences.setToJson("CART_TO_PRODUCTS", value)
        }

    private fun <T> SharedPreferences.setToJson(
        key: String,
        value: T,
    ) {
        Gson().toJson(value).let {
            with(edit()) {
                putString(key, it)
                apply()
            }
        }
    }

    private inline fun <reified T> SharedPreferences.getFromJsonToList(key: String): List<T>? {
        val json = getString(key, "")
        return if (json.isNullOrEmpty()) {
            null
        } else {
            val type = object : TypeToken<List<T>>() {}.type
            Gson().fromJson<List<T>>(json, type)
        }
    }
}
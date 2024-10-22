package com.betan.betanstoktakip.data.local

import com.betan.betanstoktakip.domain.model.CartProductModel

interface LocalRepository {
    var userEmail: String?
    var cartProductList: List<CartProductModel>?
}
package com.betan.betanstoktakip.presentation.showstock

import com.betan.betanstoktakip.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowStockViewModel @Inject constructor() : BaseViewModel() {

    fun invoke(action: ShowStockContract.Action) {
        when (action) {
            ShowStockContract.Action.GetStock -> getStock()
        }
    }

    private fun getStock() {

    }
}
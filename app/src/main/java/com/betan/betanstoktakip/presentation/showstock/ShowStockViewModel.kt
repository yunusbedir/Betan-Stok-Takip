package com.betan.betanstoktakip.presentation.showstock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.betan.betanstoktakip.core.base.BaseViewModel
import com.betan.betanstoktakip.domain.model.BrandModel
import com.betan.betanstoktakip.domain.usecases.GetBrandsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowStockViewModel@Inject constructor(private val getBrandsUseCase: GetBrandsUseCase) : BaseViewModel() {

    private val _getBrandLiveData= MutableLiveData<List<BrandModel?>>()
    val getBrandLiveData : LiveData<List<BrandModel?>>
        get() = _getBrandLiveData

    fun invoke(action: ShowStockContract.Action) {
        when (action) {
            ShowStockContract.Action.GetStock -> getStock()
        }
    }

    fun getBrand(){
        getBrandsUseCase.action(Unit) { result ->
            _getBrandLiveData.value = result
        }
    }
    private fun getStock() {

    }
}
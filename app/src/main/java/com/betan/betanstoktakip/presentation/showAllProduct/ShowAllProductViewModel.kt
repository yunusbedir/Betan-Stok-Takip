package com.betan.betanstoktakip.presentation.showAllProduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.betan.betanstoktakip.core.base.BaseViewModel
import com.betan.betanstoktakip.domain.model.ProductModel
import com.betan.betanstoktakip.domain.usecases.product.GetAllProductsUseCase
import com.betan.betanstoktakip.presentation.showstock.ShowStockContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowAllProductViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,

    ): BaseViewModel() {

    private val _getAllProductsLiveData = MutableLiveData<List<ProductModel?>>()
    val getAllProductsLiveData: LiveData<List<ProductModel?>>
        get() = _getAllProductsLiveData
    fun invoke(action: ShowAllProductContract.Action) {
        when (action) {

            ShowAllProductContract.Action.GetProduct -> getAllProduct()
        }
    }
    fun getAllProduct() {
        getAllProductsUseCase.action(Unit) { result ->
            _getAllProductsLiveData.value = result
        }
    }
}
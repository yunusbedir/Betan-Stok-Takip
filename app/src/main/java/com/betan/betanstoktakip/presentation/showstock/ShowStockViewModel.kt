package com.betan.betanstoktakip.presentation.showstock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.betan.betanstoktakip.core.base.BaseViewModel
import com.betan.betanstoktakip.domain.model.BrandModel
import com.betan.betanstoktakip.domain.model.ProductModel
import com.betan.betanstoktakip.domain.usecases.GetBrandsUseCase
import com.betan.betanstoktakip.domain.usecases.product.GetAllProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class ShowStockViewModel@Inject constructor(private val getBrandsUseCase: GetBrandsUseCase, private val getAllProductsUseCase: GetAllProductsUseCase) : BaseViewModel() {

    private val _getBrandLiveData= MutableLiveData<List<BrandModel?>>()
    private val _getAllProductsLiveData = MutableLiveData<List<ProductModel?>>()
    val getAllProductsLiveData : LiveData<List<ProductModel?>>
        get() = _getAllProductsLiveData
    val getBrandLiveData : LiveData<List<BrandModel?>>
        get() = _getBrandLiveData

    fun invoke(action: ShowStockContract.Action) {
        when (action) {
            ShowStockContract.Action.GetStock -> getStock()
            ShowStockContract.Action.GetBrands -> getBrand()
            ShowStockContract.Action.GetProducts -> getAllProduct()
        }
    }

    fun getBrand(){
        getBrandsUseCase.action(Unit) { result ->
            _getBrandLiveData.value = result
        }
    }
    fun getAllProduct() {
        getAllProductsUseCase.action(Unit) { result ->
            _getAllProductsLiveData.value = result
        }
    }
        private fun getStock() {}


}
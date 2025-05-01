package com.betan.betanstoktakip.presentation.showstock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.betan.betanstoktakip.core.base.BaseViewModel
import com.betan.betanstoktakip.domain.model.BrandModel
import com.betan.betanstoktakip.domain.model.ProductModel
import com.betan.betanstoktakip.domain.model.SoldProductsModel
import com.betan.betanstoktakip.domain.usecases.GetBrandsUseCase
import com.betan.betanstoktakip.domain.usecases.product.GetAllProductsUseCase
import com.betan.betanstoktakip.domain.usecases.product.GetProductUseCase
import com.betan.betanstoktakip.domain.usecases.product.GetShowSellUseCase
import com.betan.betanstoktakip.presentation.showproduct.ShowProductContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ShowStockViewModel @Inject constructor(
    private val getBrandsUseCase: GetBrandsUseCase,
    private val getShowSellUseCase: GetShowSellUseCase,
    private val getProductUseCase: GetProductUseCase,
) : BaseViewModel() {

    private val _getBrandLiveData = MutableLiveData<List<BrandModel?>>()
    private val _getSalesLiveData = MutableLiveData<List<SoldProductsModel?>>()
    private val _getProductLiveData = MutableLiveData<List<ProductModel?>>()

    val getBrandLiveData: LiveData<List<BrandModel?>>
        get() = _getBrandLiveData
    val getSalesLiveData: LiveData<List<SoldProductsModel?>>
        get() = _getSalesLiveData
    val getProductLiveData: LiveData<List<ProductModel?>>
        get() = _getProductLiveData
    private val _uiState = MutableStateFlow(ShowStockContract.UiState())
    val uiState: StateFlow<ShowStockContract.UiState>
        get() = _uiState

    fun invoke(action: ShowStockContract.Action) {
        when (action) {
            ShowStockContract.Action.GetStock -> getStock()
            ShowStockContract.Action.GetBrands -> getBrand()
            ShowStockContract.Action.GetSales -> getSales()
            is ShowStockContract.Action.GetProduct -> getProduct(action.barcode)

        }
    }

    private fun getSales() {
        getShowSellUseCase.action(Unit){ result ->
            _getSalesLiveData.value = result
        }
    }

    fun getBrand() {
        getBrandsUseCase.action(Unit) { result ->
            _getBrandLiveData.value = result
        }
    }

    private fun getStock() {

    }
    fun getProduct(barcode: String) {
        val params = GetProductUseCase.Params(barcode = barcode)
        getProductUseCase.action(params) { result ->
            _getProductLiveData.value = listOf(result) // Tek ürün olsa bile listeye koyduk
        }
    }


}
package com.betan.betanstoktakip.presentation.showproduct.updateproduct

import androidx.lifecycle.viewModelScope
import com.betan.betanstoktakip.core.base.BaseViewModel
import com.betan.betanstoktakip.core.extensions.orZero
import com.betan.betanstoktakip.domain.model.ProductModel
import com.betan.betanstoktakip.domain.usecases.product.UpdateProductUseCase
import com.betan.betanstoktakip.domain.usecases.product.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProductViewModel @Inject constructor(
    private val updateProductUseCase: UpdateProductUseCase,
    private val getProductUseCase: GetProductUseCase // GetProductUseCase'i enjekte et
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UpdateProductContract.UiState())
    val uiState: StateFlow<UpdateProductContract.UiState> get() = _uiState

    fun invoke(action: UpdateProductContract.Action) {
        when (action) {
            is UpdateProductContract.Action.UpdateProduct -> updateProduct(action)
            is UpdateProductContract.Action.SetInitialProduct -> setInitialProduct(action.product)
            is UpdateProductContract.Action.LoadProduct -> loadProduct(action.barcode)
        }
    }

    // Firebase'den ürünü al ve UI'ya yükle
    private fun loadProduct(barcode: String) {
        viewModelScope.launch {
            try {
                val product = getProductUseCase.run(GetProductUseCase.Params(barcode))
                setInitialProduct(product)
            } catch (e: Exception) {
                // Hata yönetimi yapılabilir
                _failState.trySend("Ürün verisi alınamadı.")
            }
        }
    }

    // Ürün güncelleme işlemi
    private fun updateProduct(action: UpdateProductContract.Action.UpdateProduct) {
        val params = UpdateProductUseCase.Params(
            product = action.updatedProduct,
            oldProductBarcodeToDelete = action.oldBarcode?.takeIf { it != action.updatedProduct.barcode }
        )

        updateProductUseCase.action(params) {
            _successState.trySend("Ürün başarıyla güncellendi.")

            _uiState.value = _uiState.value.copy(
                productModel = action.updatedProduct,
                barcode = action.updatedProduct.barcode.orEmpty(),
                name = action.updatedProduct.name.orEmpty(),
                brandName = action.updatedProduct.brandName.orEmpty(),
                stockAmount = action.updatedProduct.stockAmount.orZero(),
                purchasePrice = action.updatedProduct.purchasePrice.orZero(),
                salePrice = action.updatedProduct.salePrice.orZero(),
            )
        }
    }

    // İlk ürünü UI'ya yüklemek için
    private fun setInitialProduct(product: ProductModel) {
        _uiState.value = _uiState.value.copy(
            productModel = product,
            barcode = product.barcode.orEmpty(),
            name = product.name.orEmpty(),
            brandName = product.brandName.orEmpty(),
            stockAmount = product.stockAmount.orZero(),
            purchasePrice = product.purchasePrice.orZero(),
            salePrice = product.salePrice.orZero(),
        )
    }
}

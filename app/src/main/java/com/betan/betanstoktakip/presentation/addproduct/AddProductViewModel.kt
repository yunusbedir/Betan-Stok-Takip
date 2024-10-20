package com.betan.betanstoktakip.presentation.addproduct

import com.betan.betanstoktakip.core.base.BaseViewModel
import com.betan.betanstoktakip.core.extensions.orZero
import com.betan.betanstoktakip.domain.usecases.product.AddProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val addProductUseCase: AddProductUseCase
) : BaseViewModel() {

    private val _eventState = Channel<AddProductContract.Event>()
    val eventState: Flow<AddProductContract.Event>
        get() = _eventState.receiveAsFlow()

    fun invoke(action: AddProductContract.Action) {
        when (action) {
            is AddProductContract.Action.AddProduct -> addProduct(action)
        }
    }

    private fun addProduct(action: AddProductContract.Action.AddProduct) {
        if (action.barcode.isEmpty() ||
            action.name.isEmpty() ||
            action.stockAmount == 0 ||
            action.purchasePrice == 0.0 ||
            action.salePrice == 0.0
        ) {
            _failState.trySend("Eksik ya da yanlış veri girdin.\nGiriş yaptığın verileri kontrol et.")
            return
        }
        val params = AddProductUseCase.Params(
            barcode = action.barcode,
            name = action.name,
            stockAmount = action.stockAmount.orZero(),
            purchasePrice = action.purchasePrice.orZero(),
            salePrice = action.salePrice.orZero(),
        )
        addProductUseCase.action(params) {
            _eventState.trySend(AddProductContract.Event.AddedProduct)
        }
    }

}
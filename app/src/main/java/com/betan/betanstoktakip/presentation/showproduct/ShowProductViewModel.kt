package com.betan.betanstoktakip.presentation.showproduct

import com.betan.betanstoktakip.core.base.BaseViewModel
import com.betan.betanstoktakip.domain.usecases.product.AddToCartUseCase
import com.betan.betanstoktakip.domain.usecases.product.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ShowProductViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ShowProductContract.UiState())
    val uiState: StateFlow<ShowProductContract.UiState>
        get() = _uiState

    fun invoke(action: ShowProductContract.Action) {
        when (action) {
            is ShowProductContract.Action.GetProduct -> getProduct(action)
            is ShowProductContract.Action.AddToCart -> addToCart()
            ShowProductContract.Action.MinusCartProduct -> minusCartProduct()
            ShowProductContract.Action.PlusCartProduct -> plusCartProduct()
        }
    }

    private fun plusCartProduct() {
        _uiState.update { state ->
            val amount = state.amount + 1
            if (amount >= state.stockAmount) {
                _failState.trySend("Maximum ${state.stockAmount} Adet ürün alabilirsin.")
                state
            } else
                state.copy(
                    amount = amount
                )
        }
    }

    private fun minusCartProduct() {
        _uiState.update { state ->
            val amount = state.amount - 1
            if (amount <= 0) {
                _failState.trySend("Minimum 1 Adet ürün ekleyebilirsin.")
                state
            } else
                state.copy(
                    amount = amount
                )
        }
    }

    private fun getProduct(action: ShowProductContract.Action.GetProduct) {
        val params = GetProductUseCase.Params(
            barcode = action.barcode
        )
        getProductUseCase.action(params) { result ->
            _uiState.update { state ->
                state.copy(
                    barcode = result.barcode,
                    name = result.name,
                    stockAmount = result.stockAmount,
                    oneAmountPrice = result.salePrice,
                    amount = 1,
                )
            }
        }
    }

    private fun addToCart() {
        _uiState.value.let { state ->
            val params = AddToCartUseCase.Params(
                barcode = state.barcode,
                name = state.name,
                brandName = state.brandName,
                amount = state.amount,
                salePrice = state.oneAmountPrice,
                totalPrice = state.totalPrice,
                stockAmount = state.stockAmount
            )
            addToCartUseCase.action(params) {
                _uiState.update {
                    ShowProductContract.UiState()
                }
            }
        }
    }
}
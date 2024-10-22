package com.betan.betanstoktakip.presentation.shoppingcart

import com.betan.betanstoktakip.core.base.BaseViewModel
import com.betan.betanstoktakip.domain.usecases.cart.GetCartProductsUseCase
import com.betan.betanstoktakip.domain.usecases.cart.GetDiscountRateUseCase
import com.betan.betanstoktakip.domain.usecases.cart.MinusCartProductUseCase
import com.betan.betanstoktakip.domain.usecases.cart.PlusCartProductUseCase
import com.betan.betanstoktakip.domain.usecases.cart.RemoveCartProductUseCase
import com.betan.betanstoktakip.domain.usecases.cart.SellCartProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val getDiscountRateUseCase: GetDiscountRateUseCase,
    private val minusCartProductUseCase: MinusCartProductUseCase,
    private val plusCartProductUseCase: PlusCartProductUseCase,
    private val getCartProductsUseCase: GetCartProductsUseCase,
    private val removeCartProductUseCase: RemoveCartProductUseCase,
    private val sellCartProductsUseCase: SellCartProductsUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ShoppingCartContract.UiState())
    val uiState: StateFlow<ShoppingCartContract.UiState>
        get() = _uiState

    private val _eventState = Channel<ShoppingCartContract.Event>()
    val eventState: Flow<ShoppingCartContract.Event>
        get() = _eventState.receiveAsFlow()


    fun invoke(action: ShoppingCartContract.Action) {
        when (action) {
            ShoppingCartContract.Action.GetCartProducts -> getCartProducts()
            is ShoppingCartContract.Action.DiscountCode -> discountCode(action)
            is ShoppingCartContract.Action.MinusCartProduct -> minusCartProduct(action)
            is ShoppingCartContract.Action.PlusCartProduct -> plusCartProduct(action)
            is ShoppingCartContract.Action.RemoveCartProduct -> removeCartProduct(action)
            ShoppingCartContract.Action.Sell -> sell()
        }
    }

    private fun getCartProducts() {
        getCartProductsUseCase.action(Unit) { result ->
            _uiState.update { state ->
                state.copy(
                    cartProductModels = result,
                )
            }
        }
    }

    private fun discountCode(action: ShoppingCartContract.Action.DiscountCode) {
        val params = GetDiscountRateUseCase.Params(
            discountCode = action.code
        )
        getDiscountRateUseCase.action(params) { result ->
            _uiState.update { state ->
                state.copy(
                    discountCode = action.code,
                    discountRate = result,
                )
            }
        }
    }

    private fun minusCartProduct(action: ShoppingCartContract.Action.MinusCartProduct) {
        if (action.item.amount <= 1) {
            _eventState.trySend(
                ShoppingCartContract.Event.RemoveCartProduct(
                    barcode = action.item.barcode
                )
            )
            return
        }
        val params = MinusCartProductUseCase.Params(
            barcode = action.item.barcode
        )
        minusCartProductUseCase.action(params) { result ->
            _uiState.update { state ->
                state.copy(
                    cartProductModels = result,
                )
            }
        }
    }

    private fun plusCartProduct(action: ShoppingCartContract.Action.PlusCartProduct) {
        val params = PlusCartProductUseCase.Params(
            barcode = action.item.barcode
        )
        plusCartProductUseCase.action(params) { result ->
            _uiState.update { state ->
                state.copy(
                    cartProductModels = result,
                )
            }
        }
    }

    private fun removeCartProduct(action: ShoppingCartContract.Action.RemoveCartProduct) {
        val params = RemoveCartProductUseCase.Params(
            barcode = action.barcode
        )
        removeCartProductUseCase.action(params) { result ->
            _uiState.update { state ->
                state.copy(
                    cartProductModels = result,
                )
            }
        }
    }

    private fun sell() {
        _uiState.value.let { state ->
            val params = SellCartProductsUseCase.Params(
                items = state.cartProductModels,
                discountCode = state.discountCode,
                discountRate = state.discountRate,
            )
            sellCartProductsUseCase.action(params) {
                _uiState.update { ShoppingCartContract.UiState() }
            }
        }
    }
}